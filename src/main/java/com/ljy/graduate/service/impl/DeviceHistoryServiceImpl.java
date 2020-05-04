package com.ljy.graduate.service.impl;

import com.ljy.graduate.bean.AlarmDataVO;
import com.ljy.graduate.bean.DeviceHistoryVO;
import com.ljy.graduate.bean.HistorySearch;
import com.ljy.graduate.common.PageResponse;
import com.ljy.graduate.dao.DeviceHistoryDao;
import com.ljy.graduate.entity.DeviceHistory;
import com.ljy.graduate.service.DeviceHistoryService;
import com.ljy.graduate.util.Utils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Author: liuzhiyuan
 * Date: 2020/5/4
 * Description:
 */
@Service("deviceHistoryService")
public class DeviceHistoryServiceImpl implements DeviceHistoryService {
    @Resource(name = "deviceHistoryDao")
    private DeviceHistoryDao deviceHistoryDao;

    private SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");

    @Override
    public PageResponse<DeviceHistoryVO> findAllHistoryPageable(String email, Integer page) {
        Pageable pageable = PageRequest.of(page, 1,
            Sort.by(new Sort.Order(Sort.Direction.DESC, "createTime")));
        Page<DeviceHistory> res = deviceHistoryDao.findAll(pageable);
        PageResponse<DeviceHistoryVO> pageResponse = new PageResponse<>();
        pageResponse.setCurrentPage(page);
        pageResponse.setData(res.getContent().stream().map(d -> new DeviceHistoryVO().get(d)).collect(Collectors.toList()));
        pageResponse.setTotalPages(res.getTotalPages());
        return pageResponse;
    }

    @Override
    public PageResponse<DeviceHistoryVO> findHistoryPageable(String email, HistorySearch historySearch) {
        Pageable pageable = PageRequest.of(historySearch.getPage(), 5,
            Sort.by(new Sort.Order(Sort.Direction.DESC, "createTime")));
        Page<DeviceHistory> res = deviceHistoryDao.findAll((Specification<DeviceHistory>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!historySearch.getStatus().equals(-1)) {
                predicates.add(criteriaBuilder.equal(root.get("status").as(Integer.class), historySearch.getStatus()));
            }
            if (!historySearch.getName().equals("all")) {
                predicates.add(criteriaBuilder.equal(root.get("name").as(String.class), historySearch.getName()));
            }
            if (!historySearch.getArea().equals("all")) {
                predicates.add(criteriaBuilder.equal(root.get("area").as(String.class), historySearch.getArea()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        PageResponse<DeviceHistoryVO> pageResponse = new PageResponse<>();

        pageResponse.setCurrentPage(historySearch.getPage());
        pageResponse.setData(res.getContent().stream().map(d -> new DeviceHistoryVO().get(d)).collect(Collectors.toList()));
        pageResponse.setTotalPages(res.getTotalPages());
        pageResponse.setHistorySearch(historySearch);
        return pageResponse;

    }

    @Override
    public void addHistoryData(DeviceHistory deviceHistory) {
        deviceHistoryDao.save(deviceHistory);
    }

    @Override
    public Integer getAlarmCount(String email) {
        return deviceHistoryDao.countByEmailAndStatus(email, 0);
    }

    @Override
    public AlarmDataVO getDeviceAlarmData(String email) {
        Date start = Utils.getStartTime();
        Date end = Utils.getEndTime();

        List<Map<String, Object>> deviceAlarmData = deviceHistoryDao.getDeviceAlarmData(email, format.format(start), format.format(end));
        AlarmDataVO alarmDataVO = new AlarmDataVO();
        List<Integer> times = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();
        for (Map<String, Object> deviceAlarmDatum : deviceAlarmData) {

            times.add(Integer.valueOf(deviceAlarmDatum.get("time").toString()));
            counts.add(Integer.valueOf(deviceAlarmDatum.get("count").toString()));

        }
        alarmDataVO.setCounts(counts);
        alarmDataVO.setTimes(times);
        return alarmDataVO;
    }



}
