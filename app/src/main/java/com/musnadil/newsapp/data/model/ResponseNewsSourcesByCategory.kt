package com.musnadil.newsapp.data.model

data class ResponseNewsSourcesByCategory(
    val sources: List<Source>,
    val status: String
)