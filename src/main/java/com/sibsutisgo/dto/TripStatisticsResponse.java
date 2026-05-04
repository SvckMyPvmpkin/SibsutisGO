package com.sibsutisgo.dto;

import java.math.BigDecimal;

public record TripStatisticsResponse(
        long totalTripsToday,
        BigDecimal averagePrice
) {}
