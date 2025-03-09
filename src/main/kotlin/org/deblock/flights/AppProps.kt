package org.deblock.flights

import org.springframework.boot.context.properties.ConfigurationProperties
import java.net.URL

@ConfigurationProperties(prefix = "app")
open class AppProps(
    val crazyAir: CrazyAir,
    val toughJet: ToughJet
) {
    data class CrazyAir(
        val baseUrl: URL
    )

    data class ToughJet(
        val baseUrl: URL
    )
}