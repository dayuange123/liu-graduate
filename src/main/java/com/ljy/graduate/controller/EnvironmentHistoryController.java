package com.ljy.graduate.controller;

import com.ljy.graduate.bean.DeviceHistoryVO;
import com.ljy.graduate.bean.EnvironmentHistoryVO;
import com.ljy.graduate.bean.HistorySearch;
import com.ljy.graduate.common.PageResponse;
import com.ljy.graduate.common.Response;
import com.ljy.graduate.common.ResponseMessage;
import com.ljy.graduate.entity.EnvironmentHistory;
import com.ljy.graduate.service.DeviceHistoryService;
import com.ljy.graduate.service.EnvironmentHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Author: liuzhiyuan
 * Date: 2020/5/4
 * Description:
 */

@RestController
@RequestMapping("/history/environment")
@Slf4j
public class EnvironmentHistoryController {

    @Resource(name = "environmentHistoryService")
    private EnvironmentHistoryService environmentHistoryService;

    @RequestMapping(value = "/findHistoryPageable", method = RequestMethod.POST)
    public Response<PageResponse<EnvironmentHistoryVO>> findHistoryPageable(@CookieValue("email") String email, HistorySearch historySearch) {

        try {
            return new Response<>(environmentHistoryService.findHistoryPageable(email,historySearch));
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(ResponseMessage.SYS_ERROR);
        }
    }




}
