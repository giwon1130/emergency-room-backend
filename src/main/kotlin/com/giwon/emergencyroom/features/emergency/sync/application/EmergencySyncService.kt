package com.giwon.emergencyroom.features.emergency.sync.application

import com.giwon.emergencyroom.features.emergency.hospital.application.EmergencyHospitalService
import com.giwon.emergencyroom.features.emergency.sync.infrastructure.EmergencyApiClient
import com.giwon.emergencyroom.features.emergency.status.application.EmergencyStatusService
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Service

@Service
class EmergencySyncService(
    private val emergencyApiClient: EmergencyApiClient,
    private val emergencyHospitalService: EmergencyHospitalService,
    private val emergencyStatusService: EmergencyStatusService
) {
    fun initializeHospitals(): String {
        val items = emergencyApiClient.fetchItems("/getEgytListInfoInqire")
        if (items.isEmpty()) {
            return "병원 초기 적재 대상이 없습니다."
        }

        items.forEach { item ->
            emergencyHospitalService.saveHospital(
                hospitalId = item.requiredText("hpid") ?: return@forEach,
                name = item.requiredText("dutyName") ?: "알 수 없는 병원",
                address = item.optionalText("dutyAddr"),
                region = extractRegion(item.optionalText("dutyAddr")),
                latitude = item.optionalDouble("wgs84Lat"),
                longitude = item.optionalDouble("wgs84Lon"),
                phone = item.optionalText("dutyTel1"),
                emergencyPhone = item.optionalText("dutyTel3")
            )
        }
        return "병원 초기 적재가 완료되었습니다."
    }

    fun initializeStatuses(): String {
        return updateStatusesFromApi("응급실 상태 초기 적재가 완료되었습니다.")
    }

    fun refreshStatuses(): String {
        return updateStatusesFromApi("응급실 상태 최신화가 완료되었습니다.")
    }

    private fun updateStatusesFromApi(successMessage: String): String {
        val items = emergencyApiClient.fetchItems("/getEmrrmRltmUsefulSckbdInfoInqire")
        if (items.isEmpty()) {
            return "응급실 상태 적재 대상이 없습니다."
        }

        items.forEach { item ->
            val hospitalId = item.requiredText("hpid") ?: return@forEach
            val hospital = emergencyHospitalService.saveHospital(
                hospitalId = hospitalId,
                name = item.requiredText("dutyName") ?: "알 수 없는 병원",
                address = item.optionalText("dutyAddr"),
                region = extractRegion(item.optionalText("dutyAddr")),
                latitude = item.optionalDouble("wgs84Lat"),
                longitude = item.optionalDouble("wgs84Lon"),
                phone = item.optionalText("dutyTel1"),
                emergencyPhone = item.optionalText("dutyTel3")
            )
            val availableBeds = maxOf(item.path("hvec").asInt(0), 0)
            val status = determineEmergencyStatus(item, availableBeds)
            emergencyStatusService.saveOrUpdateStatus(hospital, availableBeds, status)
        }
        return successMessage
    }

    private fun extractRegion(address: String?): String? {
        if (address.isNullOrBlank()) return null
        return address.split(" ").firstOrNull()
    }

    private fun determineEmergencyStatus(item: JsonNode, availableBeds: Int): String {
        val rawStatus = item.optionalText("hvamyn")
            ?: item.optionalText("dutyEryn")
            ?: item.optionalText("dutyErynName")

        if (rawStatus.isNullOrBlank()) {
            return if (availableBeds > 0) "GREEN" else "RED"
        }

        return when {
            rawStatus.contains("불가") || rawStatus.contains("포화") || rawStatus.contains("불능") -> "RED"
            rawStatus.contains("가능") || rawStatus.contains("원활") -> "GREEN"
            else -> if (availableBeds > 0) "GREEN" else "UNKNOWN"
        }
    }

    private fun JsonNode.optionalText(fieldName: String): String? {
        val value = path(fieldName).asText().trim()
        return value.ifBlank { null }
    }

    private fun JsonNode.requiredText(fieldName: String): String? {
        return optionalText(fieldName)
    }

    private fun JsonNode.optionalDouble(fieldName: String): Double? {
        val text = optionalText(fieldName) ?: return null
        return text.toDoubleOrNull()
    }
}
