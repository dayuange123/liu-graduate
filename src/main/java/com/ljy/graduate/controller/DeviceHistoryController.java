package com.ljy.graduate.controller;

import com.ljy.graduate.bean.DeviceHistoryVO;
import com.ljy.graduate.bean.HistorySearch;
import com.ljy.graduate.common.PageResponse;
import com.ljy.graduate.common.Response;
import com.ljy.graduate.common.ResponseMessage;
import com.ljy.graduate.service.DeviceHistoryService;
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
@RequestMapping("/history/device")
@Slf4j
public class DeviceHistoryController {

    @Resource(name = "deviceHistoryService")
    private DeviceHistoryService deviceHistoryService;

    @RequestMapping(value = "/findHistoryPageable", method = RequestMethod.POST)
    public Response<PageResponse<DeviceHistoryVO>> findHistoryPageable(@CookieValue("email") String email, HistorySearch historySearch) {

        try {
            return new Response<>(deviceHistoryService.findHistoryPageable(email,historySearch));
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(ResponseMessage.SYS_ERROR);
        }
    }

    @RequestMapping(value = "/findAllHistoryPageable", method = RequestMethod.POST)
    public Response<PageResponse<DeviceHistoryVO>> findAllHistoryPageable(@CookieValue("email") String email,Integer page) {
        try {
            return new Response<>(deviceHistoryService.findAllHistoryPageable(email,page));
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(ResponseMessage.SYS_ERROR);
        }
    }


}
