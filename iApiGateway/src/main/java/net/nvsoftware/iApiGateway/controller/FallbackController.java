package net.nvsoftware.iApiGateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @GetMapping("/orderServiceFallback")
    public String orderServiceFallback() {
        return "OrderService Down";
    }

    @GetMapping("/productServiceFallback")
    public String productServiceFallback() {
        return "ProductService Down";
    }

    @GetMapping("/paymentServiceFallback")
    public String paymentServiceFallback() {
        return "PaymentService Down";
    }
}
