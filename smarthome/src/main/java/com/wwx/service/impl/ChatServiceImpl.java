package com.wwx.service.impl;

import java.util.concurrent.CompletableFuture;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.wwx.service.ChatService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    private final OpenAiChatModel chatModel;
    private final ChatMemory chatMemory;
    
    @Autowired
    public ChatServiceImpl(OpenAiChatModel chatModel) {
        this.chatModel = chatModel;
        this.chatMemory = MessageWindowChatMemory.builder().maxMessages(20).build();
    }

    @Async("taskExecutor")
    @Override
    public CompletableFuture<String> chatAsync(String prompt) {
        log.info("开始异步处理普通聊天请求: {}", prompt);
        ChatClient chatClient = ChatClient.create(chatModel);
        String response = chatClient
                .prompt()
                .system("你是智能家居系统管家，用markdown格式返回")
                .user(prompt)
                .call()
                .content();
        log.info("聊天请求异步处理完成");
        return CompletableFuture.completedFuture(response);
    }

    @Async("taskExecutor")
    @Override
    public CompletableFuture<String> postChatAsync(String prompt) {
        log.info("开始异步处理POST聊天请求");
        ChatClient chatClient = ChatClient.create(chatModel);
        String systemPrompt = "你是一位智能家居专家AI，请根据用户的要求以及他提供的json格式设备信息、天气数据和历史操作习惯，提供最合适的智能家居配置方案。\n" +
                "请分析设备数据、天气情况和用户操作习惯，提供具体的调整建议，以优化用户的智能家居体验。\n" +
                "用户可以通过你的建议直接控制家中设备，因此你需要返回详细的设备控制参数。\n\n" +
                "你的回复必须包含:\n" +
                "{\n" +
                "  \"devices\": [\n" +
                "    {\n" +
                "      \"id\": 设备ID(数字),\n" +
                "      \"deviceData\": {\n" +
                "        \"status\": \"on或off\",\n" +
                "        // 其他特定设备的参数，你需要依据用户传来的数据分析哪些需要修改参数，但返回所有参数包括没修改的参数，设备类型可能包括但不限于：\n" +
                "        // 窗帘: position(0-100数字), material, mode\n" +
                "        // 空调: temperature(数字), fanSpeed, mode\n" +
                "        // 灯光: brightness(0-100数字), color, mode\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}\n" +
                "只建议更新必要的设备和参数，不要修改用户没有必要改变的参数值，但要确保返回的数据完整，包含设备ID和所有参数包括更新和不更新的。";

        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .responseFormat(ResponseFormat.builder().type(ResponseFormat.Type.JSON_OBJECT).build())
                .build();
        
        String response = chatClient
                .prompt()
                .system(systemPrompt)
                .user(prompt)
                .options(options)
                .call()
                .content();
        
        log.info("POST聊天请求异步处理完成");
        return CompletableFuture.completedFuture(response);
    }

    @Override
    public Flux<String> streamChatAsync(String prompt) {
        log.info("开始处理流式聊天请求: {}", prompt);
        ChatClient chatClient = ChatClient.create(chatModel);
        return chatClient
                .prompt()
                .system("你是智能家居系统管家，若有指定格式则按格式回答")
                .user(prompt)
                .stream()
                .content()
                .doOnComplete(() -> log.info("流式聊天请求处理完成"))
                .doOnError(e -> log.error("流式聊天请求处理失败", e));
    }
    
    @Override
    public Flux<String> streamChatWithHistoryAsync(String prompt, String sessionId) {
        log.info("开始处理带历史记录的流式聊天请求: {}, 会话ID: {}", prompt, sessionId);
        String systemPrompt = "你是智能家居系统管家，用户会给你设备信息、天气数据和历史操作习惯，请按照以下规则回答用户问题：\n\n" +
                        "1. 使用 Markdown 格式组织你的回答，使其清晰易读\n" +
                "2. 当前天气数据：\n" +
                "   - 温度\n" +
                "   - 湿度\n" +
                "   - 天气状况\n" +
                "   - 空气质量\n" +
                "   - PM2.5\n" +
                "3. 基于天气数据，提供合理的出行建议\n" +
                "4. 如果用户询问或需要调整家中设备，请提醒用户可以前往首页使用\"一键建议\"功能\n" +
                "5. 为用户提供的建议应考虑天气状况、室内温湿度和用户日常习惯\n" +
                "6. 当提及设备调整时，请说明具体的调整参数，例如：\n" +
                "   - 窗帘开合度：0-100%\n" +
                "   - 空调温度：16-30°C\n" +
                "   - 灯光亮度：0-100%\n\n" +
                "请在回答开始使用适当的问候语，结束时可提供更多帮助的提示。";
                
        var messageChatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory)
                .conversationId(sessionId)
                .scheduler(Schedulers.boundedElastic())
                .build();
        
        return ChatClient.create(chatModel)
                .prompt()
                .system(systemPrompt)
                .user(prompt)
                .advisors(messageChatMemoryAdvisor)
                .stream()
                .content()
                .doOnComplete(() -> log.info("带历史记录的流式聊天请求处理完成"));
    }
}