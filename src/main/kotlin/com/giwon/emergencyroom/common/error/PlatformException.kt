package com.giwon.emergencyroom.common.error

class PlatformException(
    val errorCode: ErrorCode
) : RuntimeException(errorCode.message)

