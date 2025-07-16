package uzb.smt.questionsdemo.presenter.dialog

import android.content.Context
import androidx.appcompat.app.AlertDialog

class ErrorDialog(
    private val context: Context
) {


    private var dialog: AlertDialog? = null

    fun show(message: String) {

        dialog = AlertDialog.Builder(context)
            .setTitle("Xatolik")
            .setMessage(message).setNegativeButton("Ok") { _, _ ->
                dialog?.dismiss()
            }
            .create()


        dialog?.show()
    }
}