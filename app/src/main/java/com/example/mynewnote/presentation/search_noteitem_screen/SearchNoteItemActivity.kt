package com.example.mynewnote.presentation.search_noteitem_screen

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import com.example.mynewnote.R
import com.example.mynewnote.databinding.ActivitySearchBinding
import com.example.mynewnote.domain.NoteItem
import com.example.mynewnote.presentation.edit_noteitem_screen.NoteItemActivity
import com.example.mynewnote.presentation.note_list_adapter.NoteListAdapter

class SearchNoteItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel
    private lateinit var adapter: NoteListAdapter
    private lateinit var searchView: SearchView

    private var textFromSearchView: String? = null
    private var textFromBundleToSearchView: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setColorStatusBar()
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root

        if (savedInstanceState != null) {
            textFromBundleToSearchView = savedInstanceState.getString("TextFromSearchView")
        }
        setContentView(view)
        setupRecyclerView()
        initTopToolBarFirstScreen()

        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        viewModel.ldListNotes.observe(this) {
            adapter.submitList(it)
            changeSearchScreenPictures(it)
        }
    }

    private fun setupRecyclerView() {
        adapter = NoteListAdapter()
        binding.recyclerviewSearchNotes.adapter = adapter
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

        private const val SEARCH = "Search"
    }

    private fun initTopToolBarFirstScreen() {
        setSupportActionBar(binding.searchToolbar)
        with(binding.searchToolbar) {
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
            searchView.setQuery(textFromBundleToSearchView, false)
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
            queryHint = SEARCH
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
        binding.searchTextView.visibility = View.VISIBLE
        binding.searchTextView.setText(R.string.for_search_enter_text)
    }

    private fun changeSearchScreenPictures(list: List<NoteItem>) {
        if (list.isEmpty()) {
            binding.searchTextView.visibility = View.VISIBLE
            binding.searchTextView.setText(R.string.search_nothing)
        } else {
            binding.searchTextView.visibility = View.GONE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        textFromSearchView = searchView.query.toString()
        outState.putString("TextFromSearchView", textFromSearchView)
        super.onSaveInstanceState(outState)
    }

    private fun setColorStatusBar() {
        val window = this.window
        window.statusBarColor = this.resources.getColor(R.color.black, theme.resources.newTheme())
    }


    override fun onStart() {
        textFromBundleToSearchView?.let {
            viewModel.searchNotes(textFromBundleToSearchView.toString())
        }
        super.onStart()
    }
}