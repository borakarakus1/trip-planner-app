package trip_planner_app.authentication_service.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import trip_planner_app.authentication_service.dto.UserProfileRequestDTO;

@FeignClient(name = "user-service", url = "localhost:8082")
public interface UserServiceClient {

    @PostMapping("/api/trip-planner/user/create")
    String createUserProfile(@RequestBody UserProfileRequestDTO userProfileRequestDTO);
}
