package com.giwon.emergencyroom.common.error

import com.giwon.emergencyroom.common.response.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(PlatformException::class)
    fun handlePlatformException(exception: PlatformException): ResponseEntity<ApiResponse<Map<String, String>>> {
        val body = ApiResponse(
            status = "FAIL",
            message = exception.errorCode.message,
            data = mapOf("code" to exception.errorCode.code)
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body)
    }
}

