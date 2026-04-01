package com.giwon.emergencyroom.features.emergency.sync.infrastructure

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "emergency.api")
data class EmergencyApiProperties(
    var baseUrl: String = "",
    var serviceKey: String = ""
)

