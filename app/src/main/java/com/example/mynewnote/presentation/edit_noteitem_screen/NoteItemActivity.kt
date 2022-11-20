package com.example.mynewnote.presentation.edit_noteitem_screen

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.example.mynewnote.R
import com.example.mynewnote.presentation.DeleteDialogFragment
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import java.lang.RuntimeException

class NoteItemActivity : AppCompatActivity(), DeleteDialogFragment.DeleteCallBackToNoteActivity {

    private lateinit var bottomAppBar: BottomAppBar
    private lateinit var topToolbarSecondScreen: Toolbar

    private lateinit var viewModel: NoteItemViewModel

    private lateinit var tilHeader: TextInputLayout
    private lateinit var tilDescription: TextInputLayout
    private lateinit var etHeader: EditText
    private lateinit var etDescription: EditText
    private lateinit var fabSave: FloatingActionButton

    private var screenMode = MODE_UNKNOWN
    private var noteItemId = MODE_UNKNOWN

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_item)
        initTopToolbarSecondScreen()
        initViews()
        buttonsBottomAppBar()
        parseIntent()
        viewModel = ViewModelProvider(this)[NoteItemViewModel::class.java]

        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
        viewModel.shouldCloseScreen.observe(this) {
            finish()
        }
    }

    private fun buttonsBottomAppBar() {
        bottomAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.bottomAppBarDelete -> {
                    callDeleteDialog()
                    true
                }
                R.id.bottomAppBarShare -> {
                    shareNote()
                    true
                }
                else -> false
            }
        }
    }


    private fun shareNote() {
        if (etHeader.text.isBlank() && etDescription.text.isBlank()) {
            val toast = Toast.makeText(
                this,
                ALL_LINE_EMPTY,
                Toast.LENGTH_SHORT
            )
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        } else {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_SUBJECT, etHeader.text.trim())
                putExtra(Intent.EXTRA_TEXT, etDescription.text.trim())
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }

    private fun initTopToolbarSecondScreen() {
        topToolbarSecondScreen = findViewById(R.id.topToolbarSecondScreen)
        setSupportActionBar(topToolbarSecondScreen)
        with(topToolbarSecondScreen) {
            setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            setNavigationOnClickListener {
                finish()
            }
        }
    }

    private fun launchEditMode() {
        viewModel.getNoteItem(noteItemId)
        viewModel.noteItem.observe(this) {
            etHeader.setText(it.header)
            etDescription.setText(it.description)
        }
        fabSave.setOnClickListener {
            viewModel.editNoteItem(etHeader.text?.toString(), etDescription.text?.toString())
        }
    }

    private fun launchAddMode() {
        fabSave.setOnClickListener {
            viewModel.addNoteItem(etHeader.text?.toString(), etDescription.text?.toString())
        }
    }

    private fun callDeleteDialog() {
        if (screenMode == MODE_EDIT) {
            val deleteDialogFragment = DeleteDialogFragment()
            deleteDialogFragment.show(supportFragmentManager, "delete")
        } else {
            val toast = Toast.makeText(
                this,
                NOTHING_TO_DELETE,
                Toast.LENGTH_SHORT
            )
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }

    }

    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)) {
                throw RuntimeException("Param note item id is absent")
            }
            if (intent.getStringExtra(EXTRA_SHOP_ITEM_ID) != null) {
                noteItemId = intent.getStringExtra(EXTRA_SHOP_ITEM_ID).toString()
            }
        }
    }

    fun initViews() {
        bottomAppBar = findViewById(R.id.bottomAppBar)
        tilHeader = findViewById(R.id.tilHeader)
        tilDescription = findViewById(R.id.tilDescription)
        etHeader = findViewById(R.id.etHeader)
        etDescription = findViewById(R.id.etDescription)
        fabSave = findViewById(R.id.fabSaveNote)
    }

    companion object {
        private const val NOTHING_TO_DELETE = "Нечего удалять"
        private const val ALL_LINE_EMPTY = "Все поля пустые, нечего отправлять"
        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, NoteItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }

        fun newIntentEditItem(context: Context, noteItemId: String): Intent {
            val intent = Intent(context, NoteItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, noteItemId)
            return intent
        }
    }

    override fun onDeleteButtonClicked() {
        viewModel.noteItem.observe(this) {
            viewModel.deleteNoteFromNoteItemActivity(it)
            finish()
        }

    }

}