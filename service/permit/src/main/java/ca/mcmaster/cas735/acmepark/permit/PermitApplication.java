package ca.mcmaster.cas735.acmepark.permit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PermitApplication {

	public static void main(String[] args) {
		SpringApplication.run(PermitApplication.class, args);
	}

}
