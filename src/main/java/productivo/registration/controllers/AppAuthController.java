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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import productivo.registration.models.Course;
import productivo.registration.models.Role;
import productivo.registration.models.User;
import productivo.registration.services.CourseService;
import productivo.registration.services.RoleService;
import productivo.registration.services.UserService;

@Component
@Controller
public class AppAuthController {

    @Autowired
    private UserService userv;
    
    @Autowired
    private CourseService cserv;

    @Autowired
    private RoleService rserv;

    @GetMapping("/authRegistration")
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

    @PostMapping("/register")
    public String registerForCourse(@RequestParam("cid") Long cid, Model model) {
        Course course = cserv.findCourseById(cid);
        User user = userv.findUserByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
        int status = 500;

        if (course != null) {
            status = userv.registerUserForCourse(user, course);
        }

        if (status == 200) {
            model.addAttribute("registerstatus", status);
            model.addAttribute("message", "Successfully registered for " + course.getName());
        } else if (status == 202) {
            model.addAttribute("registerstatus", status);
            model.addAttribute("message", "Already registered for " + course.getName());
        } else {
            model.addAttribute("registerstatus", status);
            model.addAttribute("message", "Registration failed!");
        }

        model.addAttribute("registered_courses", user.getCourses());

        return "mycourse";
    }

    @GetMapping("/registered_courses")
    public String registered_courses(Model model) {
        User user = userv.findUserByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute("registered_courses", user.getCourses());
        return "mycourse";
    }

    @PostMapping("/unregister")
    public String unregisterCourse(@RequestParam("cid") Long cid, Model model) {
        Course course = cserv.findCourseById(cid);
        User user = userv.findUserByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
        int status = 500;

        if (course != null) {
            status = userv.unregisterForcourse(user, course);
        }

        if (status == 200) {
            model.addAttribute("registerstatus", status);
            model.addAttribute("message", "Successfully unregistered for " + course.getName());
        } else {
            model.addAttribute("registerstatus", status);
            model.addAttribute("message", "Unregistration failed!");
        }

        model.addAttribute("registered_courses", user.getCourses());

        return "mycourse";
    }

    @GetMapping("/yourcourses")
    public String allOfYourcourses(Model model) {
        User user = userv.findUserByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
        Role role = rserv.findRoleByRoleName("TEACHER");
        if (user.getRoles().contains(role)) {
            List<Course> allMyCourses = user.getMycourses();
            model.addAttribute("authorization", true);
            model.addAttribute("my_courses", allMyCourses);
        } else {
            model.addAttribute("authorization", false);
        }

        return "createdcourse";
    }

    @PostMapping("/create_course")
    public String create_course(@RequestParam("cname") String cname, Model model) {
        Course course = new Course();
        User user = userv.findUserByUserName(SecurityContextHolder.getContext().getAuthentication().getName());

        course.setName(cname);
        course.setCoordinator(user);

        List<Course> mycourse = user.getMycourses();
        mycourse.add(course);
        user.setMycourses(mycourse);
        userv.saveUser(user);

        if (cserv.saveCourse(course) != null) {
            model.addAttribute("createstatus", 200);
            model.addAttribute("message", "Course successfully created!");
        } else {
            model.addAttribute("createstatus", 500);
            model.addAttribute("message", "Course creation failed!");
        }

        List<Course> allMyCourses = user.getMycourses();
        model.addAttribute("authorization", true);
        model.addAttribute("my_courses", allMyCourses);

        return "createdcourse";
    }

    @PostMapping("/delete_course")
    public String delete_course(@RequestParam("cid") Long cid, Model model) {
        User user = userv.findUserByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
        Course course = cserv.findCourseById(cid);
        int status = cserv.deleteCourse(course);

        if (status == 200) {
            model.addAttribute("deletestatus", 200);
            model.addAttribute("message", "Course successfully deleted");
        } else {
            model.addAttribute("deletestatus", 500);
            model.addAttribute("message", "Course deletion failed!");
        }

        List<Course> allMyCourses = user.getMycourses();
        model.addAttribute("authorization", true);
        model.addAttribute("my_courses", allMyCourses);

        return "createdcourse";
    }
}