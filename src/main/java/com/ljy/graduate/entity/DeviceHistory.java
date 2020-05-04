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
@Table(name = "device_history")
@Data
@Builder
public class DeviceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;


    @Column(name = "area")
    private String area;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_time")
    private String endTime;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "email")
    private String email;
    @Column(name = "use_time")
    private Integer useTime;

    @Column(name = "status")
    private Integer status;

    @Tolerate
    public DeviceHistory() {
    }
}
