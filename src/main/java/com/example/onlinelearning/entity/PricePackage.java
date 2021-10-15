package com.example.onlinelearning.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
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
@Table(name = "price_package")
public class PricePackage implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column(name = "list_price")
    private float listPrice;

    @Column(name = "sale_price")
    private float salePrice;

    @Column
    private int duraion;

    @Column
    private String text;

    @Column
    private String discount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private Status status;

    @ManyToMany(mappedBy = "pricePackageList")
    private Set<Course> courseList = new HashSet<>();
}
