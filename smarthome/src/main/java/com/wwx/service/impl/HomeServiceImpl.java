package com.wwx.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wwx.mapper.HomeMapper;
import com.wwx.pojo.PageBean;
import com.wwx.pojo.SmartHome;
import com.wwx.service.HomeService;

@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    private HomeMapper homeMapper;

    private final ObjectMapper objectMapper = new ObjectMapper(); // 用于 JSON 转换

    @Override
    public PageBean page(Integer page, Integer pageSize, String homeName, Integer userId) {
        // 1. 设置分页参数
        PageHelper.startPage(page, pageSize);
        // 2. 执行查询
        try (Page<SmartHome> p = (Page<SmartHome>) homeMapper.list(homeName, userId)) {
            // 3. 处理每个SmartHome对象，将JSON字符串转换为Map
            for (SmartHome smartHome : p.getResult()) {
                if (smartHome.getDeviceDataJson() != null) {
                    try {
                        // 将JSON字符串转换为Map
                        Map<String, Object> deviceData = objectMapper.readValue(smartHome.getDeviceDataJson(), Map.class);
                        smartHome.setDeviceData(deviceData);
                    } catch (JsonProcessingException e) {
                        System.err.println("Failed to parse deviceDataJson to Map: " + e.getMessage());
                        // 可以选择继续处理其他记录而不是抛出异常
                    }
                }
            }
            // 4. 封装PageBean对象
            return new PageBean(p.getTotal(), p.getResult());
        }
    }

    @Override
    public void delete(List<Integer> ids) {
        homeMapper.delete(ids);
    }

    @Override
    public void save(SmartHome smartHome) {
        smartHome.setCreateTime(LocalDateTime.now());
        smartHome.setUpdateTime(LocalDateTime.now());

        // 将 deviceData 转换为 JSON 字符串
        try {
            if (smartHome.getDeviceData() != null) {
                String jsonString = objectMapper.writeValueAsString(smartHome.getDeviceData());
                smartHome.setDeviceDataJson(jsonString); // 设置为 JSON 字符串
            }
        } catch (JsonProcessingException e) {
            System.err.println("Failed to convert deviceData to JSON: " + e.getMessage());
            throw new RuntimeException("Failed to convert deviceData to JSON", e);
        }

        homeMapper.insert(smartHome);
    }

    @Override
    public SmartHome getById(Integer id) {
        SmartHome smartHome = homeMapper.getById(id);
        if (smartHome != null && smartHome.getDeviceDataJson() != null) {
            try {
                // 将 JSON 字符串转换为 Map
                Map<String, Object> deviceData = objectMapper.readValue(smartHome.getDeviceDataJson(), Map.class);
                smartHome.setDeviceData(deviceData);
            } catch (JsonProcessingException e) {
                // 添加日志记录
                System.err.println("Failed to parse deviceDataJson to Map: " + e.getMessage());
                throw new RuntimeException("Failed to parse deviceDataJson to Map", e);
            }
        } else {
            // 添加日志记录
            System.out.println("No deviceDataJson found for SmartHome with ID: " + id);
        }
        return smartHome;
    }

    @Override
    public void update(SmartHome smartHome) {
        smartHome.setUpdateTime(LocalDateTime.now());

        // 将 deviceData 转换为 JSON 字符串
        try {
            if (smartHome.getDeviceData() != null) {
                String jsonString = objectMapper.writeValueAsString(smartHome.getDeviceData());
                smartHome.setDeviceDataJson(jsonString); // 设置为 JSON 字符串
            }
        } catch (JsonProcessingException e) {
            System.err.println("Failed to convert deviceData to JSON: " + e.getMessage());
            throw new RuntimeException("Failed to convert deviceData to JSON", e);
        }

        homeMapper.update(smartHome);
    }
}
