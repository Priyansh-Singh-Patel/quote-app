package com.example.quoteapp

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class QuoteViewModel : ViewModel() {

    private val repository = QuoteRepository()

    val quoteText = MutableLiveData<String>()
    val authorName = MutableLiveData<String>()

    fun fetchQuote() {
        viewModelScope.launch {
            try {
                val quote = repository.getQuote()
                quote?.let {
                    quoteText.value = it.q
                    authorName.value = it.a
                } ?: run {
                    Log.e("QuoteViewModel", "Quote is null")
                }
            } catch (e: Exception) {
                Log.e("QuoteViewModel", "Error fetching quote", e)
            }
        }
    }

}
