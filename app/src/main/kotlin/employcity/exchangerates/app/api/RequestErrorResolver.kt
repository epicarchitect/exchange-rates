package employcity.exchangerates.app.api

import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class RequestErrorResolver {

    fun resolve(throwable: Throwable) = when (throwable) {
        is SocketTimeoutException -> {
            RequestErrorType.CONNECTION_ERROR
        }
        is ConnectException, is UnknownHostException -> {
            RequestErrorType.NO_INTERNET
        }
        is HttpException -> when (throwable.code()) {
            400 -> {
                RequestErrorType.BAD_REQUEST
            }
            401 -> {
                RequestErrorType.NOT_AUTHORIZED
            }
            429 -> {
                RequestErrorType.RATE_LIMIT_EXCEEDED
            }

            else -> RequestErrorType.UNKNOWN_HTTP_ERROR
        }

        else -> RequestErrorType.UNKNOWN_ERROR
    }
}