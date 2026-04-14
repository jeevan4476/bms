package com.bms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.bms.entity.Payment;

/**
 * ReceiptService handles the generation of simple digital receipts for payments.
 * Authored by jeevan-cs240 as part of the Payment Domain.
 */
@Service
public class ReceiptService {
    private static final Logger log = LoggerFactory.getLogger(ReceiptService.class);

    public String generateReceipt(Payment payment) {
        log.info("Generating digital receipt for paymentId: {}", payment.getId());
        
        StringBuilder receipt = new StringBuilder();
        receipt.append("---- BMS DIGITAL RECEIPT ----\n");
        receipt.append("Transaction ID: ").append(payment.getId()).append("\n");
        receipt.append("Booking ID: ").append(payment.getBooking().getId()).append("\n");
        receipt.append("Amount Paid: $").append(payment.getAmount()).append("\n");
        receipt.append("Status: ").append(payment.getStatus()).append("\n");
        receipt.append("Timestamp: ").append(payment.getPaymentTime()).append("\n");
        receipt.append("-----------------------------\n");
        
        return receipt.toString();
    }
}
