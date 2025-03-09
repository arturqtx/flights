package org.deblock.flights.domain

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.deblock.flights.api.Flight
import org.deblock.flights.api.FlightSearchService
import org.deblock.flights.supplier.FlightSupplier
import org.springframework.stereotype.Service

@Service("aggregateFlightSearchService")
class AggregateFlightSearchService(
    private val suppliers: Collection<FlightSupplier>
) : FlightSearchService {

    override fun search(params: FlightSearchService.Params): Collection<Flight> = runBlocking {
        suppliers
            .map { async { it.search(params) } }
            .awaitAll()
            .flatten()
            // TODO: Respect exchange rate of different currencies
            .sortedBy { it.fare.minorAmount }
    }
}