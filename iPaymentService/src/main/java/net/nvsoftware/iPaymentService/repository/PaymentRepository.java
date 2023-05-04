package net.nvsoftware.iPaymentService.repository;

import net.nvsoftware.iPaymentService.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
}
