package com.ljy.graduate.entity;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import javax.persistence.*;
import java.util.Date;

/**
 * Author: liuzhiyuan
 * Date: 2020/5/4
 * Description:
 */
@Entity
@Table(name = "environment_history")
@Data
@Builder
public class EnvironmentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;


    @Column(name = "area")
    private String area;


    @Column(name = "update_time")
    private String updateTime;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "email")
    private String email;

    @Column(name = "current")
    private Double current;

    @Column(name = "status")
    private Integer status;

    @Tolerate
    public EnvironmentHistory() {
    }
}
