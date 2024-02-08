package com.musnadil.newsapp.data

import com.musnadil.newsapp.data.api.ApiHelper

class Repository(private val apiHelper: ApiHelper) {
    suspend fun getSources(category: String, apiKey: String) =
        apiHelper.getSources(category, apiKey)

    suspend fun getArticles(source: String, apiKey: String) =
        apiHelper.getArticles(source, apiKey)

    suspend fun searchArticle(q: String, apiKey: String, page: Int) =
        apiHelper.searchArticle(q, apiKey, page)
}