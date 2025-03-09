package org.deblock.flights.api

import org.deblock.flights.types.Airline
import org.deblock.flights.types.Airport
import org.deblock.flights.types.Money
import org.deblock.flights.types.Supplier
import java.time.Instant

data class Flight(
    val airline: Airline,
    val supplier: Supplier,
    val origin: Airport,
    val destination: Airport,
    val departureAt: Instant,
    val arrivalAt: Instant,
    val passengers: Int,
    val fare: Money
) {
    init {
        check(origin != destination) { "Origin [$origin] must not be equal to Destination [$destination]" }
        check(passengers > 0) { "Passengers must be positive but is [$passengers]" }
    }
}