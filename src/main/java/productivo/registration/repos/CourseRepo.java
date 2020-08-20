package productivo.registration.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import productivo.registration.models.Course;

@Component
@Repository
public interface CourseRepo extends JpaRepository<Course, Long> {
    
}