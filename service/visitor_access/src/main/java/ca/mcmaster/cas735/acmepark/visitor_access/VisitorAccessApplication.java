package ca.mcmaster.cas735.acmepark.visitor_access;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class VisitorAccessApplication {

	public static void main(String[] args) {
		SpringApplication.run(VisitorAccessApplication.class, args);
	}

}
