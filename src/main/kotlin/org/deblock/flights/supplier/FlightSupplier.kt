package org.deblock.flights.supplier

import org.deblock.flights.api.Flight
import org.deblock.flights.api.FlightSearchService

interface FlightSupplier {
    suspend fun search(params: FlightSearchService.Params): Collection<Flight>
}