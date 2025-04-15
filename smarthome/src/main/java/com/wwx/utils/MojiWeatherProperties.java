package com.wwx.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "moji.weather")
public class MojiWeatherProperties {

    private String host ;
    private String path ;
    private String appCode;
    private String token;
}
