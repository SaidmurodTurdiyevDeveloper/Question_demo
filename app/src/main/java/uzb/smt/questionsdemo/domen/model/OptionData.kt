package uzb.smt.questionsdemo.domen.model

data class OptionData(
    val id: Int,
    val optionText: String,
    val isCorrect: Boolean,
    var writeText: String="",
    val isChecked: Boolean=false
)
