package com.example.quoteapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quoteapp.databinding.ItemSavedQuoteBinding

class SavedQuotesAdapter(private val quotes: List<QuoteModel>) :
    RecyclerView.Adapter<SavedQuotesAdapter.QuoteViewHolder>() {

    inner class QuoteViewHolder(val binding: ItemSavedQuoteBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val binding = ItemSavedQuoteBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return QuoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val quote = quotes[position]
        holder.binding.quoteText.text = quote.q
        holder.binding.authorName.text = "- ${quote.a}"
    }

    override fun getItemCount(): Int = quotes.size
}
