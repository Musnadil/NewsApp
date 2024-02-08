package com.musnadil.newsapp.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.musnadil.newsapp.data.Repository
import com.musnadil.newsapp.paging.NewsPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

data class QueryData(
    val q: String,
    val apiKey: String
)

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    //    private val _searchResult = MutableLiveData<Resource<ResponseSearch>>()
//    val searchResult: LiveData<Resource<ResponseSearch>> get() = _searchResult
//
//    fun searchArticle(q: String, apiKey: String) {
//        viewModelScope.launch {
//            _searchResult.postValue(Resource.loading())
//            try {
//                _searchResult.postValue(Resource.success(repository.searchArticle(q, apiKey)))
//            } catch (e: Exception) {
//                _searchResult.postValue(Resource.error(e.localizedMessage ?: "An Error Occurred"))
//            }
//        }
//    }

    var currentQuery = MutableStateFlow<QueryData?>(null)

    val loading = MutableLiveData<Boolean>()
    fun search(q: String, key: String) {
        currentQuery.value = QueryData(q, key)
    }

    val newsList = currentQuery
        .filter { it != null }
        .flatMapLatest { queryData ->
            Pager(PagingConfig(1)) {
                NewsPagingSource(repository, queryData!!.q, queryData!!.apiKey)
            }.flow.cachedIn(viewModelScope)
        }
}