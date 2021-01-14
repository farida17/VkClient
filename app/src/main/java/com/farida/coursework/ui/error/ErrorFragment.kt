package com.farida.coursework.ui.error

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.farida.coursework.R
import moxy.MvpAppCompatDialogFragment

class ErrorFragment : MvpAppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.error_title)
            .setMessage(R.string.error_message)
            .setPositiveButton(R.string.error_button_title) { dialog, _ ->
                dialog.cancel()
            }
            .create()
    }
}