package org.deblock.flights.supplier.crazyair

import org.deblock.flights.types.Airport
import retrofit2.http.GET
import retrofit2.http.Query
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

interface CrazyAirApiClient {

    @GET("/api/flights/search")
    suspend fun flights(
        @Query("origin") origin: Airport,
        @Query("destination") destination: Airport,
        @Query("departureDate") departureDate: LocalDate,
        @Query("returnDate") returnDate: LocalDate,
        @Query("passengerCount") passengerCount: Int
    ): Collection<CrazyAirFlight>

    class CrazyAirFlight(
        val airline: String,
        val price: BigDecimal,
        val departureAirportCode: Airport,
        val destinationAirportCode: Airport,
        val departureDate: LocalDateTime,
        val arrivalDate: LocalDateTime
    )
}
