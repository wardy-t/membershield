package com.tomward.membershield.assessment.api

import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.DateTimeException

@RestControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(
        IllegalArgumentException::class,
        DateTimeException::class,
    )
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleInvalidValue(
        exception: RuntimeException,
    ): ApiErrorResponse =
        ApiErrorResponse(
            code = "INVALID_REQUEST",
            message = exception.message ?: "The request contains an invalid value.",
        )

    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleUnreadableRequest(): ApiErrorResponse =
        ApiErrorResponse(
            code = "MALFORMED_REQUEST",
            message = "The request body is missing or malformed.",
        )
}

data class ApiErrorResponse(
    val code: String,
    val message: String,
)