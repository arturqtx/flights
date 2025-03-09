package org.deblock.flights

import org.deblock.flights.api.Flight
import org.deblock.flights.types.Airline
import org.deblock.flights.types.Airport
import org.deblock.flights.types.Airport.AMS
import org.deblock.flights.types.Airport.LAX
import org.deblock.flights.types.Money
import org.deblock.flights.types.Money.Companion.money
import org.deblock.flights.types.Supplier
import org.deblock.flights.types.Supplier.CRAZY_AIR
import java.time.Instant

interface FlightTestData {
    fun aFlight(
        airline: Airline = Airline("The Airlines"),
        supplier: Supplier = CRAZY_AIR,
        origin: Airport = AMS,
        destination: Airport = LAX,
        departureAt: Instant = Instant.parse("2025-04-01T10:00:00Z"),
        arrivalAt: Instant = Instant.parse("2025-04-15T10:00:00Z"),
        passengers: Int = 1,
        fare: Money = money(100_00, "USD")
    ) = Flight(
        airline = airline,
        supplier = supplier,
        origin = origin,
        destination = destination,
        departureAt = departureAt,
        arrivalAt = arrivalAt,
        passengers = passengers,
        fare = fare
    )
}