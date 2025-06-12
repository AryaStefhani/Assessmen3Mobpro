package com.aryastefhani0140.miniproject3.ui.screen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryastefhani0140.miniproject3.model.BookReview
import com.aryastefhani0140.miniproject3.network.BukuApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    var data = mutableStateOf(emptyList<BookReview>())
        private set
    var status = MutableStateFlow(BukuApi.ApiStatus.LOADING)
        private set

    init {
        retrieveData()
    }

    fun retrieveData() {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = BukuApi.ApiStatus.LOADING
            try {
                // Untuk sementara menggunakan userId kosong, nanti bisa disesuaikan dengan user yang login
                data.value = BukuApi.service.getBookReview("")
                status.value = BukuApi.ApiStatus.SUCCESS
                Log.d("MainViewModel", "Success: Retrieved ${data.value.size} book reviews")
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                status.value = BukuApi.ApiStatus.FAILED
            }
        }
    }
}