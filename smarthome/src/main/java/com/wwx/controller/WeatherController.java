package com.wwx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wwx.utils.CityNameToIdUtils;
import com.wwx.utils.MojiWeatherUtils;

@RestController
public class WeatherController {
    @Autowired
    private MojiWeatherUtils mojiWeatherUtils;
    
    @GetMapping("/weather")
    public String getWeather(@RequestParam String cityName) throws Exception {
        String cityId = CityNameToIdUtils.getCityId(cityName);
        return mojiWeatherUtils.getWeather(cityId);
    }
    @GetMapping("/weather/cityId")
    public String getCityName(@RequestParam String cityName) throws Exception {
        String cityId = CityNameToIdUtils.getCityId(cityName);
        return cityId;
    }
}
