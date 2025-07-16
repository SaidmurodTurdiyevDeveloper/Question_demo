package uzb.smt.questionsdemo.presenter.dialog

import android.content.Context
import androidx.appcompat.app.AlertDialog
import uzb.smt.questionsdemo.domen.model.QuestionData

class DeleteDialog(
    private val context: Context,
    private val listener: (QuestionData) -> Unit,
    private val dismiss: () -> Unit
) {
    private var dialog: AlertDialog? = null

    fun show(message: String, question: QuestionData) {
        dialog = AlertDialog.Builder(context)
            .setTitle("O‘chirish")
            .setMessage(message)
            .setNegativeButton("Yo‘q") { dialogInterface, _ ->
                dismiss()
                dialogInterface.dismiss()
            }
            .setPositiveButton("Ha") { dialogInterface, _ ->
                listener.invoke(question)
                dialogInterface.dismiss()
            }
            .setOnDismissListener {
                dismiss()
                dialog = null
            }
            .create()

        dialog?.show()
    }

}
