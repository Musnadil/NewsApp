package com.musnadil.newsapp.data.api

class ApiHelper(private val apiServices: ApiServices) {
    suspend fun getSources(category: String, apiKey: String) =
        apiServices.getSources(category, apiKey)

    suspend fun getArticles(source: String, apiKey: String) =
        apiServices.getArticles(source, apiKey)

    suspend fun searchArticle(q: String, apiKey: String, page: Int) =
        apiServices.searchArticle(q, apiKey, page)

}