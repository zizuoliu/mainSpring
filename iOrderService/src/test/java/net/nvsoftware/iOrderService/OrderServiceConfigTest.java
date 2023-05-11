package net.nvsoftware.iOrderService;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Flux;

import java.util.List;

@TestConfiguration
public class OrderServiceConfigTest {

    public ServiceInstanceListSupplier supplier() {
        return new ServiceInstanceListSupplierTest();
    }
}
