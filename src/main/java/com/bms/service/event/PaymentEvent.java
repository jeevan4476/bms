package com.bms.service.event;

import org.springframework.context.ApplicationEvent;
import com.bms.entity.Payment;

public class PaymentEvent extends ApplicationEvent {
    private final Payment payment;

    public PaymentEvent(Object source, Payment payment) {
        super(source);
        this.payment = payment;
    }

    public Payment getPayment() {
        return payment;
    }
}
