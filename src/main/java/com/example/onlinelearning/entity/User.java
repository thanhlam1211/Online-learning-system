package com.example.onlinelearning.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Admin
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "users")
@Entity
public class User implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String username;

    @Column
    private String password;

    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    @Column(name = "verification_code", updatable = false)
    private String verificationCode;

    @Column
    private String email;

    @Column(name = "full_name")
    private String fullName;

    @Column
    @Size(min=10,max=10)
    @Pattern(regexp="(^$|[0-9]{10})")
    private String phone;

    @Column
    private int gender;

    @Column(name = "avatar_url")
    private String avatar;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_provider")
    private AuthenticationProvider authenticationProvider;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Blog> blogList = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserCourse> userCourseList = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="status_id")
    private Status status;

    @OneToMany(mappedBy = "user", cascade =  CascadeType.ALL)
    private Set<UserQuiz> userQuizList = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "owner",
               joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courseList = new HashSet<>();


    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
                joinColumns =@JoinColumn(name = "user_id"),
                inverseJoinColumns =@JoinColumn(name = "role_id"))
    private Set<Role> roleList = new HashSet<>();

    public void addRole(Role role) {
        this.roleList.add(role);
    }

}
