package com.giwon.emergencyroom

import com.giwon.emergencyroom.features.emergency.sync.infrastructure.EmergencyApiProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
@EnableConfigurationProperties(EmergencyApiProperties::class)
class EmergencyRoomApplication

fun main(args: Array<String>) {
    runApplication<EmergencyRoomApplication>(*args)
}
