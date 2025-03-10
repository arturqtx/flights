package org.deblock.flights.supplier.toughjet

import org.deblock.flights.types.Airport
import retrofit2.http.GET
import retrofit2.http.Query
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate

interface ToughJetApiClient {

    @GET("/v1/search")
    suspend fun search(
        @Query("from") from: Airport,
        @Query("to") to: Airport,
        @Query("outboundDate") outboundDate: LocalDate,
        @Query("inboundDate") inboundDate: LocalDate,
        @Query("numberOfAdults") numberOfAdults: Int
    ): Collection<ToughJetFlight>

    class ToughJetFlight(
        val carrier: String,
        val basePrice: BigDecimal,
        val tax: BigDecimal,
        val discount: BigDecimal,
        val departureAirportName: Airport,
        val arrivalAirportName: Airport,
        val outboundDateTime: Instant,
        val inboundDateTime: Instant
    )
}