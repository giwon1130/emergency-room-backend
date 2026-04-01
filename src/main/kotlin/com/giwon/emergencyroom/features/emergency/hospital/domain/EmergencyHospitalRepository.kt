package com.giwon.emergencyroom.features.emergency.hospital.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.Optional

interface EmergencyHospitalRepository : JpaRepository<EmergencyHospital, Long> {
    fun findByHospitalId(hospitalId: String): Optional<EmergencyHospital>
    fun findByRegion(region: String): List<EmergencyHospital>
    @Query("select h from EmergencyHospital h where lower(h.name) like lower(concat('%', :keyword, '%'))")
    fun searchByName(@Param("keyword") keyword: String): List<EmergencyHospital>

    @Query(
        value = """
        SELECT h.hospital_id, h.name, h.address,
               h.region,
               COALESCE(es.available_beds, 0),
               COALESCE(es.emergency_status, 'UNKNOWN'),
               es.last_updated,
               h.latitude, h.longitude
        FROM emergency_hospital h
        LEFT JOIN emergency_status es ON h.hospital_id = es.hospital_id
        WHERE ST_DWithin(
            CAST(h.geom AS geography),
            CAST(ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326) AS geography),
            :radius
        )
        ORDER BY COALESCE(es.available_beds, 0) DESC, h.name ASC
        """,
        nativeQuery = true
    )
    fun findHospitalsWithStatusWithinRadius(
        @Param("longitude") longitude: Double,
        @Param("latitude") latitude: Double,
        @Param("radius") radius: Double
    ): List<Array<Any?>>
}
