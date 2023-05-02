package net.nvsoftware.iServiceConfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class IServiceConfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(IServiceConfigApplication.class, args);
	}

}
