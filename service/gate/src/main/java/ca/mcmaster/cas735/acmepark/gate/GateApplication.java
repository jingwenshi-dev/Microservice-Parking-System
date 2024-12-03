package ca.mcmaster.cas735.acmepark.gate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GateApplication {

	public static void main(String[] args) {
		SpringApplication.run(GateApplication.class, args);
	}

}
