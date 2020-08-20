package productivo.registration.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import productivo.registration.models.Course;
import productivo.registration.models.User;
import productivo.registration.repos.UserRepo;

@Component
@Service
public class UserService {
    private UserRepo urepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepo urepo) {
        this.urepo = urepo;
    }

    public User findUserByUserName(String username) {
        return urepo.findByUsername(username);
    }

    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return urepo.save(user);
    }

    public int registerUserForCourse(User user, Course course) {
        try{
            List<Course> allCourse = user.getCourses();
            if (allCourse.contains(course)) {
                return 202;
            } else {
                allCourse.add(course);
                urepo.save(user);
                return 200;
            }

        } catch (Exception e) {

            return 500;
        }

    }

    public int unregisterForcourse(User user, Course course) {
        try {
            user.removeCourse(course);
            urepo.save(user);

            return 200;
        } catch (Exception e) {
            return 500;
        }
    }

    @Bean
    public BCryptPasswordEncoder userPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}