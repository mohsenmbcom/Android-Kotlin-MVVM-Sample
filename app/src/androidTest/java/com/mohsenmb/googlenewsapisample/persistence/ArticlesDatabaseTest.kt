package com.mohsenmb.googlenewsapisample.persistence

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mohsenmb.googlenewsapisample.repository.persistence.ArticlesDao
import com.mohsenmb.googlenewsapisample.repository.persistence.ArticlesDatabase
import com.mohsenmb.googlenewsapisample.repository.persistence.PersistedArticle
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*

/**
 * To implement this test used the following article
 *      https://developer.android.com/training/data-storage/room/testing-db
 */
@RunWith(AndroidJUnit4::class)
class SimpleEntityReadWriteTest {
    private lateinit var articlesDao: ArticlesDao
    private lateinit var db: ArticlesDatabase

    private lateinit var testArticle: PersistedArticle

    @Before
    fun prepare() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room
            .inMemoryDatabaseBuilder(context, ArticlesDatabase::class.java)
            .build()
        articlesDao = db.articlesDao()

        testArticle = PersistedArticle(
            "Test Source",
            "Test Author",
            Date(),
            "Test Title",
            "Test description",
            "Test url",
            "Test Image",
            "Test Content"
        )
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    /**
     * Since the order of multiple tests is not guaranteed,
     *  and it's important to me to test the side effects of the operations,
     *  I put all the tests in a single test function with my expected order
     */
    @Test
    fun articlesDatabase_functionality_isEverythingSet() {
        articlesDatabase_insertArticle_isWorking()
        articlesDatabase_getAllArticles_checkIfEmptyOrNot(false)
        articlesDatabase_clearAllArticles_isWorking()
        articlesDatabase_getAllArticles_checkIfEmptyOrNot(true)
    }

    private fun articlesDatabase_clearAllArticles_isWorking() {
        val subscriber = articlesDao.clearAllNews()
            .subscribeOn(Schedulers.trampoline())
            .test()

        subscriber.assertComplete()
        subscriber.dispose()
    }

    private fun articlesDatabase_insertArticle_isWorking() {
        val subscriber = articlesDao.insert(testArticle)
            .subscribeOn(Schedulers.trampoline())
            .test()

        subscriber.assertComplete()
        subscriber.dispose()
    }

    private fun articlesDatabase_getAllArticles_checkIfEmptyOrNot(shouldBeEmpty: Boolean) {
        val subscriber = articlesDao.fetchNews(Date(), 100)
            .subscribeOn(Schedulers.trampoline())
            .test()

        subscriber.assertValue {
            if (shouldBeEmpty) {
                it.isEmpty()
            } else {
                it.any { article ->
                    testArticle.title == article.title
                }
            }
        }
        subscriber.dispose()
    }
}