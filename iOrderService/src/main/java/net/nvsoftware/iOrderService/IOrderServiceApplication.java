package net.nvsoftware.iOrderService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class IOrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IOrderServiceApplication.class, args);
	}

}
