package com.ljy.graduate.common;

import com.ljy.graduate.bean.HistorySearch;
import lombok.Data;

import java.util.List;

/**
 * Author: liuzhiyuan
 * Date: 2020/5/4
 * Description:
 */
@Data
public class PageResponse <T> {

    private Integer currentPage;

    private Integer totalPages;

    private List<T> data;

    private HistorySearch historySearch;

}
