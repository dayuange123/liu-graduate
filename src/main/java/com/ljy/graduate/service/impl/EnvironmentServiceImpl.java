package com.ljy.graduate.service.impl;

import com.ljy.graduate.bean.DeviceVO;
import com.ljy.graduate.bean.EnvironmentVO;
import com.ljy.graduate.common.Response;
import com.ljy.graduate.dao.EnvironmentDao;
import com.ljy.graduate.entity.Environment;
import com.ljy.graduate.service.EnvironmentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.ljy.graduate.common.ResponseMessage.AREA_NAME_EXIST;

/**
 * Author: liuzhiyuan
 * Date: 2020/5/3
 * Description:
 */
@Service("environmentService")
public class EnvironmentServiceImpl implements EnvironmentService {

    @Resource(name = "environmentDao")
    private EnvironmentDao environmentDao;

    private SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");

    @Override
    public Response<Boolean> addEnvironment(String email, Environment environment) {
        Environment e = environmentDao.findByEmailAndAreaAndName(email, environment.getArea(), environment.getName());
        if(e!=null){
            return new Response<>(AREA_NAME_EXIST);
        }
        environment.setCreate_time(new Date());
        environment.setEmail(email);
        environmentDao.save(environment);
        return new Response<>(true);
    }

    @Override
    public Response<List<EnvironmentVO>> getAllDevice(String email) {
        List<EnvironmentVO> res = environmentDao.findAllByEmail(email).stream()
            .map(e ->
            {
                EnvironmentVO environmentVO = new EnvironmentVO();
                environmentVO.setArea(e.getArea());
                environmentVO.setCreateTime(format.format(e.getCreate_time()));
                environmentVO.setId(e.getId());
                environmentVO.setName(e.getName());
                environmentVO.setNormal(e.getNormal());
                environmentVO.setReportThreshold(e.getReportThreshold());
                return environmentVO;
            }).collect(Collectors.toList());
        return new Response<>(res);
    }

    @Override
    public void deleteEnvironment(String email, Integer id) {
        environmentDao.deleteById(id);
    }
}
