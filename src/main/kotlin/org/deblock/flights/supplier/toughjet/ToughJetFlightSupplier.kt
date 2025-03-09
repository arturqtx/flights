package org.deblock.flights.supplier.toughjet

import org.deblock.flights.api.Flight
import org.deblock.flights.api.FlightSearchService
import org.deblock.flights.supplier.FlightSupplier
import org.deblock.flights.types.Airline
import org.deblock.flights.types.Money.Companion.money
import org.deblock.flights.types.Supplier.TOUGH_JET
import org.springframework.stereotype.Service
import java.math.RoundingMode.HALF_DOWN
import java.util.*

@Service
class ToughJetFlightSupplier(
    private val apiClient: ToughJetApiClient
) : FlightSupplier {
    override suspend fun search(params: FlightSearchService.Params): List<Flight> {
        return apiClient.search(
            from = params.origin,
            to = params.destination,
            outboundDate = params.departureDate,
            inboundDate = params.returnDate,
            numberOfAdults = params.passengers
        )
            .map {
                val currency = Currency.getInstance("USD") // CrazyAir base currency is always USD
                val discount = (it.basePrice * it.discount.movePointLeft(2)).setScale(currency.defaultFractionDigits, HALF_DOWN)
                val netAmount = it.basePrice + it.tax - discount

                Flight(
                    airline = Airline(it.carrier),
                    supplier = TOUGH_JET,
                    origin = it.departureAirportName,
                    destination = it.arrivalAirportName,
                    departureAt = it.outboundDateTime,
                    arrivalAt = it.inboundDateTime,
                    passengers = params.passengers,
                    fare = money(netAmount, currency)
                )
            }
    }
}