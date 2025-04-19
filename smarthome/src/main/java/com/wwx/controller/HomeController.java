package com.wwx.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wwx.anno.Log;
import com.wwx.pojo.PageBean;
import com.wwx.pojo.Result;
import com.wwx.pojo.SmartHome;
import com.wwx.service.HomeService;
import com.wwx.utils.ParseUserIdFromTokenUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/home")
public class HomeController {
    @Autowired
    HomeService homeService;

    @Autowired
    private ParseUserIdFromTokenUtils parseUserIdFromTokenUtils;


    @GetMapping
    public Result page(@RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            String homeName) {

        Integer userId =parseUserIdFromTokenUtils.getUserId().orElseThrow(); // 如果解析失败则抛出异常

        log.info("分页查询：{},{},{}", page, pageSize, homeName);
        PageBean pageBean = homeService.page(page, pageSize, homeName, userId);
        return Result.success(pageBean);
    }

    @Log
    @DeleteMapping("/delete/{ids}")
    public Result delete(@PathVariable List<Integer> ids) {

        log.info("删除家居：{}", ids);
        homeService.delete(ids);
        return Result.success();
    }

    @Log
    @PostMapping("/add")
    public Result save(@RequestBody SmartHome smartHome) {
        smartHome.setUserId(parseUserIdFromTokenUtils.getUserId().orElseThrow()); // 设置用户ID
        log.info("新增家居：{}", smartHome);
        homeService.save(smartHome); // 调用服务层保存方法
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        log.info("查询家居：{}", id);
        SmartHome smartHome = homeService.getById(id);
        if (smartHome == null) {
            return Result.error("未找到对应的家居信息");
        }
        return Result.success(smartHome); // 返回 SmartHome 对象
    }

    @Log
    @PutMapping("/update")
    public Result update(@RequestBody SmartHome smartHome) {
        log.info("更新家居：{}", smartHome);
        homeService.update(smartHome);
        return Result.success();
    }
}
