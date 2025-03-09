package org.deblock.flights.rest

import org.springframework.http.HttpStatus

data class ApiError(
    val code: HttpStatus,
    val message: String
)