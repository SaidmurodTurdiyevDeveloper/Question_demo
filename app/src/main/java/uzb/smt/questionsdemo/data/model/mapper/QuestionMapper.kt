package uzb.smt.questionsdemo.data.model.mapper

import uzb.smt.questionsdemo.data.source.storage.room.entities.QuestionEntity
import uzb.smt.questionsdemo.data.source.storage.room.entities.QuestionWithOptionsEntity
import uzb.smt.questionsdemo.domen.model.QuestionData
import uzb.smt.questionsdemo.domen.model.create.CreateQuestionData
import uzb.smt.questionsdemo.domen.model.getTypeQuestion

fun QuestionWithOptionsEntity.toQuestionData(): QuestionData {
    val answers = options.map {
        it.toOptionData()
    }
    return QuestionData(
        id = question.id.toInt(),
        question = question.question,
        type = question.type.getTypeQuestion(),
        options = answers
    )
}
fun QuestionData.toQuestion(): QuestionEntity {

    return QuestionEntity(
        id = id.toLong(),
        question = question,
        type = type.name
    )
}

fun CreateQuestionData.toQuestion(): QuestionEntity {
    return QuestionEntity(
        question = question,
        type = type.name
    )
}