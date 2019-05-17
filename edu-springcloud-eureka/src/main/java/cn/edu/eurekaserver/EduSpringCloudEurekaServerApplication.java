package cn.edu.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EduSpringCloudEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EduSpringCloudEurekaServerApplication.class, args);
	}
	
}
