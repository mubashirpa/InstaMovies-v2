package app.instamovies.core.util

sealed class Resource<T>(val data: T? = null, val message: UiText? = null) {
    class Empty<T> : Resource<T>()

    class Error<T>(message: UiText, data: T? = null) : Resource<T>(data, message)

    class Loading<T>(data: T? = null) : Resource<T>(data)

    class Success<T>(data: T?) : Resource<T>(data)
}
