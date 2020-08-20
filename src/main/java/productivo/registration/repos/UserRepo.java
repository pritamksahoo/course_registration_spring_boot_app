package productivo.registration.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import productivo.registration.models.User;

@Component
@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    public User findByUsername(String username);
}