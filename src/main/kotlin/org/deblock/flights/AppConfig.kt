package org.deblock.flights

import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.ObjectMapper
import org.deblock.flights.supplier.crazyair.CrazyAirApiClient
import org.deblock.flights.supplier.toughjet.ToughJetApiClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

@Configuration
@Suppress("unused")
open class AppConfig(
    private val appProps: AppProps
) {
    @Bean
    open fun crazyAirApiClient(): CrazyAirApiClient {
        val objectMapper = ObjectMapper().apply {
            configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
            findAndRegisterModules()
        }
        return Retrofit.Builder()
            .baseUrl(appProps.crazyAir.baseUrl)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .build()
            .create(CrazyAirApiClient::class.java)
    }

    @Bean
    open fun toughJetApiClient(): ToughJetApiClient {
        val objectMapper = ObjectMapper().apply {
            configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
            findAndRegisterModules()
        }
        return Retrofit.Builder()
            .baseUrl(appProps.toughJet.baseUrl)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .build()
            .create(ToughJetApiClient::class.java)
    }
}