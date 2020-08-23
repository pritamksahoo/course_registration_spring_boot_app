package productivo.registration.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import productivo.registration.models.Course;
import productivo.registration.models.Role;
import productivo.registration.models.User;
// import productivo.registration.models.User;
import productivo.registration.services.CourseService;
import productivo.registration.services.RoleService;
import productivo.registration.services.UserService;

@Component
@Controller
public class NoAuthController {

    @Autowired
    private CourseService cserv;

    @Autowired
    private RoleService rserv;

    @Autowired
    private UserService userv;

    @GetMapping("/createRoles")
    public String roles() {

        Role r = new Role();
        r.setName("STUDENT");
        rserv.saveRole(r);

        r = new Role();
        r.setName("TEACHER");
        rserv.saveRole(r);

        return "Hello, world";
    }

    @GetMapping("/teacherEntry")
    public String teacherEntry() {
        User user = new User();
        user.setName("Ashok Kumar Sahoo");
        user.setEmail("asahoo777@gmail.com");
        user.setUsername("ashok");
        user.setPassword("ashok");
        // user.setConfirmPassword("amrik");

        List<Role> roles = new ArrayList<>();
        
        Role r = rserv.findRoleByRoleName("TEACHER");
        roles.add(r);

        user.setRoles(roles);
        userv.saveUser(user);
        return "Hello, world";
    }

    @GetMapping("/studentEntry")
    public String studentEntry() {
        User user = new User();
        user.setName("Pritam Kumar Sahoo");
        user.setEmail("pritam.ndp@gmail.com");
        user.setUsername("pks");
        user.setPassword("pks");
        // user.setConfirmPassword("amrik");

        List<Role> roles = new ArrayList<>();
        
        Role r = rserv.findRoleByRoleName("STUDENT");
        roles.add(r);

        user.setRoles(roles);
        userv.saveUser(user);
        return "Hello, world";
    }
    
    @GetMapping({"/", "/home"})
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            String user = auth.getName();
            model.addAttribute("current_user", user);
        } else {
            model.addAttribute("current_user", "null");
        }

        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String user = auth.getName();

        if (user != "anonymousUser") {
            model.addAttribute("current_user", user);
            return "index";
        } else {
            model.addAttribute("current_user", "anonymousUser");
        }
        return "login";
    }

    @GetMapping("/available_courses")
    public String courseRegistration(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String user = auth.getName();

        List<Course> allCourses = cserv.findAllCourseDetails();

        if (user != "anonymousUser") {
            model.addAttribute("current_user", user);
            User activeUser = userv.findUserByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
            List<Course> rectifiedCourses = new ArrayList<>();

            for (Course course : allCourses) {
                if (course.getUsers().contains(activeUser)) {

                } else {
                    rectifiedCourses.add(course);
                }
            }

            model.addAttribute("all_course", rectifiedCourses);
        } else {
            model.addAttribute("current_user", "anonymousUser");
            model.addAttribute("all_course", allCourses);
        }

        return "courses";
    }
}