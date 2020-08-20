package productivo.registration.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import productivo.registration.models.Course;
import productivo.registration.models.User;
import productivo.registration.repos.CourseRepo;
import productivo.registration.repos.UserRepo;

@Component
@Service
public class CourseService {
    
    @Autowired
    private CourseRepo crepo;

    @Autowired
    private UserRepo urepo;

    public Course findCourseById(Long id) {
        Optional<Course> course = crepo.findById(id);

        if(course.isPresent()) {
            return course.get();
        } else {
            return null;
        }
    }

    public List<Course> findAllCourseDetails() {
        return crepo.findAll();
    }

    public Course saveCourse(Course course) {
        return crepo.save(course);
    }

    public int deleteCourse(Course course) {
        try {
            List<User> students = course.getUsers();
            User coordinator = course.getCoordinator();

            for (User user : students) {
                user.removeCourse(course);
                urepo.save(user);
            }

            coordinator.removeCreatedCourse(course);
            urepo.save(coordinator);

            User user = course.getCoordinator();
            user.removeCreatedCourse(course);
            urepo.save(user);

            crepo.deleteById(course.getId());
            return 200;
        } catch (Exception e) {
            return 500;
        }
    }
}