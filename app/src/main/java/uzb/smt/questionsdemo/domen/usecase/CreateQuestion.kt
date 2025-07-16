package uzb.smt.questionsdemo.domen.usecase

import uzb.smt.questionsdemo.domen.model.create.CreateQuestionData
import uzb.smt.questionsdemo.domen.repository.QuestionRepository

class CreateQuestion(private val repository: QuestionRepository) {
    operator fun invoke(createData: CreateQuestionData) = repository.insertQuestion(createData)
}