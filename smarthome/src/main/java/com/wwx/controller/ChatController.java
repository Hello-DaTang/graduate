package com.wwx.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
// import org.springframework.ai.chat.messages.UserMessage;
// import org.springframework.ai.chat.model.ChatResponse;
// import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    @PostMapping("/postchat")
    public Result postchat(@RequestBody String prompt) {
        ChatClient chatClient = ChatClient.create(ChatModel);
        String systemPrompt = "你是一位智能家居专家AI，请根据用户的设备信息、天气数据和历史操作习惯，提供最合适的智能家居配置方案。\n" +
        "请分析设备数据、天气情况和用户操作习惯，提供具体的调整建议，以优化用户的智能家居体验。\n" +
        "用户可以通过你的建议直接控制家中设备，因此你需要返回详细的设备控制参数。\n\n" +
        "你的回复必须包含:\n" +
        "一个JSON格式的设备更新指令，必须用三个反引号包裹，形如：\n" +
        "```json\n" +
        "{\n" +
        "  \"devices\": [\n" +
        "    {\n" +
        "      \"id\": 设备ID(数字),\n" +
        "      \"deviceData\": {\n" +
        "        \"status\": \"on或off\",\n" +
        "        // 其他特定设备的参数，根据设备类型可能包括：\n" +
        "        // 窗帘: position(0-100数字), material, mode\n" +
        "        // 空调: temperature(数字), fanSpeed, mode\n" +
        "        // 灯光: brightness(0-100数字), color, mode\n" +
        "      }\n" +
        "    }\n" +
        "  ]\n" +
        "}\n" +
        "```\n" +
        "只建议更新必要的设备和参数，不要修改用户没有必要改变的参数值。 确保返回的JSON格式正确，包含设备ID和需要更新的参数。";

        // 动态设置 JSON_OBJECT 格式
        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .responseFormat(ResponseFormat.builder().type(ResponseFormat.Type.JSON_OBJECT).build())
                .build();
        String response = chatClient
                .prompt()
                .system(systemPrompt)
                // 输入用户消息
                .user(prompt)
                .options(options)
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