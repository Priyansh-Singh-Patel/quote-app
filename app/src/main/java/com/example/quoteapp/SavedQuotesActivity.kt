package com.example.quoteapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quoteapp.databinding.ActivitySavedQuotesBinding

class SavedQuotesActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySavedQuotesBinding
    private lateinit var adapter: SavedQuotesAdapter
    private lateinit var savedQuotesManager: SavedQuotesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedQuotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        savedQuotesManager = SavedQuotesManager(this)
        val savedQuotes = savedQuotesManager.getSavedQuotes()

        adapter = SavedQuotesAdapter(savedQuotes)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }
}
