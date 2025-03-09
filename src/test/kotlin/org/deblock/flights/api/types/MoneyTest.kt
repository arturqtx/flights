package org.deblock.flights.api.types

import org.assertj.core.api.Assertions.assertThat
import org.deblock.flights.types.Money.Companion.money
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.math.BigDecimal

class MoneyTest {

    @ParameterizedTest
    @CsvSource(
        value = [
            "-1000,     USD,   USD -1000.00",
            "-1000.0,   EUR,   EUR -1000.00",
            "-1000.00,  USD,   USD -1000.00",
            "-1000.10,  GBP,   GBP -1000.10",
            "-1000.15,  USD,   USD -1000.15",
            "-0,        USD,   USD 0.00",
            "0,         USD,   USD 0.00",
            "0.01,      EUR,   EUR 0.01",
            "0.10,      USD,   USD 0.10",
            "0.99,      USD,   USD 0.99",
            "500,       USD,   USD 500.00",
            "500.20,    GBP,   GBP 500.20",
        ]
    )
    fun `creates a money based on BigDecimal and Currency`(
        bigDecimal: BigDecimal,
        currency: String,
        result: String
    ) {
        assertThat(money(bigDecimal, currency).toString()).isEqualTo(result)
    }

    @ParameterizedTest
    @CsvSource(
        value = [
            "-100000,  USD,   USD -1000.00",
            "-100010,  GBP,   GBP -1000.10",
            "-100015,  USD,   USD -1000.15",
            "-0,       USD,   USD 0.00",
            "0,        USD,   USD 0.00",
            "1,        EUR,   EUR 0.01",
            "10,       USD,   USD 0.10",
            "99,       USD,   USD 0.99",
            "50000,    USD,   USD 500.00",
            "50020,    GBP,   GBP 500.20",
        ]
    )
    fun `creates a money based on Long and Currency`(
        minorAmount: Long,
        currency: String,
        result: String
    ) {
        assertThat(money(minorAmount, currency).toString()).isEqualTo(result)
    }
}