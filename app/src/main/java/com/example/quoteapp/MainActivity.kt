package com.example.quoteapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.example.quoteapp.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: QuoteViewModel by viewModels()
    private var refreshCount = 0
    private var resetScheduled = false
    private lateinit var savedQuotesManager: SavedQuotesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.quoteText.observe(this, Observer { quote ->
            binding.quoteText.text = quote
        })

        viewModel.authorName.observe(this, Observer { author ->
            binding.authorName.text = "- $author"
        })


        viewModel.fetchQuote()


        binding.refreshButton.setOnClickListener {
            refreshCount++
            if (refreshCount > 5) {
                Toast.makeText(
                    this,
                    "Please wait a few seconds before refreshing again",
                    Toast.LENGTH_SHORT
                ).show()
            }
            viewModel.fetchQuote()

            if (!resetScheduled) {
                resetScheduled = true
                Handler(Looper.getMainLooper()).postDelayed({
                    refreshCount = 0
                    resetScheduled = false
                },60000)
            }
        }

        savedQuotesManager = SavedQuotesManager(this)
        binding.saveButton.setOnClickListener {
            val quote = viewModel.quoteText.value
            val author = viewModel.authorName.value?.removePrefix("- ")

            if (!quote.isNullOrBlank() && !author.isNullOrBlank()) {
                val quoteModel = QuoteModel(q = quote, a = author)
                savedQuotesManager.saveQuote(quoteModel)
                Toast.makeText(this, "Quote saved!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No quote to save", Toast.LENGTH_SHORT).show()
            }
        }

        binding.viewSavedButton.setOnClickListener {
            startActivity(Intent(this, SavedQuotesActivity::class.java))
        }

        binding.shareButton.setOnClickListener {
            val quote = binding.quoteText.text.toString()
            val author = binding.authorName.text.toString()
            shareQuoteAsImage(quote, author)
        }
    }

    private fun shareQuoteAsImage(quote: String, author: String) {

        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.share_layout, null)

        val quoteTextView = view.findViewById<TextView>(R.id.sharequoteText)
        val authorTextView = view.findViewById<TextView>(R.id.shareauthorName)
        quoteTextView.text = quote
        authorTextView.text = "- $author"

        view.measure(
            View.MeasureSpec.makeMeasureSpec(1080, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)

        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        val imageFile = File(cacheDir, "images")
        imageFile.mkdirs()
        val file = File(imageFile, "quote_${System.currentTimeMillis()}.png")
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()


        val uri = FileProvider.getUriForFile(this, "$packageName.provider", file)


        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        startActivity(Intent.createChooser(shareIntent, "Share Quote via"))
    }

}
