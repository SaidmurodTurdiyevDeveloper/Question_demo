package uzb.smt.questionsdemo.data.source.storage.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import uzb.smt.questionsdemo.data.source.storage.room.entities.OptionsEntity
import uzb.smt.questionsdemo.data.source.storage.room.entities.QuestionEntity
import uzb.smt.questionsdemo.data.source.storage.room.entities.QuestionWithOptionsEntity

@Dao
interface QuestionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: QuestionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOptions(options: List<OptionsEntity>)

    @Delete
    suspend fun deleteQuestion(question: QuestionEntity)

    @Delete
    suspend fun deleteOption(option: OptionsEntity)

    @Transaction
    @Query("SELECT * FROM questions")
    suspend fun getAllQuestionsWithOptions(): List<QuestionWithOptionsEntity>

    @Transaction
    @Query("SELECT * FROM questions WHERE id = :id")
    suspend fun getQuestionWithOptionsById(id: Long): QuestionWithOptionsEntity
}