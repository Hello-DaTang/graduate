package com.wwx.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.wwx.pojo.Result;
import com.wwx.utils.AliOSSUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class UploadController {

    @Autowired
    private AliOSSUtils aliOSSUtils;
    @PostMapping("/upload")
    public Result upload(@RequestParam("image") MultipartFile image) throws IOException{

        log.info("文件上传:{}",image);
        // 调用阿里云OSS工具类上传文件
        String url = aliOSSUtils.upload(image);
        log.info("文件上传成功:{}",url);            

        return Result.success(url);
    }

}