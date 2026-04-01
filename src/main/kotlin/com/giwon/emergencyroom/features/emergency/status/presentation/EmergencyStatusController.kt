package com.giwon.emergencyroom.features.emergency.status.presentation

import com.giwon.emergencyroom.common.response.ApiResponse
import com.giwon.emergencyroom.features.emergency.status.application.EmergencyStatusService
import com.giwon.emergencyroom.features.emergency.status.presentation.response.EmergencyStatusResponse
import com.giwon.emergencyroom.features.emergency.status.presentation.response.EmergencyStatusSummaryResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/emergency/status")
class EmergencyStatusController(
    private val emergencyStatusService: EmergencyStatusService
) {
    @GetMapping("/{hospitalId}")
    fun getStatus(@PathVariable hospitalId: String): ApiResponse<EmergencyStatusResponse> {
        return ApiResponse.success(emergencyStatusService.getStatus(hospitalId))
    }

    @GetMapping("/summary")
    fun getSummary(): ApiResponse<EmergencyStatusSummaryResponse> {
        return ApiResponse.success(emergencyStatusService.getSummary())
    }
}

