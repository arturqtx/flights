package org.deblock.flights.domain

import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.deblock.flights.FlightTestData
import org.deblock.flights.api.FlightSearchService
import org.deblock.flights.supplier.FlightSupplier
import org.deblock.flights.types.Airport.AMS
import org.deblock.flights.types.Airport.LAX
import org.deblock.flights.types.Money.Companion.money
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import java.time.LocalDate

class AggregateFlightSearchServiceTest : FlightTestData {

    private val supplierA = mock<FlightSupplier>()
    private val supplierB = mock<FlightSupplier>()
    private val supplierC = mock<FlightSupplier>()

    private val service = AggregateFlightSearchService(
        suppliers = listOf(supplierA, supplierB, supplierC)
    )

    @Test
    fun `returns flights from all suppliers sorted by price`(): Unit = runBlocking {
        // given
        val flight1 = aFlight(fare = money(1_00, "USD"))
        val flight2 = aFlight(fare = money(2_00, "EUR"))
        val flight3 = aFlight(fare = money(3_00, "GBP"))
        val flight4 = aFlight(fare = money(4_00, "USD"))
        val flight5 = aFlight(fare = money(5_00, "PLN"))
        val params = FlightSearchService.Params(
            origin = AMS,
            destination = LAX,
            departureDate = LocalDate.parse("2025-03-10"),
            returnDate = LocalDate.parse("2025-03-15"),
            passengers = 1
        )

        given(supplierA.search(params)).willReturn(listOf(flight1, flight3))
        given(supplierB.search(params)).willReturn(listOf(flight2, flight4, flight5))
        given(supplierC.search(params)).willReturn(emptyList())

        // when
        val result = service.search(params)

        // then
        assertThat(result).containsExactly(
            flight1,
            flight2,
            flight3,
            flight4,
            flight5
        )
    }

    @Test
    fun `returns nothing when all suppliers return nothing`(): Unit = runBlocking {
        // given
        given(supplierA.search(any())).willReturn(emptyList())
        given(supplierB.search(any())).willReturn(emptyList())
        given(supplierC.search(any())).willReturn(emptyList())

        // when
        val result = service.search(
            FlightSearchService.Params(
                origin = AMS,
                destination = LAX,
                departureDate = LocalDate.parse("2025-03-10"),
                returnDate = LocalDate.parse("2025-03-15"),
                passengers = 1
            )
        )

        // then
        assertThat(result).isEmpty()
    }
}