package productivo.registration.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import productivo.registration.models.Role;

@Component
@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {
    public Role findByName(String name);
}