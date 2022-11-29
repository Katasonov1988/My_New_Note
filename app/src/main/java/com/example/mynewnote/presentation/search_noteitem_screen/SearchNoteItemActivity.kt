package com.example.mynewnote.presentation.search_noteitem_screen

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.mynewnote.R
import com.example.mynewnote.domain.NoteItem
import com.example.mynewnote.presentation.edit_noteitem_screen.NoteItemActivity
import com.example.mynewnote.presentation.note_list_adapter.NoteListAdapter

class SearchNoteItemActivity : AppCompatActivity() {
    private lateinit var searchToolbar: Toolbar
    private lateinit var searchView: SearchView
    private lateinit var searchTextView: TextView
    private lateinit var viewModel: SearchViewModel
    private var textFromSearchView: String? = null
    private lateinit var adapter: NoteListAdapter
    private var textFromBundleToSearchView: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setColorStatusBar()
        Log.d("SearchActivity", "onCreate")
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
           textFromBundleToSearchView = savedInstanceState.getString("TextFromSearchView")
        }
        setContentView(R.layout.activity_search)
        setupRecyclerView()
        initTopToolBarFirstScreen()

        searchTextView = findViewById(R.id.searchTextView)

        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        viewModel.ldListNotes.observe(this) {
            adapter.submitList(it)
            changeSearchScreenPictures(it)
        }
    }

    private fun setupRecyclerView() {
        val rvSearchList = findViewById<RecyclerView>(R.id.recyclerviewSearchNotes)
        adapter = NoteListAdapter()
        rvSearchList.adapter = adapter
        setupItemShortClickListener()
    }

    private fun setupItemShortClickListener() {
        adapter.onNoteItemClickListener = {
            val intent = NoteItemActivity.newIntentEditItem(this, it.id)
            startActivity(intent)
        }
    }

    companion object {
        fun newIntentSearchItem(context: Context): Intent {
            val intent = Intent(context, SearchNoteItemActivity::class.java)
            return intent
        }
        private const val search = "Search"
    }

    private fun initTopToolBarFirstScreen() {
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)

        val searchItem: MenuItem = menu?.findItem(R.id.search_button)
            ?: return super.onCreateOptionsMenu(menu)

        searchItem.expandActionView()
        val searchView: SearchView = searchItem.actionView as SearchView
        this.searchView = searchView
        textFromBundleToSearchView?.let {
            searchView.setQuery(textFromBundleToSearchView,false)
        }


        searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)?.apply {
            setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
            setOnClickListener {
                if (searchView.query.isEmpty()) {
                    finish()
                } else {
                    searchView.setQuery("", false)
                    emptyQueryState()
                }
            }
        }

        with(searchView) {
            maxWidth = Int.MAX_VALUE
            queryHint = search
            isFocusable = true
            isIconified = false
            requestFocusFromTouch()
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    if (newText.isEmpty()) {
                        emptyQueryState()
                    } else {
                        viewModel.searchNotes(newText)
                    }
                    return false
                }
            })
        }
        return true
    }

    private fun emptyQueryState() {
        viewModel.setZeroValueToList()
        searchTextView.visibility = View.VISIBLE
        searchTextView.setText(R.string.for_search_enter_text)
    }

    private fun changeSearchScreenPictures(list: List<NoteItem>) {
        if (list.isEmpty()) {
            Log.d("rem", list.size.toString())
            searchTextView.visibility = View.VISIBLE
            searchTextView.setText(R.string.search_nothing)
        } else {
            Log.d("rem", list.size.toString())
            searchTextView.visibility = View.GONE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d("SearchActivity", "onSaveInstanceState")
        textFromSearchView = searchView.query.toString()
        outState.putString("TextFromSearchView", textFromSearchView)
        super.onSaveInstanceState(outState)
    }


    override fun onStart() {
        Log.d("SearchActivity", "onStart")
        textFromBundleToSearchView?.let {
            viewModel.searchNotes(textFromBundleToSearchView.toString())
        }
        super.onStart()
    }

    override fun onResume() {
        Log.d("SearchActivity", "onResume")
        super.onResume()
    }

    override fun onPause() {
        Log.d("SearchActivity", "onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.d("SearchActivity", "onStop")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d("SearchActivity", "onDestroy")
        super.onDestroy()
    }

    override fun onRestart() {
//        textFromBundleToSearchView?.let {
//            viewModel.searchNotes(textFromBundleToSearchView.toString())
//        }

        super.onRestart()
    }

    private fun setColorStatusBar() {
        val window = this.window
        window.statusBarColor = this.resources.getColor(R.color.black, theme.resources.newTheme())
    }

}