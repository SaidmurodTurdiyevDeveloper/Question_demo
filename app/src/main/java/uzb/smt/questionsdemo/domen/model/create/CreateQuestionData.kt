package uzb.smt.questionsdemo.domen.model.create

import uzb.smt.questionsdemo.domen.model.QuestionType

data class CreateQuestionData(
    val question: String,
    val type: QuestionType,
    val options: List<CreateOptionData>
)