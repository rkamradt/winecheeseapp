package net.kamradtfamily.winecheeseapp

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil

class PostsAdapter()
    : PagingDataAdapter<WineCheesePost, WineCheesePostViewHolder>(POST_COMPARATOR) {

    override fun onBindViewHolder(holder: WineCheesePostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(
        holder: WineCheesePostViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            val item = getItem(position)
            holder.updateScore(item)
        } else {
            onBindViewHolder(holder, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WineCheesePostViewHolder {
        return WineCheesePostViewHolder.create(parent)
    }

    companion object {
        private val PAYLOAD_SCORE = Any()
        val POST_COMPARATOR = object : DiffUtil.ItemCallback<WineCheesePost>() {
            override fun areContentsTheSame(oldItem: WineCheesePost, newItem: WineCheesePost): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: WineCheesePost, newItem: WineCheesePost): Boolean =
                oldItem.id == newItem.id

            override fun getChangePayload(oldItem: WineCheesePost, newItem: WineCheesePost): Any? {
                return if (sameExceptScore(oldItem, newItem)) {
                    PAYLOAD_SCORE
                } else {
                    null
                }
            }
        }

        private fun sameExceptScore(oldItem: WineCheesePost, newItem: WineCheesePost): Boolean {
            // DON'T do this copy in a real app, it is just convenient here for the demo :)
            // because reddit randomizes scores, we want to pass it as a payload to minimize
            // UI updates between refreshes
            return oldItem.copy(pairing = newItem.pairing) == newItem
        }
    }
}
