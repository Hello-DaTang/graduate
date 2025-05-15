package com.wwx.controller;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wwx.pojo.Result;
import com.wwx.service.ChatService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
public class ChatController {
    
    @Autowired
    private ChatService chatService;
    
    @GetMapping("/chat")
    public CompletableFuture<Result> chat(@RequestParam String prompt) {
        log.info("接收到普通聊天请求: {}", prompt);
        return chatService.chatAsync(prompt)
                .thenApply(response -> {
                    log.info("聊天异步处理完成");
                    return Result.success(response);
                })
                .exceptionally(ex -> {
                    log.error("聊天异步处理失败", ex);
                    return Result.error("聊天请求失败: " + ex.getMessage());
                });
    }
    
    @PostMapping("/postchat")
    public CompletableFuture<Result> postchat(@RequestBody String prompt) {
        log.info("接收到POST聊天请求");
        return chatService.postChatAsync(prompt)
                .thenApply(response -> {
                    log.info("POST聊天异步处理完成");
                    return Result.success(response);
                })
                .exceptionally(ex -> {
                    log.error("POST聊天异步处理失败", ex);
                    return Result.error("聊天请求失败: " + ex.getMessage());
                });
    }
    
    @GetMapping(value = "/chatStream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStream(@RequestParam String prompt) {
        log.info("接收到流式聊天请求: {}", prompt);
        return chatService.streamChatAsync(prompt);
    }
    
    @GetMapping(value = "/chatStream/history", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStreamWithHistory(@RequestParam String prompt, @RequestParam String sessionID) {
        log.info("接收到带历史记录的流式聊天请求: {}, 会话ID: {}", prompt, sessionID);
        return chatService.streamChatWithHistoryAsync(prompt, sessionID);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result> handleException(Exception e) {
        log.error("未捕获的异常", e);
        Result errorResult = Result.error("Internal Server Error: " + e.getMessage());
        return ResponseEntity.status(500).body(errorResult);
    }
}