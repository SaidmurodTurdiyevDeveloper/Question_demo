package uzb.smt.questionsdemo.domen.usecase

import uzb.smt.questionsdemo.domen.model.QuestionData
import uzb.smt.questionsdemo.domen.model.QuestionType

class CheckQuizFinish {
    operator fun invoke(
        lst: List<QuestionData>
    ): Boolean {
        lst.forEach { question ->
            val isDone = when (question.type) {
                QuestionType.Radio -> question.options.find {  it.isChecked } != null
                QuestionType.CheckBox -> question.options.find { it.isChecked } != null
                QuestionType.Input -> true
                QuestionType.Selected -> question.options.find { it.isChecked } != null
            }
            if (!isDone)
                return false
        }

        return true
    }
}