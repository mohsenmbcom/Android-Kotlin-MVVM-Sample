package com.mohsenmb.googlenewsapisample

import com.mohsenmb.googlenewsapisample.repository.di.ApiModule
import com.mohsenmb.googlenewsapisample.repository.di.RoomModule
import com.mohsenmb.googlenewsapisample.ui.headlines.MainActivityModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ApiModule::class,
        RoomModule::class,
        MainActivityModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<NewsApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: NewsApplication): Builder

        fun roomModule(module: RoomModule): Builder

        fun build(): ApplicationComponent
    }
}