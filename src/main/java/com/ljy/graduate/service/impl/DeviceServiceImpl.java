package com.ljy.graduate.service.impl;

import com.ljy.graduate.bean.DeviceVO;
import com.ljy.graduate.common.Response;
import com.ljy.graduate.dao.DeviceDao;
import com.ljy.graduate.entity.Device;
import com.ljy.graduate.service.DeviceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: liuzhiyuan
 * Date: 2020/5/3
 * Description:
 */
@Service("deviceService")
public class DeviceServiceImpl implements DeviceService {
    @Resource(name = "deviceDao")
    private DeviceDao deviceDao;
    private SimpleDateFormat format = new SimpleDateFormat( "yyy-MM-dd HH:mm:ss");


    @Override
    public Response<Boolean> addDevice(String email, Device device) {

        device.setCreateTime(new Date());
        device.setEmail(email);
        deviceDao.save(device);
        return new Response<>(true);
    }

    @Override
    public Response<List<DeviceVO>> getAllDevice(String email) {
        return new Response<>(deviceDao.findAllByEmail(email).stream().
            map(d->{
                DeviceVO deviceVO=new DeviceVO();
                deviceVO.setCreateTime(format.format(d.getCreateTime()));
                deviceVO.setDeviceArea(d.getDeviceArea());
                deviceVO.setDeviceName(d.getDeviceName());
                deviceVO.setReportThreshold(d.getReportThreshold());
                deviceVO.setId(d.getId());
                return deviceVO;
            }).collect(Collectors.toList()));
    }

    @Override
    public Integer getDeviceCount(String email) {
        return deviceDao.countByEmail(email);
    }

    @Override
    public void deleteDevice(String email, Integer id) {
        deviceDao.deleteById(id);
    }
}
