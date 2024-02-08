package com.musnadil.newsapp.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musnadil.newsapp.data.Repository
import com.musnadil.newsapp.data.api.Resource
import com.musnadil.newsapp.data.model.ResponseSearch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val _searchResult = MutableLiveData<Resource<ResponseSearch>>()
    val searchResult: LiveData<Resource<ResponseSearch>> get() = _searchResult

    fun searchArticle(q: String, apiKey: String) {
        viewModelScope.launch {
            _searchResult.postValue(Resource.loading())
            try {
                _searchResult.postValue(Resource.success(repository.searchArticle(q, apiKey)))
            } catch (e: Exception) {
                _searchResult.postValue(Resource.error(e.localizedMessage ?: "An Error Occurred"))
            }
        }
    }
}