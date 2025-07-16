package uzb.smt.questionsdemo.domen.repository

import kotlinx.coroutines.flow.Flow
import uzb.smt.questionsdemo.data.model.ResponseData
import uzb.smt.questionsdemo.domen.model.QuestionData
import uzb.smt.questionsdemo.domen.model.create.CreateQuestionData

interface QuestionRepository {
    fun getQuestions(): Flow<ResponseData<List<QuestionData>>>
    fun insertQuestion(createQuestionData: CreateQuestionData): Flow<ResponseData<Boolean>>
    fun deleteQuestion(question: QuestionData): Flow<ResponseData<Boolean>>
}