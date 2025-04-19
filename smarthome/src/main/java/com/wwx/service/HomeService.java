package com.wwx.service;

import java.time.LocalDate;
import java.util.List;

import com.wwx.pojo.PageBean;
import com.wwx.pojo.SmartHome;

public interface HomeService {

    PageBean page(Integer page, Integer pageSize, String homeName, Integer userId,String location);

    void delete(List<Integer> ids);

    void save(SmartHome smartHome);

    SmartHome getById(Integer id);

    void update(SmartHome smartHome);

}
