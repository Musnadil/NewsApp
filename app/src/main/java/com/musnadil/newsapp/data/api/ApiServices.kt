package com.musnadil.newsapp.data.api

import com.musnadil.newsapp.data.model.ResponseArticlesFromSource
import com.musnadil.newsapp.data.model.ResponseNewsSourcesByCategory
import com.musnadil.newsapp.data.model.ResponseSearch
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {
    @GET("sources")
    suspend fun getSources(
        @Query("category") category: String,
        @Query("apiKey") apiKey: String
    ) : ResponseNewsSourcesByCategory

    @GET("top-headlines")
    suspend fun getArticles(
        @Query("sources") sources: String,
        @Query("apiKey") apiKey: String
    ) : ResponseArticlesFromSource

    @GET("everything")
    suspend fun searchArticle(
        @Query("q") q: String,
        @Query("apiKey") apiKey: String
    ) : ResponseSearch


}