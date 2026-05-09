package com.sibsutisgo.model;

public enum CarType {
    ECONOMY(1.0),
    COMFORT(1.3),
    BUISNESS(1.75),
    KIDS(1.3);

    private final double coefficient;

    CarType(double coefficient) {
        this.coefficient = coefficient;
    }

    public double getCoefficient() {
        return coefficient;
    }
}
