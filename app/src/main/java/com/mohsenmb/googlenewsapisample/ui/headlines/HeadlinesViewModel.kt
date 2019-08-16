package com.mohsenmb.googlenewsapisample.ui.headlines

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.mohsenmb.googlenewsapisample.repository.persistence.ArticlesDao
import com.mohsenmb.googlenewsapisample.repository.persistence.PersistedArticle
import com.mohsenmb.googlenewsapisample.repository.persistence.toPersistedArticle
import com.mohsenmb.googlenewsapisample.repository.webservice.*
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

private const val PAGE_SIZE = 21

/**
 * Used the following articles to implement
 *      https://developer.android.com/topic/libraries/architecture/viewmodel
 */
class HeadlinesViewModel @Inject constructor(
    val newsApi: NewsApi,
    val dao: ArticlesDao,
    val gson: Gson
) : ViewModel() {

    private var fromDate = Date()
    private var page = 1
    private var goOffline = false

    private val articles: MutableLiveData<List<PersistedArticle>> by lazy { MutableLiveData<List<PersistedArticle>>() }
    private val errors: MutableLiveData<Error> by lazy { MutableLiveData<Error>() }

    private val disposables = CompositeDisposable()


    fun getArticlesLiveData() = articles
    fun getErrorsLiveData() = errors

    fun firstPage() {
        page = 1
        fromDate = Date()
        loadArticles(false)
    }

    fun nextPage() {
        loadArticles(true)
    }


    private fun loadArticles(append: Boolean) {
        if (goOffline) {
            dao.fetchNews(fromDate, PAGE_SIZE)
                .map { articles ->
                    if (articles.isEmpty()) {
                        throw UnsupportedOperationException(NO_MORE_DATA)
                    }
                    return@map articles
                }
                .observe(true, append)
        } else {
            newsApi.loadTopHeadlines(page, PAGE_SIZE)
                .map { response ->
                    response.articles.let { responseArticles ->
                        page++
                        if (responseArticles.size < PAGE_SIZE) {
                            goOffline = true
                        }
                        if (responseArticles.isEmpty()) {
                            throw UnsupportedOperationException(NO_MORE_ONLINE)
                        }
                        responseArticles.map { article ->
                            article.toPersistedArticle()
                        }
                    }
                }.observe(false, append)
        }

    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }

    private fun Single<List<PersistedArticle>>.observe(offlineDate: Boolean, append: Boolean) {
        disposables.add(
            subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    errors.value = noError()

                    fun publishData() {
                        val outArticles = when (append) {
                            true -> {
                                val existing = articles.value ?: emptyList()
                                (existing + response)
                            }
                            false -> {
                                response
                            }
                        }.apply { sortedByDescending { it.publishDate } }
                        articles.value = outArticles
                        fromDate = outArticles.last().publishDate
                    }

                    if (!offlineDate) {
                        dao.insertAll(response).observe {
                            publishData()
                        }
                    } else {
                        publishData()
                    }
                }, parseError(gson) { error ->
                    errors.value = error
                    if (error.type == ErrorType.NetworkOffline ||
                        error.type == ErrorType.NoMoreOnlineData
                    ) {
                        goOffline = true
                        loadArticles(append)
                    }
                })
        )
    }

    private fun Completable.observe(onComplete: () -> Unit = {}) {
        disposables.add(
            subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onComplete)
        )
    }
}