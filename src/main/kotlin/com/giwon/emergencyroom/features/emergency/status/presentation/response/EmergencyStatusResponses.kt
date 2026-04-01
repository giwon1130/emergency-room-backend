package com.giwon.emergencyroom.features.emergency.status.presentation.response

import java.time.LocalDateTime

data class EmergencyStatusResponse(
    val hospitalId: String,
    val hospitalName: String,
    val availableBeds: Int,
    val emergencyStatus: String,
    val lastUpdated: LocalDateTime
)

data class EmergencyStatusSummaryResponse(
    val totalHospitals: Int,
    val greenCount: Int,
    val redCount: Int,
    val unknownCount: Int,
    val totalAvailableBeds: Int,
    val latestStatusUpdatedAt: LocalDateTime?
)
