package trip_planner_app.authentication_service.service;

import trip_planner_app.authentication_service.model.ResetPasswordToken;
import trip_planner_app.authentication_service.model.User;

import javax.mail.MessagingException;
import java.io.IOException;

public interface ResetPasswordService {
    void createPasswordResetTokenForUser(User user, String token);
    ResetPasswordToken validatePasswordResetToken(String token);
    void sendResetTokenEmail(User user, String token) throws MessagingException, IOException;
    void resetPassword(User user, String newPassword);
    String generateToken();
}
