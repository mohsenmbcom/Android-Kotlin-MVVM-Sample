package com.mohsenmb.googlenewsapisample.repository.di

import android.content.Context
import com.mohsenmb.googlenewsapisample.repository.persistence.ArticlesDao
import com.mohsenmb.googlenewsapisample.repository.persistence.ArticlesDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideArticlesDao(database: ArticlesDatabase): ArticlesDao =
        database.articlesDao()

    @Provides
    @Singleton
    fun provideArticlesDatabase(): ArticlesDatabase =
        ArticlesDatabase.getDatabase(context)
}