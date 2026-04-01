package com.giwon.emergencyroom.features.emergency.status.domain

import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface EmergencyStatusRepository : JpaRepository<EmergencyStatus, Long> {
    fun findByHospital_HospitalId(hospitalId: String): Optional<EmergencyStatus>
}

