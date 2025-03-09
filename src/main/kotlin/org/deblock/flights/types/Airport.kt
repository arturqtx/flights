package org.deblock.flights.types

import java.time.ZoneId

@Suppress("unused")
enum class Airport(
    val zoneId: ZoneId
) {
    AMS(ZoneId.of("Europe/Amsterdam")),
    ATL(ZoneId.of("America/New_York")),
    CDG(ZoneId.of("Europe/Paris")),
    DFW(ZoneId.of("America/Chicago")),
    DXB(ZoneId.of("Asia/Dubai")),
    HND(ZoneId.of("Asia/Tokyo")),
    LHR(ZoneId.of("Europe/London")),
    LAX(ZoneId.of("America/Los_Angeles")),
    NYC(ZoneId.of("America/New_York")),
    ORD(ZoneId.of("America/Chicago")),
    PEK(ZoneId.of("Asia/Shanghai")),
    PVG(ZoneId.of("Asia/Shanghai"));
}
