package com.example.mynewnote.presentation.firsts_creen

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mynewnote.R
import com.example.mynewnote.domain.NoteItem
import com.example.mynewnote.presentation.DeleteDialogFragment
import com.example.mynewnote.presentation.edit_noteitem_screen.NoteItemActivity
import com.example.mynewnote.presentation.note_list_adapter.NoteListAdapter
import com.example.mynewnote.presentation.search_noteitem_screen.SearchNoteItemActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), DeleteDialogFragment.DeleteCallBackToNoteActivity {

    private lateinit var viewModel: MainViewModel
    private lateinit var noteListAdapter: NoteListAdapter
    private lateinit var textViewWelcomeScreen: TextView
    private lateinit var topToolBarFirstScreen: Toolbar
    private var itemForDelete: NoteItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        setupRecyclerView()
        initTopToolBarFirstScreen()
        textViewWelcomeScreen = findViewById(R.id.textViewWelcomeScreen)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.noteList.observe(this) {
            Log.d("MainActivityTest", it.toString())
            noteListAdapter.submitList(it)
            changeMainScreen(it)

        }
        val buttonAddNoteItem = findViewById<FloatingActionButton>(R.id.fabAddNote)
        buttonAddNoteItem.setOnClickListener {
            val intent = NoteItemActivity.newIntentAddItem(this)
            startActivity(intent)
        }

    }

    private fun  initTopToolBarFirstScreen() {
        topToolBarFirstScreen = findViewById(R.id.mainToolbar)
        setSupportActionBar(topToolBarFirstScreen)
        with(topToolBarFirstScreen) {
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_search_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = SearchNoteItemActivity.newIntentSearchItem(this)
        startActivity(intent)
        return super.onOptionsItemSelected(item)
    }

    private fun changeMainScreen( list: List<NoteItem>) {
        if (list.isEmpty()) {
            Log.d("rem",list.size.toString())
            textViewWelcomeScreen.visibility = View.VISIBLE
            supportActionBar?.hide()

        } else {
            Log.d("rem",list.size.toString())
            textViewWelcomeScreen.visibility = View.GONE
            supportActionBar?.show()
        }
    }

    private fun setupRecyclerView() {
        val rvNoteList = findViewById<RecyclerView>(R.id.recyclerviewNotes)
        noteListAdapter = NoteListAdapter()
        rvNoteList.adapter = noteListAdapter
        setupItemShortClickListener()
        setupSwipeListener(rvNoteList)

    }

    private fun setupItemShortClickListener() {
        noteListAdapter.onNoteItemClickListener = {
            val intent = NoteItemActivity.newIntentEditItem(this, it.id)
            startActivity(intent)
        }
    }

    private fun setupSwipeListener(rvNoteList: RecyclerView) {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deleteDialogFragment = DeleteDialogFragment()
                deleteDialogFragment.show(supportFragmentManager,"delete")
                itemForDelete = noteListAdapter.currentList[viewHolder.adapterPosition]
            }

        }

        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvNoteList)
    }

    override fun onDeleteButtonClicked() {
             itemForDelete?.let { viewModel.deleteNoteItem(it) }
    }
}