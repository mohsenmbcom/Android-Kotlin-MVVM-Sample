package com.mohsenmb.googlenewsapisample.repository.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Used these article to make sure about the implementation
 *      https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin/index.html
 *      https://medium.com/androiddevelopers/room-rxjava-acb0cd4f3757
 */

@Dao
interface ArticlesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(article: Article): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(users: List<Article>): Completable

    @Query("DELETE FROM articles")
    fun clearAllNews(): Completable

    @Query("SELECT * FROM articles ORDER BY publishDate DESC")
    fun getAllNews(): Single<List<Article>>
}