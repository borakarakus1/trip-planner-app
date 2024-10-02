package trip_planner_app.authentication_service.service.impl;

import trip_planner_app.authentication_service.exception.CustomException;
import trip_planner_app.authentication_service.model.ResetPasswordToken;
import trip_planner_app.authentication_service.model.User;
import trip_planner_app.authentication_service.repository.ResetPasswordTokenRepository;
import trip_planner_app.authentication_service.repository.UserRepository;
import trip_planner_app.authentication_service.service.ResetPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResetPasswordServiceImpl implements ResetPasswordService {

    @Value("${spring.mail.username}")
    private String fromEmail;
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;


    private static final long EXPIRATION = 1000 * 60 * 60; // 1 hour in milliseconds

    @Override
    public void createPasswordResetTokenForUser(User user, String token) {
        ResetPasswordToken resetPasswordToken = new ResetPasswordToken();
        resetPasswordToken.setUser(user);
        resetPasswordToken.setToken(token);
        resetPasswordToken.setExpiryDate(new Date(System.currentTimeMillis() + EXPIRATION));
        resetPasswordTokenRepository.save(resetPasswordToken);
    }

    @Override
    public ResetPasswordToken validatePasswordResetToken(String token) {
        ResetPasswordToken resetToken = resetPasswordTokenRepository.findByToken(token);

        if (resetToken == null) {
            throw new CustomException("Invalid reset token", HttpStatus.BAD_REQUEST);
        }

        if (resetToken.getExpiryDate().before(new Date())) {
            throw new CustomException("Token expired", HttpStatus.BAD_REQUEST);
        }

        return resetToken;
    }

    @Override
    public void sendResetTokenEmail(User user, String token) throws MessagingException, IOException {
        String recipientAddress = user.getEmail();
        String subject = "Password Reset Request";

        ClassPathResource resource = new ClassPathResource("reset-password-template.html");
        if (!resource.exists()) {
            throw new CustomException("Template file not found", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        String content = new String(Files.readAllBytes(resource.getFile().toPath()));

        content = content.replace("{{token}}", token);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(recipientAddress);
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(mimeMessage);
    }

    @Override
    public void resetPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public String generateToken() {
        return UUID.randomUUID().toString();
    }
}

