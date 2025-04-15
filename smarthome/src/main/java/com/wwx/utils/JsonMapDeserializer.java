package com.wwx.utils;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonMapDeserializer extends JsonDeserializer<Map<String, Object>> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Map<String, Object> deserialize(com.fasterxml.jackson.core.JsonParser p, DeserializationContext ctxt) throws IOException {
        return objectMapper.readValue(p, new TypeReference<Map<String, Object>>() {});
    }
}
