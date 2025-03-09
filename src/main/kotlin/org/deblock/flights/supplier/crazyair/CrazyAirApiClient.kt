package org.deblock.flights.supplier.crazyair

import org.deblock.flights.types.Airport
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.LocalDate

interface CrazyAirApiClient {

    @GET("/api/flights/search")
    suspend fun flights(
        @Query("origin") origin: Airport,
        @Query("destination") destination: Airport,
        @Query("departureDate") departureDate: LocalDate,
        @Query("returnDate") returnDate: LocalDate,
        @Query("passengerCount") passengerCount: Int
    ): Collection<CrazyAirFlightResponse>
}