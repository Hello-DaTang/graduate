package com.wwx.service;

import java.util.concurrent.CompletableFuture;

import reactor.core.publisher.Flux;

public interface ChatService {
    CompletableFuture<String> chatAsync(String prompt);
    CompletableFuture<String> postChatAsync(String prompt);
    Flux<String> streamChatAsync(String prompt);
    Flux<String> streamChatWithHistoryAsync(String prompt, String sessionId);
}