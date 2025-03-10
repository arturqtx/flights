package org.deblock.flights.supplier.crazyair

import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.deblock.flights.api.Flight
import org.deblock.flights.api.FlightSearchService
import org.deblock.flights.supplier.crazyair.CrazyAirApiClient.CrazyAirFlight
import org.deblock.flights.types.Airline
import org.deblock.flights.types.Airport.*
import org.deblock.flights.types.Money.Companion.money
import org.deblock.flights.types.Supplier.CRAZY_AIR
import org.junit.jupiter.api.Test
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime

class CrazyAirFlightSupplierTest {

    private val apiClient = mock<CrazyAirApiClient>()

    private val supplier = CrazyAirFlightSupplier(
        apiClient = apiClient
    )

    @Test
    fun `requests flights search in Crazy Air API and converts the result`(): Unit = runBlocking {
        // given
        given(
            apiClient.flights(
                origin = AMS,
                destination = CDG,
                departureDate = LocalDate.parse("2025-03-01"),
                returnDate = LocalDate.parse("2025-03-07"),
                passengerCount = 1
            )
        ).willReturn(
            listOf(
                CrazyAirFlight(
                    airline = "CrazyAir",
                    price = BigDecimal("199.99"),
                    departureAirportCode = LHR,
                    destinationAirportCode = PEK,
                    departureDate = LocalDateTime.parse("2025-05-01T10:00:00"),
                    arrivalDate = LocalDateTime.parse("2025-05-10T12:30:00")
                ),
                CrazyAirFlight(
                    airline = "SkyFly",
                    price = BigDecimal("299.99"),
                    departureAirportCode = AMS,
                    destinationAirportCode = CDG,
                    departureDate = LocalDateTime.parse("2025-06-15T15:30:00"),
                    arrivalDate = LocalDateTime.parse("2025-06-20T18:00:00")
                ),
                CrazyAirFlight(
                    airline = "SuperJet",
                    price = BigDecimal("399.99"),
                    departureAirportCode = ORD,
                    destinationAirportCode = NYC,
                    departureDate = LocalDateTime.parse("2025-07-20T08:45:00"),
                    arrivalDate = LocalDateTime.parse("2025-08-20T11:15:00")
                )
            )
        )

        // when
        val result = supplier.search(
            FlightSearchService.Params(
                origin = AMS,
                destination = CDG,
                departureDate = LocalDate.parse("2025-03-01"),
                returnDate = LocalDate.parse("2025-03-07"),
                passengers = 1
            )
        )

        // then
        assertThat(result).containsExactly(
            Flight(
                airline = Airline("CrazyAir"),
                supplier = CRAZY_AIR,
                origin = LHR,
                destination = PEK,
                departureAt = Instant.parse("2025-05-01T09:00:00Z"),
                arrivalAt = Instant.parse("2025-05-10T04:30:00Z"),
                passengers = 1,
                fare = money(199_99, "USD")
            ),
            Flight(
                airline = Airline("SkyFly"),
                supplier = CRAZY_AIR,
                origin = AMS,
                destination = CDG,
                departureAt = Instant.parse("2025-06-15T13:30:00Z"),
                arrivalAt = Instant.parse("2025-06-20T16:00:00Z"),
                passengers = 1,
                fare = money(299_99, "USD")
            ),
            Flight(
                airline = Airline("SuperJet"),
                supplier = CRAZY_AIR,
                origin = ORD,
                destination = NYC,
                departureAt = Instant.parse("2025-07-20T13:45:00Z"),
                arrivalAt = Instant.parse("2025-08-20T15:15:00Z"),
                passengers = 1,
                fare = money(399_99, "USD")
            )
        )
    }
}