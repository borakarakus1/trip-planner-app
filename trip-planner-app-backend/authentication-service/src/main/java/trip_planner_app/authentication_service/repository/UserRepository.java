package trip_planner_app.authentication_service.repository;

import trip_planner_app.authentication_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByUsername(String username);

    User findByUsername(String username);

    User findByEmail(String email);

    @Transactional
    void deleteByUsername(String username);

}