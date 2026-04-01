package com.giwon.emergencyroom.common.response

data class ApiResponse<T>(
    val status: String = "SUCCESS",
    val message: String = "요청이 정상 처리되었습니다.",
    val data: T? = null
) {
    companion object {
        fun <T> success(data: T? = null, message: String = "요청이 정상 처리되었습니다."): ApiResponse<T> {
            return ApiResponse(data = data, message = message)
        }
    }
}

