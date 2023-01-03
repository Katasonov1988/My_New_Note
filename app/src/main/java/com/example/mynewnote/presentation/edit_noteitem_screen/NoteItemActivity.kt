package com.example.mynewnote.presentation.edit_noteitem_screen

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.mynewnote.R
import com.example.mynewnote.databinding.ActivityNoteItemBinding
import com.example.mynewnote.presentation.DeleteDialogFragment
import com.example.mynewnote.presentation.first_screen.MainActivity
import java.lang.RuntimeException

class NoteItemActivity : AppCompatActivity(),
    DeleteDialogFragment.DeleteCallBackToNoteActivity,
    DeleteDialogFragment.CancelDeleteItemCallBack {

    private lateinit var binding: ActivityNoteItemBinding
    private lateinit var viewModel: NoteItemViewModel

    private var screenMode = MODE_UNKNOWN
    private var noteItemId = MODE_UNKNOWN

    override fun onCreate(savedInstanceState: Bundle?) {
        setColorStatusBar()
        super.onCreate(savedInstanceState)
        binding = ActivityNoteItemBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initTopToolbarSecondScreen()
        buttonsBottomAppBar()
        parseIntent()

        viewModel = ViewModelProvider(this)[NoteItemViewModel::class.java]
        viewModel.shouldCloseScreen.observe(this) {
            finish()
        }

        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }

    }

    private fun buttonsBottomAppBar() {
        binding.bottomAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.bottomAppBarDelete -> {
                    callDeleteDialog()
                    true
                }
                R.id.bottomAppBarShare -> {
                    shareNote()
                    true
                }
                R.id.bottomAppBarBackToHome -> {
                    goToHomeScreen()
                    true
                }
                else -> false
            }
        }
    }

    private fun goToHomeScreen() {
        val intent = MainActivity.newIntentHomeScreen(this)
        startActivity(intent)
    }

    private fun shareNote() {
        if (
            binding.etHeader.text?.isBlank() == true &&
            binding.etDescription.text?.isBlank() == true
        ) {
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
                putExtra(Intent.EXTRA_SUBJECT, binding.etHeader.text?.trim())
                putExtra(Intent.EXTRA_TEXT, binding.etDescription.text?.trim())
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }

    private fun initTopToolbarSecondScreen() {
        setSupportActionBar(binding.topToolbarSecondScreen)
        with(binding.topToolbarSecondScreen) {
            setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            setNavigationOnClickListener {
                saveNoteForBackButton()
            }
        }
    }

    private fun launchEditMode() {
        viewModel.getNoteItem(noteItemId)
        viewModel.noteItem.observe(this) {
            binding.etHeader.setText(it.header)
            binding.etDescription.setText(it.description)
        }
        binding.fabSaveNote.setOnClickListener {
            if (
                binding.etHeader.text?.isEmpty() == true &&
                binding.etDescription.text?.isEmpty() == true
            ) {
                onDeleteButtonClicked()
            }
            viewModel.editNoteItem(
                binding.etHeader.text?.toString(),
                binding.etDescription.text?.toString()
            )
        }
    }

    private fun launchAddMode() {
        binding.fabSaveNote.setOnClickListener {
            if (
                binding.etHeader.text?.isEmpty() == true &&
                binding.etDescription.text?.isEmpty() == true
            ) {
                finish()
            }
            viewModel.addNoteItem(
                binding.etHeader.text?.toString(),
                binding.etDescription.text?.toString()
            )
        }
    }

    private fun callDeleteDialog() {
        if (screenMode == MODE_EDIT) {
            val deleteDialogFragment = DeleteDialogFragment()
            deleteDialogFragment.show(supportFragmentManager, DELETE)
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

    companion object {
        private const val DELETE = "delete"
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

    private fun saveNoteForBackButton() {
        finish()
    }

    private fun setColorStatusBar() {
        val window = this.window
        window.statusBarColor = this.resources.getColor(R.color.black, theme.resources.newTheme())
    }

    override fun onCancelDeleteItemClicked() {
        Log.d("NoteItemActivity", "нажата кнопка отмены в диалоге")
    }

}