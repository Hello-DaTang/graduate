package com.wwx.pojo;

import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wwx.utils.JsonMapDeserializer;
import com.wwx.utils.JsonMapSerializer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmartHome {
    private Integer id; // ID
    private Integer userId; // 用户ID
    private String homeName; // 家居名称

    @JsonSerialize(using = JsonMapSerializer.class)
    @JsonDeserialize(using = JsonMapDeserializer.class)
    private Map<String, Object> deviceData; // 设备状态数据（JSON 格式）

    private String deviceDataJson; // 用于存储 JSON 字符串

    private LocalDateTime createTime; // 加入时间
    private LocalDateTime updateTime; // 更新时间
    private String location; // 家居位置
}
