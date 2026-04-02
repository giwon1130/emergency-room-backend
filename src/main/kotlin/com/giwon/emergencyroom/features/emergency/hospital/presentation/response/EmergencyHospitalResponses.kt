package com.giwon.emergencyroom.features.emergency.hospital.presentation.response

import java.time.LocalDateTime

data class EmergencyHospitalResponse(
    val hospitalId: String,
    val name: String,
    val address: String?,
    val region: String?,
    val latitude: Double?,
    val longitude: Double?,
    val phone: String?,
    val emergencyPhone: String?
)

data class NearbyEmergencyHospitalResponse(
    val hospitalId: String,
    val name: String,
    val address: String?,
    val region: String?,
    val availableBeds: Int?,
    val emergencyStatus: String?,
    val lastUpdated: LocalDateTime?,
    val latitude: Double?,
    val longitude: Double?
)

data class EmergencyHospitalDetailResponse(
    val hospitalId: String,
    val name: String,
    val address: String?,
    val region: String?,
    val phone: String?,
    val emergencyPhone: String?,
    val availableBeds: Int,
    val emergencyStatus: String,
    val emergencyStatusLabel: String,
    val lastUpdated: LocalDateTime?,
    val updatedRecently: Boolean,
    val hasLocation: Boolean,
    val contactAvailable: Boolean,
    val latitude: Double?,
    val longitude: Double?
)
