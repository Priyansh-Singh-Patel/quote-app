package com.example.quoteapp

import retrofit2.Response
import retrofit2.http.GET

interface QuoteApiService {
    @GET("random")
    suspend fun getRandomQuote(): Response<List<QuoteModel>>
}
