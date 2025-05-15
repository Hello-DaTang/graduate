package com.wwx.service.impl;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.wwx.service.UploadService;
import com.wwx.utils.AliOSSUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    private AliOSSUtils aliOSSUtils;

    @Async("taskExecutor")
    @Override
    public CompletableFuture<String> uploadFileAsync(MultipartFile file) throws IOException {
        log.info("异步处理文件上传: {}", file.getOriginalFilename());
        String url = aliOSSUtils.upload(file);
        log.info("文件上传成功: {}", url);
        return CompletableFuture.completedFuture(url);
    }
}