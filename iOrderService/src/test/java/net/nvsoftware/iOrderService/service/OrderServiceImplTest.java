package net.nvsoftware.iOrderService.service;

import net.nvsoftware.iOrderService.entity.OrderEntity;
import net.nvsoftware.iOrderService.external.client.PaymentServiceFeignClient;
import net.nvsoftware.iOrderService.external.client.ProductServiceFeignClient;
import net.nvsoftware.iOrderService.model.OrderRequest;
import net.nvsoftware.iOrderService.model.OrderResponse;
import net.nvsoftware.iOrderService.model.PaymentMode;
import net.nvsoftware.iOrderService.model.PaymentRequest;
import net.nvsoftware.iOrderService.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
class OrderServiceImplTest {
    @Mock
    RestTemplate restTemplate;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductServiceFeignClient productServiceFeignClient;

    @Mock
    private PaymentServiceFeignClient paymentServiceFeignClient;

    @InjectMocks
    OrderService orderService = new OrderServiceImpl();

    @DisplayName("Get Order Detail - SUCCESS")
    @Test
    void testWhenGetOrderSuccess() {
        // Mock Part
        OrderEntity orderEntity = getMockOrderEntity();
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(orderEntity));

        when(restTemplate.getForObject(
                "http://PRODUCT-SERVICE/products/" + orderEntity.getProductId(),
                OrderResponse.ProductResponse.class
        )).thenReturn(getMockProductResponse());

        when(restTemplate.getForObject(
                "http://PAYMENT-SERVICE/payments/" + orderEntity.getOrderId(),
                OrderResponse.PaymentResponse.class
        )).thenReturn(getMockPaymentResponse());

        // Actual Call
        OrderResponse orderResponse = orderService.getOrderDetailByOrderId(1);

        // Verify Call
        Mockito.verify(orderRepository, Mockito.times(1)).findById(anyLong());
        Mockito.verify(restTemplate, Mockito.times(1)).getForObject(
                "http://PRODUCT-SERVICE/products/" + orderEntity.getProductId(),
                OrderResponse.ProductResponse.class
        );
        Mockito.verify(restTemplate, Mockito.times(1)).getForObject(
                "http://PAYMENT-SERVICE/payments/" + orderEntity.getOrderId(),
                OrderResponse.PaymentResponse.class
        );

        // Assert Response
        Assertions.assertNotNull(orderResponse);
        Assertions.assertEquals(orderEntity.getOrderId(), orderResponse.getOrderId());
    }

    @DisplayName("Get Order Detail OrderId Not Found - FAILED")
    @Test
    void testWhenOrderIdNotFound() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));

        RuntimeException runtimeException = Assertions.assertThrows(RuntimeException.class, () ->  orderService.getOrderDetailByOrderId(1));
        Assertions.assertEquals("OrderService getOrderDetailByOrderId NOT FOUND for: 1", runtimeException.getMessage());

        Mockito.verify(orderRepository, Mockito.times(1)).findById(anyLong());
    }

    @DisplayName("Place Order - Success")
    @Test
    void testWhenPlaceOrderSuccess() {
        OrderEntity orderEntity = getMockOrderEntity();
        OrderRequest orderRequest = getMockOrderRequest();

        when(orderRepository.save(Mockito.any(OrderEntity.class)))
                .thenReturn(orderEntity);
        when(productServiceFeignClient.reduceQuantity(anyLong(), anyLong()))
                .thenReturn(new ResponseEntity<Void>(HttpStatus.OK));
        when(paymentServiceFeignClient.doPayment(Mockito.any(PaymentRequest.class)))
                .thenReturn(new ResponseEntity<Long>(1L, HttpStatus.OK));

        long orderId = orderService.placeOrder(orderRequest);

        verify(orderRepository, times(2)).save(any());
        verify(productServiceFeignClient, times(1)).reduceQuantity(anyLong(),anyLong());
        verify(paymentServiceFeignClient, times(1)).doPayment(any(PaymentRequest.class));

        Assertions.assertEquals(orderEntity.getOrderId(), orderId);
    }

    @DisplayName("Place Order Payment Failed - Failed")
    @Test
    void testWhenPlaceOrderFailed() {
        OrderEntity orderEntity = getMockOrderEntity();
        OrderRequest orderRequest = getMockOrderRequest();

        when(orderRepository.save(Mockito.any(OrderEntity.class)))
                .thenReturn(orderEntity);
        when(productServiceFeignClient.reduceQuantity(anyLong(), anyLong()))
                .thenReturn(new ResponseEntity<Void>(HttpStatus.OK));
        when(paymentServiceFeignClient.doPayment(Mockito.any(PaymentRequest.class)))
                .thenThrow(new RuntimeException("Payment Failed"));

        long orderId = orderService.placeOrder(orderRequest);

        verify(orderRepository, times(2)).save(any());
        verify(productServiceFeignClient, times(1)).reduceQuantity(anyLong(),anyLong());
        verify(paymentServiceFeignClient, times(1)).doPayment(any(PaymentRequest.class));

        Assertions.assertEquals(orderEntity.getOrderId(), orderId);
    }

    private OrderRequest getMockOrderRequest() {
        return OrderRequest.builder()
                .productId(1)
                .orderQuantity(1)
                .totalAmount(1299)
                .paymentMode(PaymentMode.CASH)
                .build();
    }

    private OrderEntity getMockOrderEntity() {
        return OrderEntity.builder()
                .orderId(1)
                .productId(5)
                .orderQuantity(1)
                .totalAmount(1299)
                .orderDate(Instant.now())
                .orderStatus("PLACED")
                .build();
    }

    private OrderResponse.PaymentResponse getMockPaymentResponse() {
        return OrderResponse.PaymentResponse.builder()
                .orderId(1)
                .paymentId(1)
                .paymentDate(Instant.now())
                .paymentMode(PaymentMode.CASH)
                .paymentStatus("SUCCESS")
                .totalAmount(2599)
                .build();
    }

    private OrderResponse.ProductResponse getMockProductResponse() {
        return OrderResponse.ProductResponse.builder()
                .productId(5)
                .productName("MacMini")
                .productQuantity(2)
                .productPrice(1299)
                .build();
    }
}