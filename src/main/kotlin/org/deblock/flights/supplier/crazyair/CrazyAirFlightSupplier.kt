package org.deblock.flights.supplier.crazyair

import org.deblock.flights.api.Flight
import org.deblock.flights.api.FlightSearchService
import org.deblock.flights.supplier.FlightSupplier
import org.deblock.flights.types.Airline
import org.deblock.flights.types.Money.Companion.money
import org.deblock.flights.types.Supplier.CRAZY_AIR
import org.springframework.stereotype.Service

@Service
class CrazyAirFlightSupplier(
    private val apiClient: CrazyAirApiClient
): FlightSupplier {

    override suspend fun search(params: FlightSearchService.Params): List<Flight> {
        return apiClient.flights(
            origin = params.origin,
            destination = params.destination,
            departureDate = params.departureDate,
            returnDate = params.returnDate,
            passengerCount = params.passengers
        )
            .map {
                Flight(
                    airline = Airline(it.airline),
                    supplier = CRAZY_AIR,
                    origin = it.departureAirportCode,
                    destination = it.destinationAirportCode,
                    departureAt = it.departureDate.atZone(it.departureAirportCode.zoneId).toInstant(),
                    arrivalAt = it.arrivalDate.atZone(it.destinationAirportCode.zoneId).toInstant(),
                    passengers = params.passengers,
                    // CrazyAir base currency is always USD
                    fare = money(it.price, "USD")
                )
            }
    }
}