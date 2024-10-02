package trip_planner_app.user_service.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import trip_planner_app.user_service.dto.UserProfileRequestDTO;
import trip_planner_app.user_service.model.UserProfile;
import trip_planner_app.user_service.service.impl.UserProfileService;

@RestController
@RequestMapping("/api/trip-planner/user")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @PostMapping("/create")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserProfile> createUserProfile(@RequestBody UserProfileRequestDTO userProfileRequestDTO) {
        UserProfile userProfile = userProfileService.createUserProfile(userProfileRequestDTO);
        return new ResponseEntity<>(userProfile, HttpStatus.CREATED);
    }

    @GetMapping("/{username}")
    //@PreAuthorize("hasRole('ROLE_ADMIN') or #username == principal.username")
    public ResponseEntity<UserProfile> getUserProfile(@PathVariable String username) {
        UserProfile userProfile = userProfileService.getUserProfile(username);
        return ResponseEntity.ok(userProfile);
    }

    @PutMapping("/{username}")
    //@PreAuthorize("hasRole('ROLE_ADMIN') or #username == principal.username")
    public ResponseEntity<UserProfile> updateUserProfile(@PathVariable String username,
                                                         @RequestBody UserProfileRequestDTO userProfileRequestDTO) {
        UserProfile updatedProfile = userProfileService.updateUserProfile(username, userProfileRequestDTO);
        return ResponseEntity.ok(updatedProfile);
    }

    @DeleteMapping("/{username}")
    //@PreAuthorize("hasRole('ROLE_ADMIN') or #username == principal.username")
    public ResponseEntity<String> deleteUserProfile(@PathVariable String username) {
        userProfileService.deleteUserProfile(username);
        return new ResponseEntity<>("User profile deleted successfully", HttpStatus.OK);
    }
}

