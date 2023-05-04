package net.nvsoftware.iPaymentService.service;

import net.nvsoftware.iPaymentService.model.PaymentRequest;

public interface PaymentService {
    long doPayment(PaymentRequest paymentRequest);
}
