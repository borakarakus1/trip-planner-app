package trip_planner_app.shared_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SharedServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SharedServiceApplication.class, args);
	}

}
