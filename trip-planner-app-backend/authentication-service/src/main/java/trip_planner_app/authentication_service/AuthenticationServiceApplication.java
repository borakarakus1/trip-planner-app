package trip_planner_app.authentication_service;

import org.springframework.cloud.openfeign.EnableFeignClients;
import trip_planner_app.authentication_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Arrays;

@SpringBootApplication
@RequiredArgsConstructor
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "trip_planner_app.authentication_service.service")
public class AuthenticationServiceApplication {
	private final UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationServiceApplication.class, args);
	}
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
