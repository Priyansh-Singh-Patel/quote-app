package com.example.quoteapp

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SavedQuotesManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("SavedQuotesPrefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveQuote(quote: QuoteModel) {
        val savedList = getSavedQuotes().toMutableList()
        if (!savedList.contains(quote)) {
            savedList.add(quote)
            val json = gson.toJson(savedList)
            prefs.edit().putString("saved_quotes", json).apply()
        }
    }

    fun getSavedQuotes(): List<QuoteModel> {
        val json = prefs.getString("saved_quotes", null)
        return if (json != null) {
            val type = object : TypeToken<List<QuoteModel>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }
}
