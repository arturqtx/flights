package org.deblock.flights.rest

import org.deblock.flights.types.Airport
import org.deblock.flights.types.Supplier
import java.math.BigDecimal
import java.time.Instant

data class FlightResponse(
    val airline: String,
    val supplier: Supplier,
    val fare: BigDecimal,
    val departureAirportCode: Airport,
    val destinationAirportCode: Airport,
    val departureDate: Instant,
    val arrivalDate: Instant
)