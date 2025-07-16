package uzb.smt.questionsdemo.presenter.screen.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uzb.smt.questionsdemo.data.model.ResponseData
import uzb.smt.questionsdemo.domen.model.QuestionType
import uzb.smt.questionsdemo.domen.model.create.CreateOptionData
import uzb.smt.questionsdemo.domen.model.create.CreateQuestionData
import uzb.smt.questionsdemo.domen.usecase.CheckCanCreateQuestion
import uzb.smt.questionsdemo.domen.usecase.CreateQuestion
import javax.inject.Inject

@HiltViewModel
class CreateQuestionViewModel @Inject constructor(
    private val createQuestion: CreateQuestion,
    private val checkCanCreateQuestion: CheckCanCreateQuestion
) : ViewModel() {
    private val _questionText = MutableStateFlow("")
    val questionText: StateFlow<String> = _questionText.asStateFlow()
    private val _options = MutableStateFlow<List<CreateOptionData>>(emptyList())
    val options: StateFlow<List<CreateOptionData>> = _options.asStateFlow()
    private val _type = MutableStateFlow(QuestionType.Radio)
    val type: StateFlow<QuestionType> = _type.asStateFlow()

    private val _showDialog = MutableSharedFlow<Boolean>()
    val showDialog: SharedFlow<Boolean> = _showDialog.asSharedFlow()

    private val _success = MutableSharedFlow<Boolean>()
    val success: SharedFlow<Boolean> = _success.asSharedFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    init {
        _options.debounce(700).onEach {
            val createData = createQuestionData(option = it)
            val result = checkCanCreateQuestion(createData)
            _errorMessage.value = result
        }.launchIn(viewModelScope)

        _type.debounce(300).onEach {
            _options.value = emptyList()
            val createData = createQuestionData(currentType = it)
            val result = checkCanCreateQuestion(createData)
            _errorMessage.value = result
        }.launchIn(viewModelScope)

        _questionText.debounce(700).onEach {
            val createData = createQuestionData(text = it)
            val result = checkCanCreateQuestion(createData)
            _errorMessage.value = result
        }.launchIn(viewModelScope)

    }

    private fun createQuestionData(
        text: String? = null,
        currentType: QuestionType? = null,
        option: List<CreateOptionData>? = null
    ): CreateQuestionData {
        return CreateQuestionData(
            question = text ?: questionText.value,
            options = option ?: _options.value,
            type = currentType ?: type.value
        )
    }

    fun selectType(type: QuestionType) {
        if (type == _type.value) {
            return
        }
        _type.value = type

    }

    fun changeQuestionText(text: String) {
        _questionText.value = text
    }


    fun saveQuestion() {
        if (_errorMessage.value != null) {
            viewModelScope.launch {
                _showDialog.emit(true)
            }
            return
        }
        val createData = createQuestionData()
        viewModelScope.launch {
            createQuestion(createData).collectLatest { result ->
                when (result) {
                    is ResponseData.Success -> {
                        _success.emit(true)
                    }

                    is ResponseData.Error -> {
                        _success.emit(false)
                    }
                }
            }
        }
    }

    fun addQuestionOption(option: CreateOptionData) {
        if (type.value == QuestionType.Input) {
            _options.value = listOf(option)
        } else {
            val ls = _options.value.toMutableList()
            ls.add(option)
            _options.value = ls
        }
    }

    fun selectCorrectOption(option: CreateOptionData, index: Int) {

        if (type.value != QuestionType.CheckBox) {
            val ls = _options.value.map { it.copy(isCorrect = false) }.toMutableList()
            ls[index] = option
            _options.value = ls
        } else {
            val ls = _options.value.toMutableList()
            ls[index] = option
            _options.value = ls
        }
    }

    fun deleteOption(option: CreateOptionData) {
        val ls = _options.value.toMutableList()
        ls.remove(option)
        _options.value = ls
    }
}