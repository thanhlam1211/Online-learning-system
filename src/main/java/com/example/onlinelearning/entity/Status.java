package com.example.onlinelearning.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Admin
 */
@Data
@Entity
@Table(name = "status")
public class Status implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String value;

    @OneToMany(mappedBy = "status", cascade = CascadeType.ALL)
    private Set<User> userList = new HashSet<>();



}
