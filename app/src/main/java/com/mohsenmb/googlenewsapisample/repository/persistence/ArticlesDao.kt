package com.mohsenmb.googlenewsapisample.repository.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single
import java.util.*

/**
 * Used these article to make sure about the implementation
 *      https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin/index.html
 *      https://medium.com/androiddevelopers/room-rxjava-acb0cd4f3757
 */

@Dao
interface ArticlesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(article: PersistedArticle): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(users: List<PersistedArticle>): Completable

    @Query("DELETE FROM articles")
    fun clearAllNews(): Completable

    @Query("""SELECT * FROM articles 
        WHERE publishDate < :fromDate 
        ORDER BY publishDate DESC 
        LIMIT :pageSize""")
    fun fetchNews(fromDate: Date, pageSize: Int): Single<List<PersistedArticle>>
}