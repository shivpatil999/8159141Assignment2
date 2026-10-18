package com.s8159141.assignment2.data.model

/** Body of GET /dashboard/{keypass}. */
data class DashboardResponse(
    val entities: List<Entity>,
    val entityTotal: Int
)
