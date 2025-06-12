package com.aryastefhani0140.miniproject3.network

import com.aryastefhani0140.miniproject3.model.BookReview
import com.aryastefhani0140.miniproject3.model.OpStatus
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

private const val BASE_URL = "https://store.sthresearch.site/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface BukuApiService {
    @GET("book_review.php")
    suspend fun getBookReview(
        @Header("Authorization") userId: String
    ): List<BookReview>

    @Multipart
    @POST("book_review.php")
    suspend fun postBookReview(
        @Header("Authorization") userId: String,
        @Part("judul_buku") judulBuku: RequestBody,
        @Part("isi_review") isiReview: RequestBody,
        @Part("rating") rating: RequestBody,
        @Part image: MultipartBody.Part
    ): OpStatus
}

object BukuApi {
    val service: BukuApiService by lazy {
        retrofit.create(BukuApiService::class.java)
    }

    fun getBookReviewUrl(imageId: String): String {
        return "${BASE_URL}image.php?id=$imageId"
    }

    enum class ApiStatus { LOADING, SUCCESS, FAILED }
}