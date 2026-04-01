package com.giwon.emergencyroom.features.emergency.sync.presentation

import com.giwon.emergencyroom.common.response.ApiResponse
import com.giwon.emergencyroom.features.emergency.sync.application.EmergencySyncService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/emergency")
class EmergencySyncController(
    private val emergencySyncService: EmergencySyncService
) {
    @PostMapping("/hospitals/initialize")
    fun initializeHospitals(): ApiResponse<String> {
        return ApiResponse.success(emergencySyncService.initializeHospitals())
    }

    @PostMapping("/status/initialize")
    fun initializeStatuses(): ApiResponse<String> {
        return ApiResponse.success(emergencySyncService.initializeStatuses())
    }

    @PostMapping("/status/refresh")
    fun refreshStatuses(): ApiResponse<String> {
        return ApiResponse.success(emergencySyncService.refreshStatuses())
    }
}

