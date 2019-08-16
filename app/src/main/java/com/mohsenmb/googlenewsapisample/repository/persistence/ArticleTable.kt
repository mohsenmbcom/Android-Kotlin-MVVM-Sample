package com.mohsenmb.googlenewsapisample.repository.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mohsenmb.googlenewsapisample.repository.webservice.Article
import java.util.*

@Entity(tableName = "articles")
class PersistedArticle(
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

fun Article.toPersistedArticle(): PersistedArticle =
    PersistedArticle(
        articleSource.name,
        author,
        publishDate ?: Date(0),
        title,
        description,
        articleUrl,
        imageUrl,
        content
    )