package com.musnadil.newsapp.data.model

data class ArticleX(
    val source: SourceXX,
    val author: String?,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String
)