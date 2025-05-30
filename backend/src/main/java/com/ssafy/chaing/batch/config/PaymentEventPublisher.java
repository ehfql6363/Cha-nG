package com.ssafy.chaing.batch.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class PaymentEventPublisher {

    @Autowired
    private ApplicationEventPublisher publisher;

    public void publish(PaymentCreatedEvent event) {
        publisher.publishEvent(event);
    }
}
