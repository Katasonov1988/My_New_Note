package com.example.mynewnote.presentation

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import com.example.mynewnote.R

class DeleteDialogFragment : DialogFragment() {

    var deleteCallBackToNoteActivity: DeleteCallBackToNoteActivity? = null
    var cancelDeleteCallBackToNoteActivity: CancelDeleteItemCallBack? = null

    interface CancelDeleteItemCallBack {
        fun onCancelDeleteItemClicked()
    }

    interface DeleteCallBackToNoteActivity {
        fun onDeleteButtonClicked()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it, R.style.AlertDialogTheme)

            builder.setMessage(R.string.dialog_message)
                .setPositiveButton(R.string.button_cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        dialog.dismiss()
                    })
                .setNegativeButton(R.string.button_delete,
                    DialogInterface.OnClickListener { dialog, id ->
                        deleteCallBackToNoteActivity?.onDeleteButtonClicked()
                        dialog.dismiss()
                        Log.d("Dialog", "заметка не удалена")
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        deleteCallBackToNoteActivity = getContext() as DeleteCallBackToNoteActivity
        cancelDeleteCallBackToNoteActivity = getContext() as CancelDeleteItemCallBack
    }

    override fun onDismiss(dialog: DialogInterface) {
        Log.d("dialog", "onDismiss")
        cancelDeleteCallBackToNoteActivity?.onCancelDeleteItemClicked()
        super.onDismiss(dialog)
    }

    companion object {
        private const val TAG = "DeleteDialogFragment"
    }
}