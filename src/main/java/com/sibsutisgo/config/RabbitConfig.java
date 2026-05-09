package com.sibsutisgo.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue driverStatusQueue() {
        return new Queue("driver-status-queue", true);
    }

    @Bean
    public Queue driverSearchQueue(){
        return new Queue("driver-search-queue", true);
    }

    @Bean
    public Queue passengerVerifyQueue() {
        return new Queue("passenger-verify-queue", true);
    }

    @Bean
    public Queue notificationsQueue() {
        return new Queue("notifications-queue", true);
    }

    @Bean
    public Queue reviewDriverRatingQueue() {
        return new Queue("review-driver-rating-queue", true);
    }

    @Bean
    public Queue tripVerifyQueue() {
        return new Queue("trip-verify-queue", true);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}