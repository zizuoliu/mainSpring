package net.nvsoftware.iOrderService;

import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Flux;

import java.security.Provider;
import java.util.ArrayList;
import java.util.List;

public class ServiceInstanceListSupplierTest implements ServiceInstanceListSupplier {
    @Override
    public String getServiceId() {
        return null;
    }

    @Override
    public Flux<List<ServiceInstance>> get() {
        List<ServiceInstance> serviceInstanceList = new ArrayList<>();

        serviceInstanceList.add(new DefaultServiceInstance(
                "PAYMENT-SERVICE",
                "PAYMENT-SERVICE",
                "localhost",
                8053,
                false
        ));

        serviceInstanceList.add(new DefaultServiceInstance(
                "PRODUCT-SERVICE",
                "PRODUCT-SERVICE",
                "localhost",
                8053,
                false
        ));
        return Flux.just(serviceInstanceList);
    }
}
