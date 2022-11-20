package com.example.mynewnote.presentation.search_noteitem_screen

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.mynewnote.R

class SearchNoteItemActivity : AppCompatActivity() {
    private lateinit var searchToolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initTopToolBarFirstScreen()
    }

    companion object {
        fun newIntentSearchItem(context: Context): Intent {
            val intent = Intent(context, SearchNoteItemActivity::class.java)
            return intent
        }
    }

    private fun  initTopToolBarFirstScreen() {
        searchToolbar = findViewById(R.id.searchToolbar)
        setSupportActionBar(searchToolbar)
        with(searchToolbar) {
            setNavigationIcon(R.drawable.ic_baseline_arrow_white_24)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            setNavigationOnClickListener {
                finish()
            }
        }
    }
}