package com.practice.spring.api.common

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiExceptionHandler {
    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ApiResponse<Unit>> {
        logger.warn(e) { "Unexpected exception: ${e.message}" }
        return ResponseEntity(ApiResponse.error(e.message), HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
