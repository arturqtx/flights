package org.deblock.flights.spec

import org.deblock.flights.App
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpHeaders.ACCEPT
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.ResponseEntity
import org.wiremock.spring.ConfigureWireMock
import org.wiremock.spring.EnableWireMock

@SpringBootTest(
    webEnvironment = RANDOM_PORT,
    classes = [App::class]
)
@EnableWireMock(
    ConfigureWireMock(
        baseUrlProperties = [
            "app.crazy-air.base-url",
            "app.tough-jet.base-url"
        ]
    )
)
abstract class FunctionalSpec {

    @LocalServerPort
    private var port: Int = 0

    private val restTemplate = TestRestTemplate()
        .apply {
            restTemplate.interceptors.add { request, body, execution ->
                request.headers.set(CONTENT_TYPE, APPLICATION_JSON.toString())
                request.headers.set(ACCEPT, APPLICATION_JSON.toString())
                execution.execute(request, body)
            }
        }

    protected fun post(path: String, body: String): ResponseEntity<String> = restTemplate
        .postForEntity(
            "http://localhost:$port$path",
            body,
            String::class.java
        )
}