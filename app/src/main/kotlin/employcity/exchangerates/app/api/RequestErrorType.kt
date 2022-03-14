package employcity.exchangerates.app.api

enum class RequestErrorType {
    CONNECTION_ERROR,
    NO_INTERNET,
    BAD_REQUEST,
    NOT_AUTHORIZED,
    RATE_LIMIT_EXCEEDED,
    UNKNOWN_HTTP_ERROR,
    UNKNOWN_ERROR,
}