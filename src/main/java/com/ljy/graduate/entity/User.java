package com.ljy.graduate.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Author: liuzhiyuan
 * Date: 2020/3/23
 * Description:
 */
@Entity
@Table(name = "user")
@Data
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "u_name")
    private String userName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "status")
    private Integer status;
}
