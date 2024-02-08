package com.musnadil.newsapp.data

import com.musnadil.newsapp.data.api.ApiHelper
import com.musnadil.newsapp.data.api.ApiServices
import com.musnadil.newsapp.data.model.ResponseArticlesFromSource
import com.musnadil.newsapp.data.model.ResponseNewsSourcesByCategory
import com.musnadil.newsapp.data.model.ResponseSearch
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class RepositoryTest {
    private lateinit var apiHelper: ApiHelper
    private lateinit var apiServices: ApiServices

    private lateinit var repository: Repository

    @Before
    fun setup(){
        apiServices = mockk()
        apiHelper = ApiHelper(apiServices)
        repository = Repository(apiHelper)
    }


    @Test
    fun getSources(): Unit = runBlocking {
        val responseNewsSources = mockk<ResponseNewsSourcesByCategory>()
        every {
            runBlocking {
                repository.getSources("","")
            }
        } returns responseNewsSources

        repository.getSources("","")

        verify {
            runBlocking {
                repository.getSources("","")
            }
        }
    }

    @Test
    fun getArticles(): Unit = runBlocking {
        val responseNewsArticles = mockk<ResponseArticlesFromSource>()
        every {
            runBlocking {
                repository.getArticles("","")
            }
        } returns responseNewsArticles

        repository.getArticles("","")

        verify {
            runBlocking {
                repository.getArticles("","")
            }
        }
    }

    @Test
    fun searchArticle() : Unit = runBlocking {
        val responseSearch = mockk<ResponseSearch>()
        every {
            runBlocking {
                repository.searchArticle("","")
            }
        } returns responseSearch

        repository.searchArticle("","")

        verify {
            runBlocking {
                repository.searchArticle("","")
            }
        }
    }
}