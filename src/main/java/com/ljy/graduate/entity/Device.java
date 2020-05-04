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
@Table(name = "device")
@Data
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "device_name")
    private String deviceName;

    @Column(name = "device_area")
    private String deviceArea;

    @Column(name = "report_threshold")
    private String reportThreshold;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "email")
    private String email;
}
