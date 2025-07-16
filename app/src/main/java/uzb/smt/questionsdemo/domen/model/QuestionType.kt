package uzb.smt.questionsdemo.domen.model

enum class QuestionType {
    Radio, CheckBox, Input, Selected
}

fun String.getTypeQuestion(): QuestionType {
    return when (this) {
        QuestionType.Radio.name -> QuestionType.Radio
        QuestionType.CheckBox.name -> QuestionType.CheckBox
        QuestionType.Input.name -> QuestionType.Input
        QuestionType.Selected.name -> QuestionType.Selected
        else -> QuestionType.Radio
    }
}