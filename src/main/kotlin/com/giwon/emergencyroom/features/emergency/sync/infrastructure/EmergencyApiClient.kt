package com.giwon.emergencyroom.features.emergency.sync.infrastructure

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import kotlin.math.ceil

@Component
class EmergencyApiClient(
    private val restTemplate: RestTemplate,
    private val objectMapper: ObjectMapper,
    private val emergencyApiProperties: EmergencyApiProperties
) {
    fun fetchItems(endpoint: String, numOfRows: Int = 100): List<JsonNode> {
        if (emergencyApiProperties.baseUrl.isBlank() || emergencyApiProperties.serviceKey.isBlank()) {
            return emptyList()
        }

        val firstBody = fetchBody(endpoint, 1, numOfRows) ?: return emptyList()
        val firstItems = extractItems(firstBody)
        val totalCount = firstBody.path("totalCount").asInt(firstItems.size)
        val totalPages = maxOf(1, ceil(totalCount / numOfRows.toDouble()).toInt())

        if (totalPages == 1) {
            return firstItems
        }

        return buildList {
            addAll(firstItems)
            for (pageNo in 2..totalPages) {
                val body = fetchBody(endpoint, pageNo, numOfRows) ?: continue
                addAll(extractItems(body))
            }
        }
    }

    private fun fetchBody(endpoint: String, pageNo: Int, numOfRows: Int): JsonNode? {
        val url = buildUrl(endpoint, pageNo, numOfRows)
        val response = restTemplate.getForObject(url, String::class.java) ?: return null
        return objectMapper.readTree(response).path("response").path("body")
    }

    private fun extractItems(body: JsonNode): List<JsonNode> {
        val itemNode = body.path("items").path("item")
        return when {
            itemNode.isArray -> itemNode.toList()
            itemNode.isObject -> listOf(itemNode)
            else -> emptyList()
        }
    }

    private fun buildUrl(endpoint: String, pageNo: Int, numOfRows: Int): String {
        return UriComponentsBuilder
            .fromHttpUrl(emergencyApiProperties.baseUrl)
            .path(endpoint)
            .queryParam("pageNo", pageNo)
            .queryParam("numOfRows", numOfRows)
            .queryParam("serviceKey", emergencyApiProperties.serviceKey)
            .queryParam("_type", "json")
            .build(false)
            .toUriString()
    }
}
