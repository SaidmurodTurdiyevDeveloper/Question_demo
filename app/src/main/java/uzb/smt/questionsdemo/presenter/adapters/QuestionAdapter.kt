package uzb.smt.questionsdemo.presenter.adapters

import android.R
import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import uzb.smt.questionsdemo.databinding.ItemCheckResultBinding
import uzb.smt.questionsdemo.databinding.ItemQuestionBinding
import uzb.smt.questionsdemo.domen.model.QuestionData
import uzb.smt.questionsdemo.domen.model.QuestionType

class QuestionAdapter(
    private val changeItem: (QuestionData, Int) -> Unit,
    private val deleteItem: (QuestionData) -> Unit,
    private val onCheckClick: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_QUESTION = 0
        private const val VIEW_TYPE_CHECK_BUTTON = 1
    }

    var isActive = false
    private val differCallback = object : DiffUtil.ItemCallback<QuestionData>() {
        override fun areItemsTheSame(oldItem: QuestionData, newItem: QuestionData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: QuestionData, newItem: QuestionData): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    fun submitList(newList: List<QuestionData>) {
        differ.submitList(newList)
    }

    override fun getItemCount(): Int = differ.currentList.size + 1

    override fun getItemViewType(position: Int): Int {
        return if (position == differ.currentList.size) VIEW_TYPE_CHECK_BUTTON else VIEW_TYPE_QUESTION
    }

    inner class QuestionViewHolder(val binding: ItemQuestionBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class CheckResultViewHolder(val binding: ItemCheckResultBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_CHECK_BUTTON -> {
                val binding = ItemCheckResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                CheckResultViewHolder(binding)
            }

            else -> {
                val binding = ItemQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                QuestionViewHolder(binding)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CheckResultViewHolder) {
            holder.binding.checkResult.isEnabled = isActive
            holder.binding.checkResult.setOnClickListener {
                if (isActive) {
                    onCheckClick()
                }
            }
        } else if (holder is QuestionViewHolder) {
            val item = differ.currentList[position]
            holder.binding.questionTitle.text = "${position + 1}. ${item.question}"

            when (item.type) {
                QuestionType.Radio -> radioGroupHolder(holder, item)
                QuestionType.CheckBox -> checkBoxHolder(holder, item)
                QuestionType.Input -> inputHolder(holder, item)
                QuestionType.Selected -> selectedHolder(holder, item)
            }

            holder.binding.root.setOnClickListener {
                changeItem(item, position)
            }

            holder.binding.btnDelete.setOnClickListener {
                deleteItem(item)
            }
        }
    }


    private fun radioGroupHolder(holder: QuestionViewHolder, item: QuestionData) {
        val radioGroup = holder.binding.radioSelectContainer
        radioGroup.setOnCheckedChangeListener(null)

        radioGroup.isVisible = true
        holder.binding.edittextAnswerContainer.isVisible = false
        holder.binding.dropdownMenuLayout.isVisible = false
        holder.binding.checkboxContainer.isVisible = false

        radioGroup.removeAllViews()

        val spacingInDp = 8
        val spacingInPx = (spacingInDp * holder.itemView.context.resources.displayMetrics.density).toInt()

        item.options.forEachIndexed { index, option ->
            val radioButton = RadioButton(holder.itemView.context).apply {
                text = option.optionText
                isChecked = option.isChecked
                id = View.generateViewId()

                layoutParams = RadioGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    topMargin = spacingInPx
                    bottomMargin = if (index != item.options.lastIndex) spacingInPx else 0
                }
            }
            radioGroup.addView(radioButton)
        }

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == -1) return@setOnCheckedChangeListener

            val selectedIndex = group.indexOfChild(group.findViewById(checkedId))
            if (selectedIndex < 0 || selectedIndex >= item.options.size) return@setOnCheckedChangeListener

            val updatedOptions = item.options.mapIndexed { i, opt ->
                opt.copy(isChecked = i == selectedIndex)
            }

            val newItem = item.copy(options = updatedOptions)

            // Check adapter position validity
            val adapterPos = holder.adapterPosition
            if (adapterPos != RecyclerView.NO_POSITION) {
                changeItem(newItem, adapterPos)
            }
        }
    }


    private fun checkBoxHolder(holder: QuestionViewHolder, item: QuestionData) {
        holder.binding.radioSelectContainer.isVisible = false
        holder.binding.edittextAnswerContainer.isVisible = false
        holder.binding.dropdownMenuLayout.isVisible = false
        holder.binding.checkboxContainer.isVisible = true

        val container = holder.binding.checkboxContainer
        container.removeAllViews()

        val spacingInDp = 8
        val spacingInPx = (spacingInDp * holder.itemView.context.resources.displayMetrics.density).toInt()

        item.options.forEachIndexed { index, option ->
            val checkBox = CheckBox(holder.itemView.context).apply {
                text = option.optionText
                isChecked = option.isChecked
                id = View.generateViewId()

                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    topMargin = spacingInPx
                    bottomMargin = if (index != item.options.lastIndex) spacingInPx else 0
                }
            }

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                val newOptions = item.options.mapIndexed { i, opt ->
                    if (i == index) opt.copy(isChecked = isChecked) else opt
                }

                val newItem = item.copy(options = newOptions)
                changeItem(newItem, holder.adapterPosition)
            }

            container.addView(checkBox)
        }
    }


    private fun inputHolder(holder: QuestionViewHolder, item: QuestionData) {
        holder.binding.radioSelectContainer.isVisible = false
        holder.binding.edittextAnswerContainer.isVisible = true
        holder.binding.dropdownMenuLayout.isVisible = false
        holder.binding.checkboxContainer.isVisible = false

        holder.binding.edittextAnswer.setText(item.options.firstOrNull()?.writeText ?: "")

        holder.binding.edittextAnswer.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val newText = s.toString()
                item.options.firstOrNull()?.writeText = newText
            }
        })

    }

    private fun selectedHolder(holder: QuestionViewHolder, item: QuestionData) {
        holder.binding.radioSelectContainer.isVisible = false
        holder.binding.edittextAnswerContainer.isVisible = false
        holder.binding.dropdownMenuLayout.isVisible = true
        holder.binding.checkboxContainer.isVisible = false

        val context = holder.itemView.context
        val dropdownView = holder.binding.dropdownMenu

        dropdownView.setAdapter(null)

        val optionList = item.options.map { it.optionText }

        val adapter = ArrayAdapter(context, R.layout.simple_dropdown_item_1line, optionList)
        dropdownView.setAdapter(adapter)

        val selectedOption = item.options.find { it.isChecked }
        dropdownView.setText(selectedOption?.optionText ?: "", false)

        dropdownView.setOnItemClickListener { _, _, position, _ ->
            val newOptions = item.options.mapIndexed { index, option ->
                option.copy(isChecked = index == position)
            }
            val newItem = item.copy(options = newOptions)
            changeItem(newItem, holder.adapterPosition)
        }

    }
}

