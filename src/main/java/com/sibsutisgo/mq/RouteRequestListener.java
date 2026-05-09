package com.sibsutisgo.mq;

import com.sibsutisgo.dto.messaging.RouteRequest;
import com.sibsutisgo.dto.messaging.RouteResponse;
import com.sibsutisgo.service.DistanceService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RouteRequestListener {
    private final DistanceService distanceService;

    public RouteRequestListener(DistanceService distanceService) {
        this.distanceService = distanceService;
    }

    @RabbitListener(queues = "route-request-queue")
    public RouteResponse handleRouteRequest(RouteRequest request) {
        double dist = distanceService.getDistanceBetweenAddresses(request.origin(), request.destination());

        return new RouteResponse(dist, dist * 1.5);
    }
}
