package com.mohsenmb.googlenewsapisample.repository.webservice

import com.google.gson.annotations.SerializedName
import com.mohsenmb.googlenewsapisample.repository.toLocalDate
import java.util.*

const val STATUS_OK = "ok"
const val STATUS_ERROR = "error"

data class TopHeadlinesResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("totalResults")
    val totalResults: Int,

    @SerializedName("articles")
    val articles: List<Article>
)

data class Article(
    @SerializedName("source")
    val articleSource: Source,

    @SerializedName("author")
    val author: String?,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("url")
    val articleUrl: String,

    @SerializedName("urlToImage")
    val imageUrl: String?,

    @SerializedName("publishedAt")
    val publishedAt: String,

    @SerializedName("content")
    val content: String?
) {
    val publishDate: Date?
        get() = publishedAt.toLocalDate()
}

data class Source(
    @SerializedName("name")
    val name: String?
)
