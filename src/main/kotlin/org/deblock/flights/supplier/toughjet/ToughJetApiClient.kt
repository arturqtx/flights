package org.deblock.flights.supplier.toughjet

import org.deblock.flights.types.Airport
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.LocalDate

interface ToughJetApiClient {

    @GET("/v1/search")
    suspend fun search(
        @Query("from") from: Airport,
        @Query("to") to: Airport,
        @Query("outboundDate") outboundDate: LocalDate,
        @Query("inboundDate") inboundDate: LocalDate,
        @Query("numberOfAdults") numberOfAdults: Int
    ): Collection<ToughJetFlightResponse>
}