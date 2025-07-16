package uzb.smt.questionsdemo.presenter.screen.results

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uzb.smt.questionsdemo.domen.model.QuestionData
import uzb.smt.questionsdemo.domen.model.QuestionResultData
import uzb.smt.questionsdemo.domen.usecase.CheckQuizResult
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    checkQuizResult: CheckQuizResult,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _results = MutableStateFlow<List<QuestionResultData>>(emptyList())
    val results: StateFlow<List<QuestionResultData>> = _results.asStateFlow()

    private val _correctCountText = MutableStateFlow("")
    val correctCountText: StateFlow<String> = _correctCountText.asStateFlow()


    private val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> = _error.asSharedFlow()

    init {
        val json = savedStateHandle.get<String>("result")
        if (json == null) {
            viewModelScope.launch { _error.emit("Savollar topilmadi") }
        }

        try {
            val type = object : TypeToken<List<QuestionData>>() {}.type
            val questionList: List<QuestionData> = Gson().fromJson(json, type)

            val resultList = checkQuizResult(questionList)
            _results.value = resultList

            val total = resultList.size
            val correct = resultList.count { it.isCorrect }
            _correctCountText.value = "$total savoldan $correct ta to`g`ri javob berdingiz."

        } catch (e: Exception) {
            viewModelScope.launch {
                _error.emit("Natijalarni yuklashda xatolik: ${e.localizedMessage}")
            }
        }
    }
}
