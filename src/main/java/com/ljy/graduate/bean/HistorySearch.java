package com.ljy.graduate.bean;

import lombok.Data;

/**
 * Author: liuzhiyuan
 * Date: 2020/5/4
 * Description:
 */
@Data
public class HistorySearch {
    /**
     * 0 异常 1正常
     */
    private Integer status;

    private String name;

    private String area;

    private Integer page;



}
