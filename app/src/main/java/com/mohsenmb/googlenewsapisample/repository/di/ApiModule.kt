package com.mohsenmb.googlenewsapisample.repository.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mohsenmb.googlenewsapisample.BuildConfig
import com.mohsenmb.googlenewsapisample.repository.webservice.NewsApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class ApiModule(val disableLog: Boolean = false) {

    @Provides
    @Singleton
    fun provideNewsApi(
        client: OkHttpClient,
        rxCallingAdapter: RxJava2CallAdapterFactory,
        gsonConverter: GsonConverterFactory
    ): NewsApi =
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(client)
            .addCallAdapterFactory(rxCallingAdapter)
            .addConverterFactory(gsonConverter)
            .build().create(NewsApi::class.java)

    @Provides
    @Singleton
    fun provideHttpClient(logInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logInterceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            if (!disableLog) {
                level =
                    if (BuildConfig.DEBUG)
                        HttpLoggingInterceptor.Level.BODY
                    else
                        HttpLoggingInterceptor.Level.NONE
            }
        }

    @Provides
    @Singleton
    fun provideRxJavaCallingAdapter(): RxJava2CallAdapterFactory =
        RxJava2CallAdapterFactory.create()


    @Provides
    @Singleton
    fun provideGsonConverter(gson: Gson): GsonConverterFactory =
        GsonConverterFactory.create(gson)

    @Provides
    @Singleton
    fun provideGson(): Gson =
        GsonBuilder()
            .apply {
                if (BuildConfig.DEBUG) setPrettyPrinting()
            }
            .create()
}