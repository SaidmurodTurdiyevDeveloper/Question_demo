package uzb.smt.questionsdemo.presenter.screen.questions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uzb.smt.questionsdemo.data.model.ResponseData
import uzb.smt.questionsdemo.domen.model.QuestionData
import uzb.smt.questionsdemo.domen.usecase.CheckQuizFinish
import uzb.smt.questionsdemo.domen.usecase.DeleteQuestion
import uzb.smt.questionsdemo.domen.usecase.GetQuestions
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val getQuestions: GetQuestions,
    private val deleteQuestions: DeleteQuestion,
    private val checkQuizFinish: CheckQuizFinish
) : ViewModel() {
    private val _questions = MutableStateFlow<List<QuestionData>>(emptyList())
    val questions = _questions.asStateFlow()
    private val _isActive = MutableStateFlow<Boolean>(false)
    val isActive = _isActive.asStateFlow()
    private val _deleteQuestion = MutableStateFlow<QuestionData?>(null)
    val deleteQuestion = _deleteQuestion.asStateFlow()

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage = _errorMessage.asSharedFlow()

    private val _openResult = MutableSharedFlow<String>()
    val openResult = _openResult.asSharedFlow()

    init {
        _questions.onEach {
            if (it.isNotEmpty()) {
                _isActive.value = checkQuizFinish(_questions.value)
            }
        }.launchIn(viewModelScope)
    }

    fun loadQuestions() {
        viewModelScope.launch {
            getQuestions().collectLatest { result ->
                when (result) {
                    is ResponseData.Success -> {
                        _questions.value = result.data
                    }

                    is ResponseData.Error -> {
                        _errorMessage.emit(result.error)
                    }
                }
            }
        }
    }

    fun deleteQuestion(deleteData: QuestionData?) {
        if (deleteData == null)
            return
        viewModelScope.launch {
            deleteQuestions(deleteData).collectLatest { result ->
                when (result) {
                    is ResponseData.Success -> {
                        _deleteQuestion.value = null
                        val ls = _questions.value.toMutableList()
                        ls.remove(deleteData)
                        _questions.value = ls
                    }

                    is ResponseData.Error -> {
                        _errorMessage.emit(result.error)
                    }
                }
            }
        }
    }
    fun dismissDelete() {
        _deleteQuestion.value = null
    }

    fun openDeleteQuestion(daletItem: QuestionData) {
        viewModelScope.launch {
            if (_deleteQuestion.value == null) {
            _deleteQuestion.emit(daletItem)
        }
        }
    }


    fun openResult() {
        val isFinish = checkQuizFinish(_questions.value)
        if (isFinish) {
            val jsonString = Gson().toJson(_questions.value)
            viewModelScope.launch {
                _openResult.emit(jsonString)
            }
        }
    }

    fun changeQuestionOption(questionData: QuestionData, position: Int) {
        val ls = _questions.value.toMutableList()
        ls[position] = questionData
        _questions.value = ls
    }
}