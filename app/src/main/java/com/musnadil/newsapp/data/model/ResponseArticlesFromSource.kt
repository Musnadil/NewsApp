package com.musnadil.newsapp.data.model

data class ResponseArticlesFromSource(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)