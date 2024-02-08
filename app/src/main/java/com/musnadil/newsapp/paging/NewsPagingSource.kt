package com.musnadil.newsapp.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.musnadil.newsapp.data.Repository
import com.musnadil.newsapp.data.model.ArticleX
import retrofit2.HttpException

class NewsPagingSource(
    private val repository: Repository,
    private val q: String,
    private val apiKey: String,
    ) : PagingSource<Int, ArticleX>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleX> {
        return try {
            val currentPage = params.key ?: 1
            val response = repository.searchArticle(q, apiKey, currentPage)
            val data = response.body()!!.articles
            val responseData = mutableListOf<ArticleX>()
            responseData.addAll(data)

            LoadResult.Page(
                data = responseData,
                prevKey = if (currentPage == 1) null else -1,
                nextKey = currentPage.plus(1)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        } catch (http: HttpException) {
            LoadResult.Error(http)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ArticleX>): Int? {
        return null
    }
}