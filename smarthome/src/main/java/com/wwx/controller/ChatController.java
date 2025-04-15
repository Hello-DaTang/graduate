package com.wwx.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
// import org.springframework.ai.chat.messages.UserMessage;
// import org.springframework.ai.chat.model.ChatResponse;
// import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.wwx.pojo.Result;
import lombok.extern.slf4j.Slf4j;
// import reactor.core.publisher.Flux;
import reactor.core.publisher.Flux;

// import java.util.Map;


@Slf4j
@RestController
public class ChatController {
    
    private final OpenAiChatModel ChatModel;
    //存储聊天记录
    private final ChatMemory chatMemory = new InMemoryChatMemory();
    @Autowired
    public ChatController(OpenAiChatModel ChatModel) {
        this.ChatModel = ChatModel;
    }
    @GetMapping("/chat")
    public Result chat(@RequestParam String prompt) {
        ChatClient chatClient = ChatClient.create(ChatModel);
        String response = chatClient
                // 表示要发起一个对话
                .prompt()
                .system("你是智能家居系统管家，用markdown格式返回")
                // 输入用户消息
                .user(prompt)
                //响应的结果
                // call代表非流式问答，返回的结果可以是ChatResponse，也可以是Entity（转成java类型），也可以是字符串直接提取回答结果。
                .call()
                // 获取回答内容
                .content();
        return Result.success(response);
    }

    @GetMapping(value = "/chatStream",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStream(@RequestParam String prompt) {
        ChatClient chatClient = ChatClient.create(ChatModel);
        return chatClient
                .prompt()
                .system("你是智能家居系统管家，若有指定格式则按格式回答")
                // 输入单条提示词
                .user(prompt)
                // stream代表流式问答，返回的结果是ChatResponse
                .stream()
                .content();
    }

    @GetMapping(value = "/chatStream/history",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStreamWithHistory(@RequestParam String prompt,@RequestParam String sessionID){
        //设置聊天记录的最大长度为10，在内存中存储聊天记录，返回给大模型前将历史记录拼接到prompt中
        MessageChatMemoryAdvisor messageChatMemoryAdvisor = new MessageChatMemoryAdvisor(chatMemory,sessionID,10);
        return ChatClient.create(ChatModel)
                .prompt()
                .system("你是智能家居系统管家，用markdown格式返回")
                .user(prompt)
                .advisors(messageChatMemoryAdvisor)
                .stream()
                .content();
    }

    // @GetMapping("/ai/generate")
    // public Map generate(@RequestParam String message) {
    //     return Map.of("generation", this.ChatModel.call(message));
    // }
 
    // @GetMapping("/ai/generateStream")
    // public Flux<ChatResponse> generateStream(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
    //     Prompt prompt = new Prompt(new UserMessage(message));
    //     return this.ChatModel.stream(prompt);
    // }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result> handleException(Exception e) {
        log.error("Unhandled exception", e);
        Result errorResult = new Result(0, "Internal Server Error: " + e.getMessage(), null);
        return ResponseEntity.status(500).body(errorResult);
    }
}