package com.sibsutisgo.dto;


import com.sibsutisgo.model.CarType;

public record DriverRegistrationRequest(
        String name,
        String email,
        String phone,
        String licenseNumber,
        CarType carType
) {}
