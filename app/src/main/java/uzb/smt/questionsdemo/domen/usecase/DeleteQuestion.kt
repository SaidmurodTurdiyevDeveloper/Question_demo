package uzb.smt.questionsdemo.domen.usecase

import uzb.smt.questionsdemo.domen.model.QuestionData
import uzb.smt.questionsdemo.domen.repository.QuestionRepository

class DeleteQuestion(private val repository: QuestionRepository) {
    operator fun invoke(data: QuestionData) = repository.deleteQuestion(data)
}