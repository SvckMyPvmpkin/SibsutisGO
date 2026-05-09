package com.sibsutisgo.mq;

import com.sibsutisgo.dto.messaging.DriverSearchRequest;
import com.sibsutisgo.dto.messaging.DriverSearchResponse;
import com.sibsutisgo.service.UserService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DriverSearchListener {
    private final UserService userService;

    public DriverSearchListener(UserService userService){
        this.userService = userService;
    }

    @RabbitListener(queues = "driver-search-queue")
    public DriverSearchResponse handleSearchRequest(DriverSearchRequest request) {
        Long foundDriverId = userService.getAvailableDriverId(request.carType());

        if (foundDriverId != null) {
            return new DriverSearchResponse(foundDriverId, request.carType());
        } else {
            return new DriverSearchResponse(null, null);
        }
    }
}
