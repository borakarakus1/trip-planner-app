package trip_planner_app.user_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import trip_planner_app.user_service.dto.UserProfileRequestDTO;
import trip_planner_app.user_service.exception.CustomException;
import trip_planner_app.user_service.model.UserProfile;
import trip_planner_app.user_service.repository.UserProfileRepository;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements trip_planner_app.user_service.service.impl.UserProfileService {

    private final UserProfileRepository userProfileRepository;

    @Override
    @Cacheable(value = "userProfile", key = "#username")
    public UserProfile getUserProfile(String username) {
        return userProfileRepository.findByUsername(username);
    }

    @Override
    public UserProfile updateUserProfile(String username, UserProfileRequestDTO userProfileRequestDTO) {
        UserProfile userProfile = userProfileRepository.findByUsername(username);
        if (userProfile == null) {
            throw new CustomException("User profile not found", HttpStatus.NOT_FOUND);
        }
        userProfile.setFullName(userProfileRequestDTO.getFullName());
        userProfile.setAddress(userProfileRequestDTO.getAddress());
        userProfile.setPhoneNumber(userProfileRequestDTO.getPhoneNumber());
        userProfile.setPreferences(userProfileRequestDTO.getPreferences());

        return userProfileRepository.save(userProfile);
    }

    @Override
    @CachePut(value = "userProfile", key = "#userProfileRequestDTO.username")
    public UserProfile createUserProfile(UserProfileRequestDTO userProfileRequestDTO) {
        if (userProfileRepository.findByUsername(userProfileRequestDTO.getUsername()) != null) {
            throw new CustomException("User profile already exists", HttpStatus.BAD_REQUEST);
        }

        UserProfile userProfile = UserProfile.builder()
                .username(userProfileRequestDTO.getUsername())
                .password(userProfileRequestDTO.getPassword())
                .email(userProfileRequestDTO.getEmail())
                .fullName("")
                .address("")
                .phoneNumber("")
                .preferences("")
                .build();

        return userProfileRepository.save(userProfile);
    }

    @Override
    public void deleteUserProfile(String username) {
        userProfileRepository.deleteByUsername(username);
    }
}
