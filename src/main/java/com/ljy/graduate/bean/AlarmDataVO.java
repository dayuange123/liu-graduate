package com.ljy.graduate.bean;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * Author: liuzhiyuan
 * Date: 2020/5/4
 * Description:
 */
@Data
@ToString
public class AlarmDataVO {

    private List<Integer> times;
    private List<Integer> counts;
}
