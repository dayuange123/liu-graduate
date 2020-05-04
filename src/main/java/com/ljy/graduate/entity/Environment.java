package com.ljy.graduate.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Author: liuzhiyuan
 * Date: 2020/5/3
 * Description:
 */
@Entity
@Table(name = "environment")
@Data
public class Environment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "e_name")
    private String name;

    @Column(name = "e_area")
    private String area;

    @Column(name = "report_threshold")
    private Double reportThreshold;

    @Column(name = "e_normal")
    private Double normal;

    @Column(name = "create_time")
    private Date create_time;

    @Column(name = "email")
    private String email;
}
