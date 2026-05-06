package com.sibsutisgo.dto.messaging;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DriverSearchRequest(
        boolean status
        //здесь добавить типа машины или тариф
) {}
