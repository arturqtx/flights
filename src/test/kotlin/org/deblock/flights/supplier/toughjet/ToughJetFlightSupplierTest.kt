package org.deblock.flights.supplier.toughjet

import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.deblock.flights.api.Flight
import org.deblock.flights.api.FlightSearchService
import org.deblock.flights.supplier.toughjet.ToughJetApiClient.ToughJetFlight
import org.deblock.flights.types.Airline
import org.deblock.flights.types.Airport.*
import org.deblock.flights.types.Money.Companion.money
import org.deblock.flights.types.Supplier.TOUGH_JET
import org.junit.jupiter.api.Test
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate

class ToughJetFlightSupplierTest {
    private val apiClient = mock<ToughJetApiClient>()

    private val supplier = ToughJetFlightSupplier(
        apiClient = apiClient
    )

    @Test
    fun `requests flights search in Tough Jet API and converts the result`(): Unit = runBlocking {
        // given
        given(
            apiClient.search(
                from = AMS,
                to = CDG,
                outboundDate = LocalDate.parse("2025-03-01"),
                inboundDate = LocalDate.parse("2025-03-07"),
                numberOfAdults = 1
            )
        ).willReturn(
            listOf(
                ToughJetFlight(
                    carrier = "CrazyAir",
                    basePrice = BigDecimal("199.99"),
                    tax = BigDecimal("50.00"),
                    discount = BigDecimal("25"),
                    departureAirportName = LHR,
                    arrivalAirportName = PEK,
                    outboundDateTime = Instant.parse("2025-05-01T09:00:00Z"),
                    inboundDateTime = Instant.parse("2025-05-10T04:30:00Z")
                ),
                ToughJetFlight(
                    carrier = "SkyFly",
                    basePrice = BigDecimal("299.99"),
                    tax = BigDecimal("10.99"),
                    discount = BigDecimal("10"),
                    departureAirportName = AMS,
                    arrivalAirportName = CDG,
                    outboundDateTime = Instant.parse("2025-05-01T10:00:00Z"),
                    inboundDateTime = Instant.parse("2025-05-10T12:30:00Z")
                ),
                ToughJetFlight(
                    carrier = "SuperJet",
                    basePrice = BigDecimal("399.99"),
                    tax = BigDecimal("0"),
                    discount = BigDecimal("0"),
                    departureAirportName = ORD,
                    arrivalAirportName = NYC,
                    outboundDateTime = Instant.parse("2025-07-20T13:45:00Z"),
                    inboundDateTime = Instant.parse("2025-08-20T15:15:00Z")
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
                supplier = TOUGH_JET,
                origin = LHR,
                destination = PEK,
                departureAt = Instant.parse("2025-05-01T09:00:00Z"),
                arrivalAt = Instant.parse("2025-05-10T04:30:00Z"),
                passengers = 1,
                fare = money(199_99, "USD")
            ),
            Flight(
                airline = Airline("SkyFly"),
                supplier = TOUGH_JET,
                origin = AMS,
                destination = CDG,
                departureAt = Instant.parse("2025-05-01T10:00:00Z"),
                arrivalAt = Instant.parse("2025-05-10T12:30:00Z"),
                passengers = 1,
                fare = money(280_98, "USD")
            ),
            Flight(
                airline = Airline("SuperJet"),
                supplier = TOUGH_JET,
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