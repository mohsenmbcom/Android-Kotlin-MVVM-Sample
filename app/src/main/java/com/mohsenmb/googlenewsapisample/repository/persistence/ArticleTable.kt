package com.mohsenmb.googlenewsapisample.repository.persistence

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mohsenmb.googlenewsapisample.repository.webservice.Article
import java.util.*

@Entity(tableName = "articles")
data class PersistedArticle(
	val source: String?,
	val author: String?,
	val publishDate: Date,
	val title: String,
	val description: String?,
	@PrimaryKey
	val articleUrl: String,
	val imageUrl: String?,
	val content: String?
)

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