package com.mohsenmb.googlenewsapisample.repository.webservice

import com.mohsenmb.googlenewsapisample.BuildConfig
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NewsApi {
    @Headers("X-Api-Key: ${BuildConfig.NEWS_API_KEY}")
    @GET("v2/top-headlines")
    fun loadTopHeadlines(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int = 21,
        @Query("country") country: String = "us"
    ): Single<TopHeadlinesResponse>
}