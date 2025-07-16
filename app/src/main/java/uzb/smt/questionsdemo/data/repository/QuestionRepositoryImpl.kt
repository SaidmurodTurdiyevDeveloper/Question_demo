package uzb.smt.questionsdemo.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uzb.smt.questionsdemo.data.model.ResponseData
import uzb.smt.questionsdemo.data.model.mapper.toOption
import uzb.smt.questionsdemo.data.model.mapper.toQuestion
import uzb.smt.questionsdemo.data.model.mapper.toQuestionData
import uzb.smt.questionsdemo.data.source.storage.room.dao.QuestionDao
import uzb.smt.questionsdemo.domen.model.QuestionData
import uzb.smt.questionsdemo.domen.model.QuestionType
import uzb.smt.questionsdemo.domen.model.create.CreateOptionData
import uzb.smt.questionsdemo.domen.model.create.CreateQuestionData
import uzb.smt.questionsdemo.domen.repository.QuestionRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuestionRepositoryImpl @Inject constructor(
    private val questionDao: QuestionDao
) : QuestionRepository {
    private val demo = listOf(
        CreateQuestionData(
            question = "Android operatsion tizimi qachon chiqarilgan?",
            type = QuestionType.Radio,
            options = listOf(
                CreateOptionData(optionText = "2008", isCorrect = true),
                CreateOptionData(optionText = "2010"),
                CreateOptionData(optionText = "2006"),
                CreateOptionData(optionText = "2012")
            )
        ),
        CreateQuestionData(
            question = "Quyidagilardan qaysi biri dasturlash tillaridir?",
            type = QuestionType.CheckBox,
            options = listOf(
                CreateOptionData(optionText = "Python", isCorrect = true),
                CreateOptionData(optionText = "Kotlin", isCorrect = true),
                CreateOptionData(optionText = "Google"),
                CreateOptionData(optionText = "Jetpack")
            )
        ),
        CreateQuestionData(
            question = "Jetpack Compose nima uchun ishlatiladi?",
            type = QuestionType.Input,
            options = listOf(
                CreateOptionData(optionText = "UI yaratish", isCorrect = true)
            )
        ),
        CreateQuestionData(
            question = "Android Studio IDE'sining ishlab chiqaruvchisi kim?",
            type = QuestionType.Selected,
            options = listOf(
                CreateOptionData(optionText = "JetBrains"),
                CreateOptionData(optionText = "Oracle"),
                CreateOptionData(optionText = "Google", isCorrect = true),
                CreateOptionData(optionText = "Microsoft")
            )
        ),
        CreateQuestionData(
            question = "ViewModel nima uchun ishlatiladi?",
            type = QuestionType.Radio,
            options = listOf(
                CreateOptionData(optionText = "Ma'lumotlar saqlash uchun", isCorrect = true),
                CreateOptionData(optionText = "UI dizayn qilish"),
                CreateOptionData(optionText = "Ma'lumot yuborish"),
                CreateOptionData(optionText = "HTTP so'rovlar uchun")
            )
        ),
        CreateQuestionData(
            question = "Quyidagilardan qaysi biri Android komponentlari hisoblanadi?",
            type = QuestionType.CheckBox,
            options = listOf(
                CreateOptionData(optionText = "Activity", isCorrect = true),
                CreateOptionData(optionText = "Service", isCorrect = true),
                CreateOptionData(optionText = "BroadcastReceiver", isCorrect = true),
                CreateOptionData(optionText = "Firebase")
            )
        ),
        CreateQuestionData(
            question = "Gradle nima?",
            type = QuestionType.Input,
            options = listOf(
                CreateOptionData(optionText = "Build system", isCorrect = true)
            )
        ),
        CreateQuestionData(
            question = "Android dasturini ishlab chiqishda qaysi til eng ko‘p qo‘llaniladi?",
            type = QuestionType.Selected,
            options = listOf(
                CreateOptionData(optionText = "Java"),
                CreateOptionData(optionText = "C++"),
                CreateOptionData(optionText = "Kotlin", isCorrect = true),
                CreateOptionData(optionText = "Swift")
            )
        )
    )

    override fun getQuestions(): Flow<ResponseData<List<QuestionData>>> = flow {
        try {
            val entities = questionDao.getAllQuestionsWithOptions()
            if (entities.isEmpty()) {
                demo.forEach { question ->
                    insertQuestion(question).collect { result ->
                        when (result) {
                            is ResponseData.Success -> { /* ... */ }
                            is ResponseData.Error -> { /* ... */ }
                        }
                    }
                }
                val new = questionDao.getAllQuestionsWithOptions()
                val questions = new.map { it.toQuestionData() }
                emit(ResponseData.Success(questions))
            } else {

                val questions = entities.map { it.toQuestionData() }
                emit(ResponseData.Success(questions))
            }
        } catch (e: Exception) {
            emit(ResponseData.Error("Savollarni yuklashda xatolik: ${e.localizedMessage}"))
        }
    }

    override fun insertQuestion(createQuestionData: CreateQuestionData): Flow<ResponseData<Boolean>> = flow {
        try {
            val questionEntity = createQuestionData.toQuestion()
            val id = questionDao.insertQuestion(questionEntity)

            val options = createQuestionData.options.mapIndexed { index, option ->
                option.toOption(id)
            }
            questionDao.insertOptions(options)

            emit(ResponseData.Success(true))
        } catch (e: Exception) {
            emit(ResponseData.Error("Savolni saqlashda xatolik: ${e.localizedMessage}"))
        }
    }

    override fun deleteQuestion(question: QuestionData): Flow<ResponseData<Boolean>> = flow {
        try {
            questionDao.deleteQuestion(question.toQuestion())
            emit(ResponseData.Success(true))
        } catch (e: Exception) {
            emit(ResponseData.Error("Savolni o‘chirishda xatolik: ${e.localizedMessage}"))
        }
    }
}
