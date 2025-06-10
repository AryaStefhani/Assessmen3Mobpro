package com.aryastefhani0140.miniproject3.ui.screen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryastefhani0140.miniproject3.model.BookReview
import com.aryastefhani0140.miniproject3.network.BookApiService

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    var data = mutableStateOf(emptyList<BookReview>())
        private set
    var status = MutableStateFlow(BookApiService.ApiStatus.LOADING)
        private set

    init {
        retrieveData()
    }

     fun retrieveData() {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = BookApiService.ApiStatus.LOADING
            try {
                data.value = BookApiService.service.getBookReviews()
                status.value = BookApiService.ApiStatus.SUCCESS
                Log.d("MainViewModel", "Success: Retrieved ${data.value.size} book reviews")
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                status.value = BookApiService.ApiStatus.FAILED
            }
        }
    }
}