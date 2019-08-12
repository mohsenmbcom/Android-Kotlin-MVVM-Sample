package com.mohsenmb.googlenewsapisample.api

import com.google.gson.Gson
import com.mohsenmb.googlenewsapisample.repository.di.ApiModule
import com.mohsenmb.googlenewsapisample.repository.webservice.NewsApi
import com.mohsenmb.googlenewsapisample.repository.webservice.STATUS_OK
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class NewsApiTest {

    @Inject
    lateinit var newsApi: NewsApi

    @Inject
    lateinit var gson: Gson


    @Before
    fun prepare() {
        DaggerNewsApiComponent
            .builder()
            .apiModule(ApiModule(true))
            .build()
            .inject(this);
    }

    /**
     * Test to see if the api is set
     */
    @Test
    fun newsApi_loadHeadlines_NoError() {
        val subscriber = newsApi.loadTopHeadlines(1)
            .subscribeOn(Schedulers.trampoline())
            .test()

        subscriber.assertValue { STATUS_OK == it.status }
        subscriber.assertComplete()
        subscriber.dispose()
    }
}