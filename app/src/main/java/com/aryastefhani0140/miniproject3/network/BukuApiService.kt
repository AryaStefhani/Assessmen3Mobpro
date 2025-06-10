package com.aryastefhani0140.miniproject3.network

import com.aryastefhani0140.miniproject3.model.BookReview
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://store.sthresearch.site/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface BookReviewApiService {
    @GET("book_review.php")
    suspend fun getBookReviews(): List<BookReview>
}

object BookApiService {
    val service: BookReviewApiService by lazy {
        retrofit.create(BookReviewApiService::class.java)
    }

    fun getBookImageUrl(imageId: String): String {
        return "${BASE_URL}images/$imageId.jpg"
    }
}