package net.kamradtfamily.winecheeseapp

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*


class WineCheeseViewModel(repository: WineCheesePostRepository, val savedStateHandle: SavedStateHandle)  : ViewModel() {
    companion object {
        const val KEY_SUBREDDIT = "subreddit"
        const val DEFAULT_SUBREDDIT = "androiddev"
    }

    init {
        if (!savedStateHandle.contains(KEY_SUBREDDIT)) {
            savedStateHandle.set(KEY_SUBREDDIT, DEFAULT_SUBREDDIT)
        }
    }

    private val clearListCh = Channel<Unit>(Channel.CONFLATED)

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val posts = flowOf(
        clearListCh.receiveAsFlow().map { PagingData.empty<WineCheesePost>() },
        savedStateHandle.getLiveData<Int>(KEY_SUBREDDIT)
            .asFlow()
            .flatMapLatest { repository.postsOfWineCheese(it, 30) }
            // cachedIn() shares the paging state across multiple consumers of posts,
            // e.g. different generations of UI across rotation config change
            .cachedIn(viewModelScope)
    ).flattenMerge(2)

    fun shouldShowSubreddit(
        subreddit: String
    ) = savedStateHandle.get<String>(KEY_SUBREDDIT) != subreddit

    fun showSubreddit(subreddit: String) {
        if (!shouldShowSubreddit(subreddit)) return

        clearListCh.offer(Unit)

        savedStateHandle.set(KEY_SUBREDDIT, subreddit)
    }


}
