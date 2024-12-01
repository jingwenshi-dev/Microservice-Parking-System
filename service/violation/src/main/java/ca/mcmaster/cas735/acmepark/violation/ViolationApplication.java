package ca.mcmaster.cas735.acmepark.violation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
//@EnableEurekaServer
public class ViolationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ViolationApplication.class, args);
    }

}
