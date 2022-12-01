package com.example.mynewnote.presentation.firsts_creen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mynewnote.R
import com.example.mynewnote.databinding.ActivityMainBinding
import com.example.mynewnote.domain.NoteItem
import com.example.mynewnote.presentation.DeleteDialogFragment
import com.example.mynewnote.presentation.edit_noteitem_screen.NoteItemActivity
import com.example.mynewnote.presentation.note_list_adapter.NoteListAdapter
import com.example.mynewnote.presentation.search_noteitem_screen.SearchNoteItemActivity

class MainActivity : AppCompatActivity(),
    DeleteDialogFragment.DeleteCallBackToNoteActivity,
    DeleteDialogFragment.CancelDeleteItemCallBack {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var noteListAdapter: NoteListAdapter

    private var itemForDelete: NoteItem? = null
    private var itemPosition: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setColorStatusBar()
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupRecyclerView()
        initTopToolBarFirstScreen()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.noteList.observe(this) {
            Log.d("MainActivityTest", it.size.toString())
            noteListAdapter.submitList(it)
            changeMainScreen(it)
        }

        binding.fabAddNote.setOnClickListener {
            val intent = NoteItemActivity.newIntentAddItem(this)
            startActivity(intent)
        }
    }

    private fun setColorStatusBar() {
        val window = this.window
        window.statusBarColor = this.resources.getColor(R.color.black, theme.resources.newTheme())
    }

    private fun initTopToolBarFirstScreen() {
        setSupportActionBar(binding.mainToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_search_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = SearchNoteItemActivity.newIntentSearchItem(this)
        startActivity(intent)
        return super.onOptionsItemSelected(item)
    }

    private fun changeMainScreen(list: List<NoteItem>) {
        if (list.isEmpty()) {
            binding.textViewWelcomeScreen.visibility = View.VISIBLE
            supportActionBar?.hide()
        } else {
            binding.textViewWelcomeScreen.visibility = View.GONE
            supportActionBar?.show()
        }
    }

    private fun setupRecyclerView() {
        noteListAdapter = NoteListAdapter()
        binding.recyclerviewNotes.adapter = noteListAdapter
        setupItemShortClickListener()
        setupSwipeListener(binding.recyclerviewNotes)
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
                itemPosition = viewHolder.adapterPosition
                val deleteDialogFragment = DeleteDialogFragment()
                deleteDialogFragment.show(supportFragmentManager, "delete")
                itemForDelete = noteListAdapter.currentList[viewHolder.adapterPosition]
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvNoteList)
    }

    override fun onDeleteButtonClicked() {
        itemForDelete?.let {
            viewModel.deleteNoteItem(it)
        }
    }

    override fun onCancelDeleteItemClicked() {
        itemPosition?.let {
            noteListAdapter.restoreItem(it)
        }
    }

    companion object {
        fun newIntentHomeScreen(context: Context): Intent {
            val intent = Intent(context, MainActivity::class.java)
            return intent
        }
    }
}