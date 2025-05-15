package com.wwx.controller;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.wwx.pojo.Result;
import com.wwx.service.UploadService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @PostMapping("/upload")
    public CompletableFuture<Result> upload(@RequestParam("image") MultipartFile image) throws IOException {
        log.info("接收到文件上传请求: {}", image.getOriginalFilename());
        return uploadService.uploadFileAsync(image)
                .thenApply(url -> {
                    log.info("文件上传异步处理完成: {}", url);
                    return Result.success(url);
                })
                .exceptionally(ex -> {
                    log.error("文件上传异步处理失败", ex);
                    return Result.error("文件上传失败: " + ex.getMessage());
                });
    }
}