package uzb.smt.questionsdemo.presenter.adapters

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uzb.smt.questionsdemo.R
import uzb.smt.questionsdemo.databinding.ItemOptionBinding
import uzb.smt.questionsdemo.domen.model.QuestionType
import uzb.smt.questionsdemo.domen.model.create.CreateOptionData

class OptionAdapter(
    private val onCorrectSelected: (CreateOptionData, Int) -> Unit,
    private val onDelete: (CreateOptionData) -> Unit
) : ListAdapter<CreateOptionData, OptionAdapter.OptionViewHolder>(DiffCallback) {
    var type: QuestionType? = null

    inner class OptionViewHolder(val binding: ItemOptionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val binding = ItemOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OptionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        val item = getItem(position)

        holder.binding.optionText.text = item.optionText
        when (type) {
            QuestionType.Radio -> {
                holder.binding.optionIcon.isVisible = false
                holder.binding.rb.isVisible = true
                holder.binding.btnDelete.isVisible = true
                holder.binding.chb.isVisible = false
                holder.binding.rb.isChecked = item.isCorrect
                holder.binding.rb.setOnClickListener {
                    onCorrectSelected(item.copy(isCorrect = true), position)
                }
                holder.binding.root.setOnClickListener {
                    onCorrectSelected(item.copy(isCorrect = true), position)
                }
            }

            QuestionType.CheckBox -> {
                holder.binding.optionIcon.isVisible = false
                holder.binding.rb.isVisible = false
                holder.binding.btnDelete.isVisible = true
                holder.binding.chb.isVisible = true
                holder.binding.chb.isChecked = item.isCorrect
                holder.binding.chb.setOnClickListener {
                    onCorrectSelected(item.copy(isCorrect = !item.isCorrect), position)
                }
                holder.binding.root.setOnClickListener {
                    onCorrectSelected(item.copy(isCorrect = !item.isCorrect), position)
                }
            }

            QuestionType.Input -> {
                holder.binding.optionIcon.isVisible = true
                holder.binding.rb.isVisible = false
                holder.binding.chb.isVisible = false
                holder.binding.btnDelete.isVisible = false
                val tint = R.color.blue_2
                holder.binding.optionIcon.setColorFilter(
                    ContextCompat.getColor(holder.itemView.context, tint),
                    PorterDuff.Mode.SRC_IN
                )
                holder.binding.optionIcon.setImageResource(R.drawable.ic_chat)

            }

            QuestionType.Selected -> {
                holder.binding.optionIcon.isVisible = true
                holder.binding.rb.isVisible = false
                holder.binding.btnDelete.isVisible = true
                holder.binding.chb.isVisible = false
                val tint = if (item.isCorrect) R.color.green else R.color.blue_2
                holder.binding.optionIcon.setColorFilter(
                    ContextCompat.getColor(holder.itemView.context, tint),
                    PorterDuff.Mode.SRC_IN
                )
                if (item.isCorrect)
                    holder.binding.optionIcon.setImageResource(R.drawable.ic_check_filled)
                else
                    holder.binding.optionIcon.setImageResource(R.drawable.ic_check_outline)


                holder.binding.optionIcon.setOnClickListener {
                    onCorrectSelected(item.copy(isCorrect = true), position)
                }
                holder.binding.root.setOnClickListener {
                    onCorrectSelected(item.copy(isCorrect = true), position)
                }
            }

            null -> {
                holder.binding.optionIcon.isVisible = false
                holder.binding.rb.isVisible = true
                holder.binding.chb.isVisible = false
                holder.binding.rb.isChecked = item.isCorrect
                holder.binding.rb.setOnClickListener {
                    onCorrectSelected(item.copy(isCorrect = true), position)
                }
                holder.binding.root.setOnClickListener {
                    onCorrectSelected(item.copy(isCorrect = true), position)
                }
            }
        }
        holder.binding.btnDelete.setOnClickListener {
            onDelete(item)
        }


    }

    object DiffCallback : DiffUtil.ItemCallback<CreateOptionData>() {
        override fun areItemsTheSame(oldItem: CreateOptionData, newItem: CreateOptionData): Boolean {
            return oldItem.optionText == newItem.optionText
        }

        override fun areContentsTheSame(oldItem: CreateOptionData, newItem: CreateOptionData): Boolean {
            return oldItem == newItem
        }
    }
}
