package org.deblock.flights.supplier.toughjet

import org.deblock.flights.types.Airport
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.time.Instant

class ToughJetFlightResponse(
    val carrier: String,
    val basePrice: BigDecimal,
    val tax: BigDecimal,
    val discount: BigDecimal,
    val departureAirportName: Airport,
    val arrivalAirportName: Airport,
    val outboundDateTime: Instant,
    val inboundDateTime: Instant
) {
    init {
        check(basePrice >= ZERO) { "Base Price must not be negative but is [$basePrice]" }
        check(tax >= ZERO) { "Tax must not be negative but is [$tax]" }
        check(discount >= ZERO) { "Discount must not be negative but is [$discount]" }
    }
}