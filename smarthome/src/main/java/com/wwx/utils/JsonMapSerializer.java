package com.wwx.utils;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class JsonMapSerializer extends JsonSerializer<Map<String, Object>> {
    @Override
    public void serialize(Map<String, Object> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeObject(value); // 将 Map 转换为 JSON 字符串
    }
}
