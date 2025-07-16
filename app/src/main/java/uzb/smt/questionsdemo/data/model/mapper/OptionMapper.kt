package uzb.smt.questionsdemo.data.model.mapper

import uzb.smt.questionsdemo.data.source.storage.room.entities.OptionsEntity
import uzb.smt.questionsdemo.domen.model.OptionData
import uzb.smt.questionsdemo.domen.model.create.CreateOptionData

fun OptionsEntity.toOptionData(): OptionData {
    return OptionData(
        id = id.toInt(),
        optionText = optionText,
        isCorrect = isCorrect
    )
}

fun CreateOptionData.toOption(id: Long): OptionsEntity {
    return OptionsEntity(
        optionText = optionText,
        questionId = id,
        isCorrect = isCorrect
    )
}
