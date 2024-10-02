package trip_planner_app.authentication_service.repository;

import trip_planner_app.authentication_service.model.ResetPasswordToken;
import trip_planner_app.authentication_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Long> {
    ResetPasswordToken findByToken(String token);
    ResetPasswordToken findByUser(User user);
}
