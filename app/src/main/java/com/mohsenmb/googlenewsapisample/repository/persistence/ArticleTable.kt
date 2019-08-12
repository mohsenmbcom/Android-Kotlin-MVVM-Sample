package com.mohsenmb.googlenewsapisample.repository.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity(tableName = "articles")
class Article(
    val source: String?,
    val author: String?,
    val publishDate: Date,
    val title: String,
    val description: String,
    val articleUrl: String,
    val imageUrl: String?,
    val content: String
) {
    @ColumnInfo(name = "_id")
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}