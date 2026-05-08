package com.sibsutisgo.mq;

import com.sibsutisgo.dto.messaging.PassengerVerifyRequest;
import com.sibsutisgo.dto.messaging.PassengerVerifyResponse;
import com.sibsutisgo.service.UserService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PassengerVerifyListener {
    private final UserService userService;

    public PassengerVerifyListener(UserService userService){
        this.userService = userService;
    }

    @RabbitListener(queues = "passenger-verify-queue")
    public PassengerVerifyResponse handleVerifyRequest(PassengerVerifyRequest request){
        return new PassengerVerifyResponse(userService.isPassengerExist(request.id()));
    }
}
