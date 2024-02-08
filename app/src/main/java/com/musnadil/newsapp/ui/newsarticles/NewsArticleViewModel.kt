package com.musnadil.newsapp.ui.newsarticles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musnadil.newsapp.data.Repository
import com.musnadil.newsapp.data.api.Resource
import com.musnadil.newsapp.data.model.ResponseArticlesFromSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsArticleViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val _article = MutableLiveData<Resource<ResponseArticlesFromSource>>()
    val article: LiveData<Resource<ResponseArticlesFromSource>> get() = _article

    fun getArticle(source: String, apiKey: String) {
        viewModelScope.launch {
            _article.postValue(Resource.loading())
            try {
                _article.postValue(Resource.success(repository.getArticles(source, apiKey)))
            } catch (e: Exception) {
                _article.postValue(Resource.error(e.localizedMessage ?: "An Error Occurred"))
            }
        }
    }
}