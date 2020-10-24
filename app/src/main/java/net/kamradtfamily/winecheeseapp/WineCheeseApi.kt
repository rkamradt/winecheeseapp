package net.kamradtfamily.winecheeseapp

import android.util.Log
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface WineCheeseApi {
    @GET("/r/{subreddit}/hot.json")
    suspend fun getTop(
            @Path("id") id: Int,
            @Query("limit") limit: Int,
            @Query("after") after: Int? = null,
            @Query("before") before: Int? = null
    ): ListingResponse

    class ListingResponse(val data: ListingData)

    class ListingData(
            val children: List<WineCheeseChildrenResponse>,
            val after: Int?,
            val before: Int?
    )

    data class WineCheeseChildrenResponse(val data: WineCheesePost)

    companion object {
        private const val BASE_URL = "https://www.reddit.com/"
        fun create(): WineCheeseApi {
            val logger = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { Log.d("API", it) })
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .build()
            return Retrofit.Builder()
                    .baseUrl(HttpUrl.parse(BASE_URL)!!)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(WineCheeseApi::class.java)
        }
    }

}