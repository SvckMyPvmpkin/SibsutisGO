package com.sibsutisgo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class DistanceService {

    private final RestClient orsClient;
    private final ObjectMapper objectMapper;

    @Value("${ors.api-key}")
    private String apiKey;

    public DistanceService(RestClient orsClient) {
        this.orsClient = orsClient;
        this.objectMapper = new ObjectMapper();
    }

    public double getDistanceBetweenAddresses(String origin, String destination) {
        double[] originCoords = getCoordinates(origin);
        double[] destCoords = getCoordinates(destination);

        System.out.println("LOG Start Coords: " + originCoords[0] + ", " + originCoords[1]);
        System.out.println("LOG End Coords: " + destCoords[0] + ", " + destCoords[1]);

        if (originCoords[0] == destCoords[0] && originCoords[1] == destCoords[1]) {
            return 5.0;
        }

        return getDistanceInKm(originCoords[0], originCoords[1], destCoords[0], destCoords[1]);
    }

    private double[] getCoordinates(String address) {
        try {
            String rawJson = orsClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/geocode/search")
                            .queryParam("text", address)
                            .queryParam("boundary.country", "RU")
                            .queryParam("size", 1)
                            .build())
                    .header("Authorization", apiKey)
                    .retrieve()
                    .body(String.class);

            JsonNode response = objectMapper.readTree(rawJson);
            JsonNode features = response.path("features");

            if (features.isEmpty()) {
                return new double[]{82.9346, 55.0282};
            }

            JsonNode coords = features.get(0).path("geometry").path("coordinates");
            return new double[]{coords.get(0).asDouble(), coords.get(1).asDouble()};

        } catch (Exception e) {
            System.err.println("Geocoding Error: " + e.getMessage());
            return new double[]{82.9346, 55.0282};
        }
    }

    private double getDistanceInKm(double sLon, double sLat, double eLon, double eLat) {
        try {
            String rawJson = orsClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v2/directions/driving-car")
                            .queryParam("start", sLon + "," + sLat)
                            .queryParam("end", eLon + "," + eLat)
                            .build())
                    .header("Authorization", apiKey)
                    .retrieve()
                    .body(String.class);

            JsonNode response = objectMapper.readTree(rawJson);

            double meters = response.path("features").get(0)
                    .path("properties")
                    .path("summary")
                    .path("distance")
                    .asDouble();

            return meters / 1000.0;

        } catch (Exception e) {
            System.err.println("Routing Error: " + e.getMessage());
            return 5.0;
        }
    }
}