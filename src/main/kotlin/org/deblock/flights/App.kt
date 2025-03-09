package org.deblock.flights

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(AppProps::class)
open class App

fun main(args: Array<String>) {
    runApplication<App>(*args)
}
