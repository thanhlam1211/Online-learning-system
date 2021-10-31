package com.example.onlinelearning.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Admin
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "course")
public class Course implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String title;

    @Column(name = "short_description", columnDefinition = "text")
    private String shortDescription;

    @Column(columnDefinition = "text")
    private String description;

    @Column
    private int featured;

    @Column(name = "thumbnail_url")
    private String thumbnail;

    @Column
    @CreatedDate
    private Date createdDate;

    @ManyToMany(mappedBy = "courseList")
    private Set<User> userList = new HashSet<>();

    public void addUser(User user) {
        this.userList.add(user);
    }

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private Set<UserCourse> userCourseList = new HashSet<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private Set<QuestionBank> questionBankList = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private Status status;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private Set<Quiz> quizList;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "course_package",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "package_id"))
    private Set<PricePackage> pricePackageList = new HashSet<>();

    public PricePackage minSalePrice() {
        Set<PricePackage> pricePackageList = getPricePackageList();
        double min = 0;
        int count = 0;
        PricePackage pricePackage = new PricePackage();
        for (PricePackage element : pricePackageList) {
            if (count == 0) {
                min = element.getSalePrice();
                pricePackage = element;
                count = -1;
            }
            if (min > element.getSalePrice()) {
                min = element.getSalePrice();
                pricePackage = element;
            }
        }
        return pricePackage;
    }

    public String getDateCreate() {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String date = df.format(createdDate);
        return date;
    }

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private Set<Topic> topicList;

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name = "course_dimension",
            joinColumns =@JoinColumn(name = "course_id"),
            inverseJoinColumns =@JoinColumn(name = "dimension_id"))
    private Set<Dimension> dimensionList = new HashSet<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private Set<Lesson> lessonList;
}
