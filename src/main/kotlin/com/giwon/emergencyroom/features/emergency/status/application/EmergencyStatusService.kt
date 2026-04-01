package com.giwon.emergencyroom.features.emergency.status.application

import com.giwon.emergencyroom.common.error.ErrorCode
import com.giwon.emergencyroom.common.error.PlatformException
import com.giwon.emergencyroom.features.emergency.hospital.domain.EmergencyHospital
import com.giwon.emergencyroom.features.emergency.status.domain.EmergencyStatusRepository
import com.giwon.emergencyroom.features.emergency.status.domain.EmergencyStatus
import com.giwon.emergencyroom.features.emergency.status.presentation.response.EmergencyStatusResponse
import com.giwon.emergencyroom.features.emergency.status.presentation.response.EmergencyStatusSummaryResponse
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class EmergencyStatusService(
    private val emergencyStatusRepository: EmergencyStatusRepository
) {
    @Cacheable(cacheNames = ["emergencyStatus"], key = "#hospitalId")
    fun getStatus(hospitalId: String): EmergencyStatusResponse {
        val status = emergencyStatusRepository.findByHospital_HospitalId(hospitalId)
            .orElseThrow { PlatformException(ErrorCode.STATUS_NOT_FOUND) }

        return EmergencyStatusResponse(
            hospitalId = status.hospital.hospitalId,
            hospitalName = status.hospital.name,
            availableBeds = status.availableBeds,
            emergencyStatus = status.emergencyStatus,
            lastUpdated = status.lastUpdated
        )
    }

    @Cacheable(cacheNames = ["emergencyStatusSummary"])
    fun getSummary(): EmergencyStatusSummaryResponse {
        val statuses = emergencyStatusRepository.findAll()
        return EmergencyStatusSummaryResponse(
            totalHospitals = statuses.size,
            greenCount = statuses.count { it.emergencyStatus == "GREEN" },
            redCount = statuses.count { it.emergencyStatus == "RED" },
            unknownCount = statuses.count { it.emergencyStatus == "UNKNOWN" },
            totalAvailableBeds = statuses.sumOf { it.availableBeds },
            latestStatusUpdatedAt = statuses.maxOfOrNull { it.lastUpdated }
        )
    }

    @Caching(
        put = [CachePut(cacheNames = ["emergencyStatus"], key = "#hospital.hospitalId")],
        evict = [CacheEvict(cacheNames = ["emergencyStatusSummary"], allEntries = true)]
    )
    fun saveOrUpdateStatus(
        hospital: EmergencyHospital,
        availableBeds: Int,
        emergencyStatus: String
    ): EmergencyStatusResponse {
        val existing = emergencyStatusRepository.findByHospital_HospitalId(hospital.hospitalId).orElse(null)
        val next = EmergencyStatus(
            id = existing?.id,
            hospital = hospital,
            availableBeds = availableBeds,
            emergencyStatus = emergencyStatus,
            lastUpdated = LocalDateTime.now()
        )
        val saved = emergencyStatusRepository.save(next)
        return EmergencyStatusResponse(
            hospitalId = saved.hospital.hospitalId,
            hospitalName = saved.hospital.name,
            availableBeds = saved.availableBeds,
            emergencyStatus = saved.emergencyStatus,
            lastUpdated = saved.lastUpdated
        )
    }

    @CacheEvict(cacheNames = ["emergencyStatusSummary"], allEntries = true)
    fun evictSummaryCache() {
    }
}
