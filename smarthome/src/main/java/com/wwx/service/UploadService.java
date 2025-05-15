package com.wwx.service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    CompletableFuture<String> uploadFileAsync(MultipartFile file) throws IOException;
}