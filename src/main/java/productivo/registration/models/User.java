package productivo.registration.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.stereotype.Component;

@Component
@Entity
@Table(name = "userinfo", schema = "productivo")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "uid")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Transient
    private String confirmPassword;

    @ManyToMany
    @JoinTable(name = "role_assignment", schema = "productivo",
    joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "uid"), 
    inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "rid"))
    private List<Role> roles;

    @ManyToMany
    @JoinTable(name = "course_registration", schema = "productivo",
    joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "uid"), 
    inverseJoinColumns = @JoinColumn(name = "course_id", referencedColumnName = "cid"))
    private List<Course> courses;

    @OneToMany(mappedBy = "coordinator")
    private List<Course> mycourses;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public void removeCourse(Course course) {
        this.courses.remove(course);
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }
    
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public List<Course> getMycourses() {
        return mycourses;
    }

    public void setMycourses(List<Course> mycourses) {
        this.mycourses = mycourses;
    }

    public void removeCreatedCourse(Course course) {
        this.mycourses.remove(course);
    }
}