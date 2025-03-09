package org.deblock.flights.rest

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
@Suppress("unused")
class ExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(ex: HttpMessageNotReadableException): ResponseEntity<ApiError> {
        log.error("HttpMessageNotReadableException: ${ex.message}", ex)
        return ResponseEntity(
            ApiError(BAD_REQUEST, ex.cause?.message ?: "Bad request"),
            BAD_REQUEST
        )
    }

    @ExceptionHandler(Throwable::class)
    fun handleThrowable(ex: Throwable): ResponseEntity<ApiError> {
        log.error("Unhandled exception: ${ex.message}", ex)
        return ResponseEntity(
            ApiError(INTERNAL_SERVER_ERROR, "Something went wrong"),
            INTERNAL_SERVER_ERROR
        )
    }

    companion object {
        private val log = LoggerFactory.getLogger(ExceptionHandler::class.java)
    }
}
