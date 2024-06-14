package com.example.scheduleui.ui.setting.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.scheduleui.R

/**
 * Customer Dialog with EditText
 *
 * @param title title of dialog
 * @param currentValue current value of view
 * @param positiveButtonClick event positive button click
 */
class TextInputDialogFragment(
    private val title: String,
    private val currentValue: String,
    private val positiveButtonClick: (String) -> Unit
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            // Create dialog builder
            val builder = AlertDialog.Builder(context)
            // Inflate layout for dialog
            val dialogLayout = layoutInflater.inflate(R.layout.text_input_dialog_layout, null)
            // Get edit text
            val editText = dialogLayout.findViewById<EditText>(R.id.textInput)
            // Set text of current value
            editText.setText(currentValue)
            // Set layout for dialog
            builder.setView(dialogLayout)
                .setTitle(title)
                .setPositiveButton("Oke") { _, _ ->
                    positiveButtonClick(editText.text.toString())
                }

            // Create dialog
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {
        const val TAG = "TextInputDialogFragment"
    }
}