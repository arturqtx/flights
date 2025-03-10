package org.deblock.flights.spec

import com.github.tomakehurst.wiremock.client.WireMock.*
import net.javacrumbs.jsonunit.assertj.assertThatJson
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.http.HttpStatus.*

class FlightSearchSpec : FunctionalSpec() {

    @Test
    fun `searches and aggregates flights from the supplier APIs`() {
        // given
        // @formatter:off
        // Stub Crazy Air API
        stubFor(get(urlPathEqualTo("/api/flights/search"))
            .withQueryParam("origin", equalTo("AMS"))
            .withQueryParam("destination", equalTo("DXB"))
            .withQueryParam("departureDate", equalTo("2025-03-10"))
            .withQueryParam("returnDate", equalTo("2025-03-15"))
            .withQueryParam("passengerCount", equalTo("4"))
            .willReturn(ok("""
                [
                  {
                    "airline": "Air France",
                    "price": 299.99,
                    "cabinclass": "E",
                    "departureAirportCode": "AMS",
                    "destinationAirportCode": "DXB",
                    "departureDate": "2025-03-10T08:30:00",
                    "arrivalDate": "2025-03-15T10:45:00"
                  },
                  {
                    "airline": "KLM",
                    "price": 400.25,
                    "cabinclass": "E",
                    "departureAirportCode": "AMS",
                    "destinationAirportCode": "DXB",
                    "departureDate": "2025-03-10T10:30:00",
                    "arrivalDate": "2025-03-15T19:20:00"
                  },
                  {
                    "airline": "EasyJet",
                    "price": 80.00,
                    "cabinclass": "B",
                    "departureAirportCode": "AMS",
                    "destinationAirportCode": "DXB",
                    "departureDate": "2025-03-10T04:30:00",
                    "arrivalDate": "2025-03-15T04:20:00"
                  }
                ]    
                """.trimIndent())))
        // Stub Tough Jet API
        stubFor(get(urlPathEqualTo("/v1/search"))
            .withQueryParam("from", equalTo("AMS"))
            .withQueryParam("to", equalTo("DXB"))
            .withQueryParam("outboundDate", equalTo("2025-03-10"))
            .withQueryParam("inboundDate", equalTo("2025-03-15"))
            .withQueryParam("numberOfAdults", equalTo("4"))
            .willReturn(ok(
                """
                [
                  {
                    "carrier": "Wizz Air",
                    "basePrice": 199.99,
                    "tax": 29.50,
                    "discount": 50.00,
                    "departureAirportName": "AMS",
                    "arrivalAirportName": "DXB",
                    "outboundDateTime": "2025-03-10T08:30:00Z",
                    "inboundDateTime": "2025-03-15T18:45:00Z"
                  },
                  {
                    "carrier": "Air France",
                    "basePrice": 100.00,
                    "tax": 199.00,
                    "discount": 0,
                    "departureAirportName": "AMS",
                    "arrivalAirportName": "DXB",
                    "outboundDateTime": "2025-03-10T08:30:00Z",
                    "inboundDateTime": "2025-03-15T10:45:00Z"
                  }
                ],
                """.trimIndent())))
        // @formatter:on

        // when
        val response = post(
            "/api/flights/search",
            """
                {
                  "origin": "AMS",
                  "destination": "DXB",
                  "departureDate": "2025-03-10",
                  "returnDate": "2025-03-15",
                  "numberOfPassengers": 4
                }
            """.trimIndent()
        )

        // then
        assertThat(response.statusCode).isEqualTo(OK)
        assertThatJson(response.body!!).isEqualTo(
            """
                [
                  {
                    "airline": "EasyJet",
                    "supplier": "CRAZY_AIR",
                    "fare": 80.00,
                    "departureAirportCode": "AMS",
                    "destinationAirportCode": "DXB",
                    "departureDate": "2025-03-10T03:30:00Z",
                    "arrivalDate": "2025-03-15T00:20:00Z"
                  },
                  {
                    "airline": "Wizz Air",
                    "supplier": "TOUGH_JET",
                    "fare": 129.50,
                    "departureAirportCode": "AMS",
                    "destinationAirportCode": "DXB",
                    "departureDate": "2025-03-10T08:30:00Z",
                    "arrivalDate": "2025-03-15T18:45:00Z"
                  },
                  {
                    "airline": "Air France",
                    "supplier": "TOUGH_JET",
                    "fare": 299.00,
                    "departureAirportCode": "AMS",
                    "destinationAirportCode": "DXB",
                    "departureDate": "2025-03-10T08:30:00Z",
                    "arrivalDate": "2025-03-15T10:45:00Z"
                  },
                  {
                    "airline": "Air France",
                    "supplier": "CRAZY_AIR",
                    "fare": 299.99,
                    "departureAirportCode": "AMS",
                    "destinationAirportCode": "DXB",
                    "departureDate": "2025-03-10T07:30:00Z",
                    "arrivalDate": "2025-03-15T06:45:00Z"
                  },
                  {
                    "airline": "KLM",
                    "supplier": "CRAZY_AIR",
                    "fare": 400.25,
                    "departureAirportCode": "AMS",
                    "destinationAirportCode": "DXB",
                    "departureDate": "2025-03-10T09:30:00Z",
                    "arrivalDate": "2025-03-15T15:20:00Z"
                  }
                ]
            """.trimIndent()
        )
    }

    @ParameterizedTest
    @ValueSource(ints = [-100, -1, 0, 5, 100])
    fun `returns 400 if invalid number of passengers`(numberOfPassengers: Int) {
        // when
        val response = post(
            "/api/flights/search",
            """
                {
                    "origin": "AMS",
                    "destination": "DXB",
                    "departureDate": "2025-03-10",
                    "returnDate": "2025-03-15",
                    "numberOfPassengers": $numberOfPassengers
                }
            """
        )

        // then
        assertThat(response.statusCode).isEqualTo(BAD_REQUEST)
        assertThatJson(response.body!!).inPath("$.code").isEqualTo(BAD_REQUEST.name)
        assertThatJson(response.body!!).inPath("$.message").asString()
            .contains("Number of passengers must be between 1 and 4 but is [$numberOfPassengers]")
    }

    @Test
    fun `returns 500 if external APIs are not available`() {
        // when
        val response = post(
            "/api/flights/search",
            """
                {
                    "origin": "AMS",
                    "destination": "DXB",
                    "departureDate": "2025-03-10",
                    "returnDate": "2025-03-15",
                    "numberOfPassengers": 1
                }
            """.trimIndent()
        )

        // then
        assertThat(response.statusCode).isEqualTo(INTERNAL_SERVER_ERROR)
        assertThatJson(response.body!!).isEqualTo("""
            {
              "code": "INTERNAL_SERVER_ERROR",
              "message": "Something went wrong"
            }
        """.trimIndent())
    }
}