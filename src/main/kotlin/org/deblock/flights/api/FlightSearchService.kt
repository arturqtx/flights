package org.deblock.flights.api

import org.deblock.flights.types.Airport
import java.time.LocalDate

interface FlightSearchService {

    data class Params(
        val origin: Airport,
        val destination: Airport,
        val departureDate: LocalDate,
        val returnDate: LocalDate,
        val passengers: Int
    )

    fun search(params: Params): Collection<Flight>
}