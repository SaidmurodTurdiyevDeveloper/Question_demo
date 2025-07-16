package uzb.smt.questionsdemo.data.source.storage.room.entities

import androidx.room.Embedded
import androidx.room.Relation

data class QuestionWithOptionsEntity(
    @Embedded val question: QuestionEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "questionId"
    )
    val options: List<OptionsEntity>
)