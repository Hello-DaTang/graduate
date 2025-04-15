package com.wwx.utils;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@lombok.extern.slf4j.Slf4j
@Component
public class MojiWeatherUtils {

    @Autowired
    private MojiWeatherProperties props;

    public String getWeather(String cityId) {
        String method = "POST";
        Map<String, String> headers = new HashMap<String, String>();
	    //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
	    headers.put("Authorization", "APPCODE " + props.getAppCode());
	    //根据API的要求，定义相对应的Content-Type
	    headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	    Map<String, String> querys = new HashMap<String, String>();
	    Map<String, String> bodys = new HashMap<String, String>();
	    bodys.put("token", props.getToken());
	    bodys.put("cityId", cityId);


	    try {
	    	HttpResponse response = HttpUtils.doPost(props.getHost(), props.getPath(), method, headers, querys, bodys);
            return EntityUtils.toString(response.getEntity());

		} catch (Exception e) {
			e.printStackTrace();
			return "Error: Unable to get weather data";
		}
    }
}