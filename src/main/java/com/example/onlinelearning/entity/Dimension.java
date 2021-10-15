package com.example.onlinelearning.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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
@Entity
@Table(name = "dimension")
public class    Dimension implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String name;

    @Column
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private DimensionType dimensionType;

    @ManyToMany(mappedBy = "dimensionList")
    private Set<QuestionBank> questionBankList = new HashSet<>();

    @ManyToMany(mappedBy = "dimensionList")
    private Set<Course> courseList = new HashSet<>();

}
