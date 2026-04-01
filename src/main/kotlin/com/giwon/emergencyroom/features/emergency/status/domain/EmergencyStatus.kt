package com.giwon.emergencyroom.features.emergency.status.domain

import com.giwon.emergencyroom.features.emergency.hospital.domain.EmergencyHospital
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "emergency_status")
class EmergencyStatus(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", referencedColumnName = "hospital_id", nullable = false)
    val hospital: EmergencyHospital,

    @Column(name = "available_beds", nullable = false)
    val availableBeds: Int = 0,

    @Column(name = "emergency_status", nullable = false)
    val emergencyStatus: String = "GREEN",

    @Column(name = "last_updated", nullable = false)
    val lastUpdated: LocalDateTime = LocalDateTime.now()
)

