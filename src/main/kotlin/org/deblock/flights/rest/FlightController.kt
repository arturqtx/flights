package org.deblock.flights.rest

import org.deblock.flights.api.Flight
import org.deblock.flights.api.FlightSearchService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/flights")
class FlightController(
    @Qualifier("aggregateFlightSearchService") private val flightSearchService: FlightSearchService
) {
    @PostMapping("/search")
    fun search(@RequestBody request: FlightSearchRequest): Collection<FlightResponse> {
        val params = FlightSearchService.Params(
            origin = request.origin,
            destination = request.destination,
            departureDate = request.departureDate,
            returnDate = request.returnDate,
            passengers = request.numberOfPassengers
        )

        return flightSearchService.search(params)
            .map { toResponse(it) }
    }

    private fun toResponse(flight: Flight) = FlightResponse(
        airline = flight.airline.name,
        supplier = flight.supplier,
        fare = flight.fare.amount(),
        departureAirportCode = flight.origin,
        destinationAirportCode = flight.destination,
        departureDate = flight.departureAt,
        arrivalDate = flight.arrivalAt
    )
}