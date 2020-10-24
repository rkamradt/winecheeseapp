package net.kamradtfamily.winecheeseapp

import androidx.paging.Pager
import androidx.paging.PagingConfig

class DbWineCheesePostRepository(val db: WineCheeseDb, val wineCheeseApi: WineCheeseApi) :
    WineCheesePostRepository {
    override fun postsOfWineCheese(id: Int, pageSize: Int) = Pager(
        config = PagingConfig(pageSize),
        remoteMediator = PageKeyedRemoteMediator(db, wineCheeseApi, id)
    ) {
        db.posts().postsByWineCheese(id)
    }.flow

}
