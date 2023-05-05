package net.nvsoftware.iApiGateway;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class IApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(IApiGatewayApplication.class, args);
	}

	@Bean
	public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer() {
		return factory -> factory.configureDefault(
				id -> new Resilience4JConfigBuilder(id).circuitBreakerConfig(CircuitBreakerConfig.ofDefaults()).build()
		);
	}

	@Bean
	KeyResolver userIdSolver() {
		return exchange -> Mono.just("userId");
	}
}
