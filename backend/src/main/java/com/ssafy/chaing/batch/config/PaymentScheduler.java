package com.ssafy.chaing.batch.config;

import com.ssafy.chaing.batch.service.RentBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class PaymentScheduler {

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private RentBatchService rentBatchService;


    @EventListener
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePaymentCreated(PaymentCreatedEvent event) {
        taskScheduler.schedule(() -> rentBatchService.collectToJointAccount(event.paymentId()),
                event.collectExecution().toInstant());
        taskScheduler.schedule(() -> rentBatchService.payToOwner(event.paymentId()),
                event.ownerExecution().toInstant());
    }
}
