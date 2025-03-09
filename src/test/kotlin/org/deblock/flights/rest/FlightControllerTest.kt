package org.deblock.flights.rest

import org.assertj.core.api.Assertions.assertThat
import org.deblock.flights.FlightTestData
import org.deblock.flights.api.FlightSearchService
import org.deblock.flights.types.Airline
import org.deblock.flights.types.Airport.AMS
import org.deblock.flights.types.Airport.LAX
import org.deblock.flights.types.Money.Companion.money
import org.deblock.flights.types.Supplier.CRAZY_AIR
import org.deblock.flights.types.Supplier.TOUGH_JET
import org.junit.jupiter.api.Test
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate

class FlightControllerTest : FlightTestData {

    private val flightSearchService = mock<FlightSearchService>()

    private val controller = FlightController(
        flightSearchService = flightSearchService
    )

    @Test
    fun `returns list of flights`() {
        // given
        val flight1 = aFlight(
            airline = Airline("The Airlines"),
            supplier = CRAZY_AIR,
            origin = AMS,
            destination = LAX,
            departureAt = Instant.parse("2025-04-01T10:00:00Z"),
            arrivalAt = Instant.parse("2025-04-15T14:00:00Z"),
            passengers = 1,
            fare = money(1000_99, "USD")
        )
        val flight2 = aFlight(
            airline = Airline("An Airlines"),
            supplier = TOUGH_JET,
            origin = AMS,
            destination = LAX,
            departureAt = Instant.parse("2025-04-01T08:00:00Z"),
            arrivalAt = Instant.parse("2025-04-15T18:00:59.999Z"),
            passengers = 3,
            fare = money(1300_00, "EUR")
        )

        given(flightSearchService.search(
            FlightSearchService.Params(
                origin = AMS,
                destination = LAX,
                departureDate = LocalDate.parse("2025-04-01"),
                returnDate = LocalDate.parse("2025-04-15"),
                passengers = 1
            )
        )).willReturn(listOf(flight1, flight2))

        // when
        val result = controller.search(
            FlightSearchRequest(
                origin = AMS,
                destination = LAX,
                departureDate = LocalDate.parse("2025-04-01"),
                returnDate = LocalDate.parse("2025-04-15"),
                numberOfPassengers = 1
        ))

        // then
        assertThat(result).containsExactly(
            FlightResponse(
                airline = "The Airlines",
                supplier = CRAZY_AIR,
                fare = BigDecimal("1000.99"),
                departureAirportCode = AMS,
                destinationAirportCode = LAX,
                departureDate = Instant.parse("2025-04-01T10:00:00Z"),
                arrivalDate = Instant.parse("2025-04-15T14:00:00Z")
            ),
            FlightResponse(
                airline = "An Airlines",
                supplier = TOUGH_JET,
                fare = BigDecimal("1300.00"),
                departureAirportCode = AMS,
                destinationAirportCode = LAX,
                departureDate = Instant.parse("2025-04-01T08:00:00Z"),
                arrivalDate = Instant.parse("2025-04-15T18:00:59.999Z")
            )
        )
    }
}