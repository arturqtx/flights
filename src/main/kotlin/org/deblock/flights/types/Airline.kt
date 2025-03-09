package org.deblock.flights.types

data class Airline(val name: String) {

    init {
        check(name.isNotBlank()) { "Airline name must not be empty but is [$name]" }
    }
}