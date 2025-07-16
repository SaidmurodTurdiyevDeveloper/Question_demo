package uzb.smt.questionsdemo.domen.usecase

import uzb.smt.questionsdemo.domen.repository.QuestionRepository

class GetQuestions(private val repository: QuestionRepository) {
    operator fun invoke()=repository.getQuestions()
}