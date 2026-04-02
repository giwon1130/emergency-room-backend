package com.giwon.emergencyroom.features.emergency.hospital.application

import com.giwon.emergencyroom.common.error.ErrorCode
import com.giwon.emergencyroom.common.error.PlatformException
import com.giwon.emergencyroom.features.emergency.hospital.domain.EmergencyHospital
import com.giwon.emergencyroom.features.emergency.hospital.domain.EmergencyHospitalRepository
import com.giwon.emergencyroom.features.emergency.hospital.presentation.response.EmergencyHospitalDetailResponse
import com.giwon.emergencyroom.features.emergency.hospital.presentation.response.EmergencyHospitalResponse
import com.giwon.emergencyroom.features.emergency.hospital.presentation.response.NearbyEmergencyHospitalResponse
import com.giwon.emergencyroom.features.emergency.status.domain.EmergencyStatusRepository
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.PrecisionModel
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.Duration
import java.time.LocalDateTime

@Service
class EmergencyHospitalService(
    private val emergencyHospitalRepository: EmergencyHospitalRepository,
    private val emergencyStatusRepository: EmergencyStatusRepository
) {
    private val geometryFactory = GeometryFactory(PrecisionModel(), 4326)

    fun getHospital(hospitalId: String): EmergencyHospitalDetailResponse {
        val hospital = emergencyHospitalRepository.findByHospitalId(hospitalId)
            .orElseThrow { PlatformException(ErrorCode.HOSPITAL_NOT_FOUND) }
        val status = emergencyStatusRepository.findByHospital_HospitalId(hospitalId).orElse(null)

        return EmergencyHospitalDetailResponse(
            hospitalId = hospital.hospitalId,
            name = hospital.name,
            address = hospital.address,
            region = hospital.region,
            phone = hospital.phone,
            emergencyPhone = hospital.emergencyPhone,
            availableBeds = status?.availableBeds ?: 0,
            emergencyStatus = status?.emergencyStatus ?: "UNKNOWN",
            emergencyStatusLabel = toStatusLabel(status?.emergencyStatus ?: "UNKNOWN"),
            lastUpdated = status?.lastUpdated,
            updatedRecently = status?.lastUpdated?.let { Duration.between(it, LocalDateTime.now()).toHours() < 6 } ?: false,
            hasLocation = hospital.latitude != null && hospital.longitude != null,
            contactAvailable = !hospital.phone.isNullOrBlank() || !hospital.emergencyPhone.isNullOrBlank(),
            latitude = hospital.latitude,
            longitude = hospital.longitude
        )
    }

    fun getNearbyHospitals(latitude: Double, longitude: Double, radius: Double): List<NearbyEmergencyHospitalResponse> {
        return emergencyHospitalRepository.findHospitalsWithStatusWithinRadius(longitude, latitude, radius)
            .map { row ->
                val lastUpdated = when (val value = row[6]) {
                    is Timestamp -> value.toLocalDateTime()
                    is LocalDateTime -> value
                    else -> null
                }

                NearbyEmergencyHospitalResponse(
                    hospitalId = row[0] as String,
                    name = row[1] as String,
                    address = row[2] as String?,
                    region = row[3] as String?,
                    availableBeds = (row[4] as Number?)?.toInt(),
                    emergencyStatus = row[5] as String?,
                    lastUpdated = lastUpdated,
                    latitude = (row[7] as Number?)?.toDouble(),
                    longitude = (row[8] as Number?)?.toDouble()
                )
            }
    }

    fun getHospitalsByRegion(region: String): List<EmergencyHospitalResponse> {
        return emergencyHospitalRepository.findByRegion(region).map(::toResponse)
    }

    fun searchHospitals(keyword: String): List<EmergencyHospitalResponse> {
        return emergencyHospitalRepository.searchByName(keyword).map(::toResponse)
    }

    fun saveHospital(
        hospitalId: String,
        name: String,
        address: String?,
        region: String?,
        latitude: Double?,
        longitude: Double?,
        phone: String?,
        emergencyPhone: String?
    ): EmergencyHospital {
        val point = if (latitude != null && longitude != null) {
            geometryFactory.createPoint(Coordinate(longitude, latitude))
        } else {
            null
        }

        val existing = emergencyHospitalRepository.findByHospitalId(hospitalId).orElse(null)
        val hospital = EmergencyHospital(
            id = existing?.id,
            hospitalId = hospitalId,
            name = name,
            address = address,
            region = region,
            latitude = latitude,
            longitude = longitude,
            phone = phone,
            emergencyPhone = emergencyPhone,
            geom = point
        )
        return emergencyHospitalRepository.save(hospital)
    }

    private fun toResponse(hospital: EmergencyHospital): EmergencyHospitalResponse {
        return EmergencyHospitalResponse(
            hospitalId = hospital.hospitalId,
            name = hospital.name,
            address = hospital.address,
            region = hospital.region,
            latitude = hospital.latitude,
            longitude = hospital.longitude,
            phone = hospital.phone,
            emergencyPhone = hospital.emergencyPhone
        )
    }

    private fun toStatusLabel(status: String): String =
        when (status) {
            "GREEN" -> "가용"
            "RED" -> "혼잡"
            else -> "미확인"
        }
}
