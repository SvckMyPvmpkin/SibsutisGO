package com.sibsutisgo.dto;

import com.sibsutisgo.model.CarType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TripCreateRequest(
        @NotNull(message = "ID пассажира обязателен")
        Long passengerId,
        @NotBlank(message = "Точка отправления не может быть пустой")
        String origin,
        @NotBlank(message = "Точка назначения не может быть пустой")
        String destination,
        CarType carType
) {}
