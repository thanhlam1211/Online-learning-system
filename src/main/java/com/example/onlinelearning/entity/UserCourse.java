package com.example.onlinelearning.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Admin
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_course")
public class UserCourse implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE)
    private Date startDate;

    @Column
//    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE)
    private Date endDate;

    @Column
    private int registrationStatus;

    @Column
    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE)
    private Date registrationDate;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "pricePackage_id")
    private PricePackage pricePackage;
}
