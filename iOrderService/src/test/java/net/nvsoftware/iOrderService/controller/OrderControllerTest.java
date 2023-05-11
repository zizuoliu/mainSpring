package net.nvsoftware.iOrderService.controller;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import net.nvsoftware.iOrderService.OrderServiceConfigTest;
import net.nvsoftware.iOrderService.repository.OrderRepository;
import net.nvsoftware.iOrderService.service.OrderService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;

@SpringBootTest
@EnableConfigurationProperties
@AutoConfigureMockMvc
@ContextConfiguration(classes = {OrderServiceConfigTest.class})
public class OrderControllerTest {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MockMvc mockMvc;

    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
            .options(WireMockConfiguration.wireMockConfig().port(8053))
            .build();

    @BeforeEach
    void setupTest() throws IOException {// put common methods every test will use here
        reduceQuantity();
        getProductResponse();
        doPayment();
        getPaymentResponse();
    }

    private void doPayment() {
        wireMockExtension.stubFor(WireMock.post("/payments")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                )
        );
    }

    private void getPaymentResponse() throws IOException {
        wireMockExtension.stubFor(WireMock.get("/payments/1")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(StreamUtils.copyToString(OrderControllerTest.class.getClassLoader()
                                                .getResourceAsStream("mockData/paymentResponse.json"),
                                        Charset.defaultCharset()
                                )
                        )
                )
        );
    }

    private void getProductResponse() throws IOException {
        wireMockExtension.stubFor(WireMock.get("/products/1")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(StreamUtils.copyToString(OrderControllerTest.class.getClassLoader()
                                .getResourceAsStream("mockData/productResponse.json"),
                                Charset.defaultCharset()
                                )
                        )
                )
        );
    }

    private void reduceQuantity() {
        wireMockExtension.stubFor(WireMock.put("/product/reduceQuantity")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                )
        );
    }

    @Test
    public void testPlaceOrderDoPaymentSuccess() { //TODO: Finish JWT Token for Services
        // Place Order
        // Get Order from DB by orderId
        // Verify OrderResponse
    }
}
