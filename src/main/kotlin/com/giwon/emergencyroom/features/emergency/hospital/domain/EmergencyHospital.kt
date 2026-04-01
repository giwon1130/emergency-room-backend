package com.giwon.emergencyroom.features.emergency.hospital.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.locationtech.jts.geom.Point
import java.time.LocalDateTime

@Entity
@Table(name = "emergency_hospital")
class EmergencyHospital(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "hospital_id", nullable = false, unique = true)
    val hospitalId: String,

    @Column(nullable = false)
    val name: String,

    val address: String? = null,

    val region: String? = null,

    val latitude: Double? = null,

    val longitude: Double? = null,

    val phone: String? = null,

    @Column(name = "emergency_phone")
    val emergencyPhone: String? = null,

    @Column(name = "geom", columnDefinition = "geometry(Point,4326)")
    val geom: Point? = null,

    @Column(name = "updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
