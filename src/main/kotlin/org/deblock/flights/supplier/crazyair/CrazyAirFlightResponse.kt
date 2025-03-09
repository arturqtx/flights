package org.deblock.flights.supplier.crazyair

import org.deblock.flights.types.Airport
import java.math.BigDecimal
import java.time.LocalDateTime

class CrazyAirFlightResponse(
    val airline: String,
    val price: BigDecimal,
    val departureAirportCode: Airport,
    val destinationAirportCode: Airport,
    val departureDate: LocalDateTime,
    val arrivalDate: LocalDateTime
)