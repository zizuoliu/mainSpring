package net.nvsoftware.iServiceRegistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class IServiceRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(IServiceRegistryApplication.class, args);
	}

}
