package com.musnadil.newsapp.ui.newssources

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musnadil.newsapp.data.Repository
import com.musnadil.newsapp.data.api.Resource
import com.musnadil.newsapp.data.model.ResponseNewsSourcesByCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsSourcesViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val _newsSource = MutableLiveData<Resource<ResponseNewsSourcesByCategory>>()
    val newsSource: LiveData<Resource<ResponseNewsSourcesByCategory>> get() = _newsSource

    fun getSource(category: String, apiKey: String) {
        viewModelScope.launch {
            _newsSource.postValue(Resource.loading())
            try {
                _newsSource.postValue(Resource.success(repository.getSources(category, apiKey)))
            } catch (e: Exception) {
                _newsSource.postValue(Resource.error(e.localizedMessage ?: "An Error Occurred"))

            }
        }
    }
}