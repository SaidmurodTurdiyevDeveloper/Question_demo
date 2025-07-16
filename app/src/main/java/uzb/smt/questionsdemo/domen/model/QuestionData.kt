package uzb.smt.questionsdemo.domen.model

data class QuestionData(
    val id: Int,
    val question: String,
    val type: QuestionType,
    val options: List<OptionData>
)
