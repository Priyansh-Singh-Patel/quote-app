package com.example.quoteapp

import android.util.Log

class QuoteRepository {
    suspend fun getQuote(): QuoteModel? {
        return try {
            val response = RetrofitInstance.api.getRandomQuote()
            if (response.isSuccessful) {
                response.body()?.firstOrNull()
            } else {
                Log.e("QuoteRepository", "Failed: ${response.code()} ${response.message()}")
                null
            }
        } catch (e: Exception) {
            Log.e("QuoteRepository", "Exception: ${e.message}")
            null
        }
    }
}



