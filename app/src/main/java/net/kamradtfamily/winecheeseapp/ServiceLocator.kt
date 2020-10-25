package net.kamradtfamily.winecheeseapp

import android.app.Application
import android.content.Context

interface ServiceLocator {
    companion object {
        private val LOCK = Any()
        private var instance: ServiceLocator? = null
        fun instance(context: Context): ServiceLocator {
            synchronized(LOCK) {
                if (instance == null) {
                    instance = DefaultServiceLocator(
                        app = context.applicationContext as Application,
                        useInMemoryDb = false)
                }
                return instance!!
            }
        }
    }

    fun getRepository(): WineCheesePostRepository

    fun getWineCheeseApi(): WineCheeseApi
}

/**
 * default implementation of ServiceLocator that uses production endpoints.
 */
open class DefaultServiceLocator(val app: Application, val useInMemoryDb: Boolean) : ServiceLocator {
    private val db by lazy {
        WineCheeseDb.create(app, useInMemoryDb)
    }

    private val api by lazy {
        WineCheeseApi.create()
    }

    override fun getRepository(): WineCheesePostRepository {
        return InMemoryByItemRepository(
                wineCheeseApi = getWineCheeseApi())
    }

    override fun getWineCheeseApi(): WineCheeseApi = api

}
