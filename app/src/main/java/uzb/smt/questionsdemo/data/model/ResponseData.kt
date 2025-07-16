package uzb.smt.questionsdemo.data.model

sealed class ResponseData<T> {
    data class Success<T>(val data: T) : ResponseData<T>()
    data class Error<T>(val error: String) : ResponseData<T>()
}
