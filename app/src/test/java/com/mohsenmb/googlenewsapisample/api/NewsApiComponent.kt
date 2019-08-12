package com.mohsenmb.googlenewsapisample.api

import com.mohsenmb.googlenewsapisample.repository.di.ApiModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class])
interface NewsApiComponent {

    fun inject(testClass: NewsApiTest)
}