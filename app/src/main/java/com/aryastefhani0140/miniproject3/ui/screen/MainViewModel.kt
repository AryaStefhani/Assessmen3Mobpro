package com.aryastefhani0140.miniproject3.ui.screen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryastefhani0140.miniproject3.model.BookReview
import com.aryastefhani0140.miniproject3.network.BukuApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

class MainViewModel : ViewModel() {

    var data = mutableStateOf(emptyList<BookReview>())
        private set
    var status = MutableStateFlow(BukuApi.ApiStatus.LOADING)
        private set
    var errorMessage = mutableStateOf<String?>(null)
        private set

    init {
        retrieveData()
    }

    fun retrieveData() {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = BukuApi.ApiStatus.LOADING
            try {
                data.value = BukuApi.service.getBookReview("")
                status.value = BukuApi.ApiStatus.SUCCESS
                Log.d("MainViewModel", "Success: Retrieved ${data.value.size} book reviews")
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                status.value = BukuApi.ApiStatus.FAILED
            }
        }
    }

    fun saveData(userId: String, judulBuku: String, isiReview: String, rating: Float, bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = BukuApi.service.postBookReview(
                    userId,
                    judulBuku.toRequestBody("text/plain".toMediaTypeOrNull()),
                    isiReview.toRequestBody("text/plain".toMediaTypeOrNull()),
                    rating.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    bitmap.toMultipartBody()
                )

                if (result.status == "success")
                    retrieveData()
                else
                    throw Exception(result.message)

            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    private fun Bitmap.toMultipartBody(): MultipartBody.Part {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()
        val requestBody = byteArray.toRequestBody(
            "image/jpg".toMediaTypeOrNull(), 0, byteArray.size)
        return MultipartBody.Part.createFormData(
            "image", "image.jpg", requestBody)
    }

    fun clearMessage() { errorMessage.value = null }
}