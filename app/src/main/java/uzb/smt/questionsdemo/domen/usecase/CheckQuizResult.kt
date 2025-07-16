package uzb.smt.questionsdemo.domen.usecase

import uzb.smt.questionsdemo.domen.model.QuestionData
import uzb.smt.questionsdemo.domen.model.QuestionResultData
import uzb.smt.questionsdemo.domen.model.QuestionType

class CheckQuizResult {
    operator fun invoke(
        lst: List<QuestionData>,
    ): List<QuestionResultData>{
        val result = lst.map { question ->
            val isCorrect = when(question.type){
                QuestionType.Radio -> question.options.find { it.isCorrect!=it.isChecked }==null
                QuestionType.CheckBox -> question.options.find { it.isCorrect!=it.isChecked }==null
                QuestionType.Input -> question.options.firstOrNull()?.optionText==question.options.firstOrNull()?.writeText
                QuestionType.Selected -> question.options.find { it.isCorrect!=it.isChecked }==null
            }
            QuestionResultData(
                question = question,
                isCorrect = isCorrect
            )
        }
        return result
    }
}