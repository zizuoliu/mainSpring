package net.nvsoftware.iPaymentService.service;

import lombok.extern.log4j.Log4j2;
import net.nvsoftware.iPaymentService.entity.PaymentEntity;
import net.nvsoftware.iPaymentService.model.PaymentRequest;
import net.nvsoftware.iPaymentService.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Log4j2
public class PaymentServiceImpl implements PaymentService{

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public long doPayment(PaymentRequest paymentRequest) {
        log.info("PaymentService doPayment Start");
        PaymentEntity paymentEntity = PaymentEntity.builder()
                .orderId(paymentRequest.getOrderId())
                .referenceNumber(paymentRequest.getReferenceNumber())
                .totalAmount(paymentRequest.getTotalAmount())
                .paymentDate(Instant.now())
                .paymentMode(paymentRequest.getPaymentMode().name())
                .paymentStatus("SUCCESS")
                .build();
        paymentRepository.save(paymentEntity);
        log.info("PaymentService doPayment Done with paymentId: " + paymentEntity.getPaymentId());
        return paymentEntity.getPaymentId();
    }
}
