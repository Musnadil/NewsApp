package com.musnadil.newsapp.data.model

data class ResponseSearch(
    val status: String,
    val totalResults: Int,
    val articles: List<ArticleX>
)