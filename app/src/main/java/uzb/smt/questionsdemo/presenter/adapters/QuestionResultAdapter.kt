package uzb.smt.questionsdemo.presenter.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import uzb.smt.questionsdemo.R
import uzb.smt.questionsdemo.databinding.ItemResultBinding
import uzb.smt.questionsdemo.domen.model.QuestionData
import uzb.smt.questionsdemo.domen.model.QuestionResultData
import uzb.smt.questionsdemo.domen.model.QuestionType
import uzb.smt.questionsdemo.utils.createBorderBackground

class QuestionResultAdapter :
    RecyclerView.Adapter<QuestionResultAdapter.QuestionViewHolder>() {

    inner class QuestionViewHolder(val binding: ItemResultBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<QuestionResultData>() {
        override fun areItemsTheSame(
            oldItem: QuestionResultData,
            newItem: QuestionResultData
        ): Boolean = oldItem.question.id == newItem.question.id

        override fun areContentsTheSame(
            oldItem: QuestionResultData,
            newItem: QuestionResultData
        ): Boolean = oldItem == newItem
    }

    private val differ = AsyncListDiffer(this, differCallback)

    fun submitList(list: List<QuestionResultData>) {
        differ.submitList(list)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding = ItemResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestionViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val item = differ.currentList[position]

        holder.binding.questionTitle.text = "${position + 1}. ${item.question.question}"

        val borderColor = if (item.isCorrect) R.color.green else R.color.red
        holder.binding.itemContainer.background =
            createBorderBackground(holder.itemView.context, borderColor)

        if (item.question.type == QuestionType.Input) {
            holder.binding.inputResultContainer.isVisible = true
            holder.binding.optionsContainer.isVisible = false

            val option = item.question.options.firstOrNull()
            holder.binding.correctAnswer.text = option?.optionText
            holder.binding.yourAnswer.text = option?.writeText

            val color = ContextCompat.getColor(
                holder.itemView.context,
                if (item.isCorrect) R.color.green else R.color.red
            )
            holder.binding.answerIcon.setColorFilter(color, PorterDuff.Mode.SRC_IN)

        } else {
            holder.binding.inputResultContainer.isVisible = false
            holder.binding.optionsContainer.isVisible = true

            when (item.question.type) {
                QuestionType.Radio -> radioGroupHolder(holder, item.question)
                QuestionType.CheckBox -> checkBoxHolder(holder, item.question)
                else -> selectOptionHolder(holder, item.question)
            }
        }
    }

    private fun radioGroupHolder(holder: QuestionViewHolder, item: QuestionData) {
        val container = holder.binding.optionsContainer
        container.removeAllViews()

        val spacingInPx = dpToPx(holder.itemView.context, 8)

        item.options.forEachIndexed { index, option ->
            val radioButton = RadioButton(holder.itemView.context).apply {
                text = option.optionText
                isChecked = option.isCorrect || option.isChecked
                isEnabled = false
                val colorRes = when {
                    option.isCorrect -> R.color.green
                    option.isChecked && !option.isCorrect -> R.color.red
                    else -> android.R.color.darker_gray
                }
                val color = ContextCompat.getColor(context, colorRes)
                setTextColor(color)
                buttonTintList = ColorStateList.valueOf(color)
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    topMargin = spacingInPx
                    bottomMargin = if (index != item.options.lastIndex) spacingInPx else 0
                }
            }
            container.addView(radioButton)
        }
    }

    private fun checkBoxHolder(holder: QuestionViewHolder, item: QuestionData) {
        val container = holder.binding.optionsContainer
        container.removeAllViews()
        val spacingInPx = dpToPx(holder.itemView.context, 8)

        item.options.forEachIndexed { index, option ->
            val checkBox = CheckBox(holder.itemView.context).apply {
                text = option.optionText
                isChecked = option.isCorrect || option.isChecked
                isEnabled = false
                id = View.generateViewId()

                val colorRes = when {
                    option.isCorrect -> R.color.green
                    option.isChecked && !option.isCorrect -> R.color.red
                    else -> android.R.color.darker_gray
                }
                val color = ContextCompat.getColor(context, colorRes)
                setTextColor(color)
                buttonTintList = ColorStateList.valueOf(color)

                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    topMargin = spacingInPx
                    bottomMargin = if (index != item.options.lastIndex) spacingInPx else 0
                }
            }

            container.addView(checkBox)
        }
    }

    private fun selectOptionHolder(holder: QuestionViewHolder, item: QuestionData) {
        val container = holder.binding.optionsContainer
        container.removeAllViews()

        item.options.forEachIndexed { _, option ->
            val optionView = LayoutInflater.from(holder.itemView.context)
                .inflate(R.layout.item_selected_result, container, false)

            val textView = optionView.findViewById<TextView>(R.id.optionText)
            val iconView = optionView.findViewById<ImageView>(R.id.optionIcon)

            textView.text = option.optionText
            iconView.visibility = View.VISIBLE

            when {
                option.isCorrect -> {
                    iconView.setImageResource(R.drawable.ic_check_filled)
                    iconView.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.green))
                }

                option.isChecked && !option.isCorrect -> {
                    iconView.setImageResource(R.drawable.ic_check_filled)
                    iconView.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.red))
                }

                else -> {
                    iconView.setImageResource(R.drawable.ic_check_outline)
                }
            }

            container.addView(optionView)
        }
    }

    private fun dpToPx(context: Context, dp: Int): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }
}
