package trip_planner_app.user_service.service.impl;

import trip_planner_app.user_service.model.UserProfile;
import trip_planner_app.user_service.dto.UserProfileRequestDTO;

public interface UserProfileService {
    UserProfile getUserProfile(String username);
    UserProfile updateUserProfile(String username, UserProfileRequestDTO userProfileRequestDTO);
    UserProfile createUserProfile(UserProfileRequestDTO userProfileRequestDTO);
    void deleteUserProfile(String username);
}
