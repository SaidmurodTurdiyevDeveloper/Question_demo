package uzb.smt.questionsdemo.domen.usecase

import uzb.smt.questionsdemo.domen.model.create.CreateQuestionData

class CheckCanCreateQuestion {

    operator fun invoke(data: CreateQuestionData): String? {
        if (data.question.isBlank()) {
            return "Savol matni bo`sh"
        }
        if (data.options.isEmpty()) {
            return "Javoblar kiritilmagan"
        }
        if (data.options.find { it.isCorrect } == null) {
            return "Javoblar orasida to`g`ri variantni belgilang"
        }
        return null
    }
}