package trip_planner_app.authentication_service.controller;

import trip_planner_app.authentication_service.model.ResetPasswordToken;
import trip_planner_app.authentication_service.model.User;
import trip_planner_app.authentication_service.repository.UserRepository;
import trip_planner_app.authentication_service.service.ResetPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.IOException;

@RestController
@RequestMapping("/api/trip-planner/authentication")
@RequiredArgsConstructor
public class ResetPasswordController {

    private final UserRepository userRepository;
    private final ResetPasswordService resetPasswordService;

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam("email") String userEmail) throws MessagingException, IOException {
        System.out.println("Received email: " + userEmail);

        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            System.out.println("User not found for email: " + userEmail);
            return ResponseEntity.badRequest().body("User not found");
        }

        String token = resetPasswordService.generateToken();
        resetPasswordService.createPasswordResetTokenForUser(user, token);
        resetPasswordService.sendResetTokenEmail(user, token);

        return ResponseEntity.ok("Reset password email has been sent");
    }


    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam("token") String token,
                                                @RequestParam("newPassword") String newPassword) {
        ResetPasswordToken resetPasswordToken = resetPasswordService.validatePasswordResetToken(token);
        User user = resetPasswordToken.getUser();
        resetPasswordService.resetPassword(user, newPassword);

        return ResponseEntity.ok("Password has been successfully reset");
    }
}
