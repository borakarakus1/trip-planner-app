package trip_planner_app.user_service.dto;

import lombok.Data;

@Data
public class UserProfileRequestDTO {
    private String fullName;
    private String username;
    private String password;
    private String email;
    private String address;
    private String phoneNumber;
    private String preferences;
}
