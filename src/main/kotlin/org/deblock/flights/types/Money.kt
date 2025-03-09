package org.deblock.flights.types

import java.math.BigDecimal
import java.math.RoundingMode.UNNECESSARY
import java.util.*

data class Money(
    val minorAmount: Long,
    val currency: Currency
) {
    companion object {
        fun money(amount: BigDecimal, currency: String): Money {
            return money(amount, Currency.getInstance(currency))
        }

        fun money(amount: BigDecimal, currency: Currency): Money {
            val minorAmount = amount.movePointRight(currency.defaultFractionDigits)
                .setScale(0, UNNECESSARY)
                .toLong()
            return Money(minorAmount, currency)
        }

        fun money(minorAmount: Long, currency: Currency): Money {
            return Money(minorAmount, currency)
        }

        fun money(minorAmount: Long, currency: String): Money {
            return money(minorAmount, Currency.getInstance(currency))
        }
    }

    fun amount(): BigDecimal = BigDecimal.valueOf(minorAmount).movePointLeft(currency.defaultFractionDigits)

    override fun toString(): String {
        val bigDecimal = BigDecimal.valueOf(minorAmount).movePointLeft(currency.defaultFractionDigits)
            .setScale(currency.defaultFractionDigits, UNNECESSARY)
        return "${currency.currencyCode} $bigDecimal"
    }
}