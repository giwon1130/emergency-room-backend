package com.giwon.emergencyroom.features.emergency.hospital.presentation

import com.giwon.emergencyroom.common.response.ApiResponse
import com.giwon.emergencyroom.features.emergency.hospital.application.EmergencyHospitalService
import com.giwon.emergencyroom.features.emergency.hospital.presentation.response.EmergencyHospitalDetailResponse
import com.giwon.emergencyroom.features.emergency.hospital.presentation.response.EmergencyHospitalResponse
import com.giwon.emergencyroom.features.emergency.hospital.presentation.response.NearbyEmergencyHospitalResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/emergency/hospitals")
class EmergencyHospitalController(
    private val emergencyHospitalService: EmergencyHospitalService
) {
    @GetMapping("/{hospitalId}")
    fun getHospital(@PathVariable hospitalId: String): ApiResponse<EmergencyHospitalDetailResponse> {
        return ApiResponse.success(emergencyHospitalService.getHospital(hospitalId))
    }

    @GetMapping("/nearby")
    fun getNearbyHospitals(
        @RequestParam latitude: Double,
        @RequestParam longitude: Double,
        @RequestParam(defaultValue = "5000") radius: Double
    ): ApiResponse<List<NearbyEmergencyHospitalResponse>> {
        return ApiResponse.success(emergencyHospitalService.getNearbyHospitals(latitude, longitude, radius))
    }

    @GetMapping("/region")
    fun getHospitalsByRegion(@RequestParam region: String): ApiResponse<List<EmergencyHospitalResponse>> {
        return ApiResponse.success(emergencyHospitalService.getHospitalsByRegion(region))
    }

    @GetMapping("/search")
    fun searchHospitals(@RequestParam keyword: String): ApiResponse<List<EmergencyHospitalResponse>> {
        return ApiResponse.success(emergencyHospitalService.searchHospitals(keyword))
    }
}
