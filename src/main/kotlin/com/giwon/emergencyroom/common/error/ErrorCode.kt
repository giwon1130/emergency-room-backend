package com.giwon.emergencyroom.common.error

enum class ErrorCode(
    val code: String,
    val message: String
) {
    HOSPITAL_NOT_FOUND("HOSPITAL_NOT_FOUND", "병원 정보를 찾을 수 없습니다."),
    STATUS_NOT_FOUND("STATUS_NOT_FOUND", "응급실 상태 정보를 찾을 수 없습니다.")
}

