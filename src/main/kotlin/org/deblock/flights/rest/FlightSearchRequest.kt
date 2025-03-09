package org.deblock.flights.rest

import org.deblock.flights.types.Airport
import java.time.LocalDate

data class FlightSearchRequest(
    val origin: Airport,
    val destination: Airport,
    val departureDate: LocalDate,
    val returnDate: LocalDate,
    val numberOfPassengers: Int
) {
    init {
        check(numberOfPassengers in 1..4) { "Number of passengers must be between 1 and 4 but is [$numberOfPassengers]" }
    }
}