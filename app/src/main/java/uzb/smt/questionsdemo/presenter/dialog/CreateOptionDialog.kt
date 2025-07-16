package uzb.smt.questionsdemo.presenter.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.toDrawable
import uzb.smt.questionsdemo.R
import uzb.smt.questionsdemo.databinding.DialogCreateOptionBinding

class CreateOptionDialog(
    context: Context,
    private val onSave: (String) -> Unit
) : Dialog(context) {

    private val binding = DialogCreateOptionBinding.inflate(LayoutInflater.from(context))

    init {
        setContentView(binding.root)
        setCancelable(false)

        binding.saveButton.setOnClickListener {
            val text = binding.edittextAnswer.text.toString().trim()
            if (text.isNotEmpty()) {
                onSave(text)
                dismiss()
            } else {
                Toast.makeText(context, context.getString(R.string.enter_answer), Toast.LENGTH_SHORT).show()
            }
        }

        window?.apply {
            val metrics = context.resources.displayMetrics
            val width = (metrics.widthPixels * 0.9).toInt()
            setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(Color.TRANSPARENT.toDrawable())

            setGravity(Gravity.CENTER)
        }
    }
}

