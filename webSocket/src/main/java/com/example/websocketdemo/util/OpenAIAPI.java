package com.example.websocketdemo.util;


import cn.hutool.core.convert.ConvertException;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.plexpt.chatgpt.util.Proxys;
import lombok.experimental.UtilityClass;

import java.net.Proxy;
import java.net.ProxySelector;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Astar
 * ClassName:OpenAIAPI.java
 * date:2023-03-03 16:49
 * Description:
 */
@UtilityClass
public class OpenAIAPI {
    /**
     * 聊天端点
     */
    String chatEndpoint = "https://api.openai.com/v1/chat/completions";
    /**
     * api密匙
     */
    String apiKey = "你的API密匙";

    /**
     * 发送消息
     *
     * @param txt 内容
     * @return {@link String}
     */
    public static String chat(String txt) {
        Map<String,String> headers = new HashMap<String,String>();
        headers.put("Content-Type","application/json");

        JSONObject json = new JSONObject();
        //选择模型
        json.set("model","gpt-3.5-turbo");
        //添加我们需要输入的内容
        JSONObject msg = new JSONObject();
        msg.set("role", "user");
        msg.set("content", txt);
        JSONArray array = new JSONArray();
        array.add(msg);
        json.set("messages", array);
        json.set("temperature",0);
        json.set("max_tokens",2048);
        json.set("top_p",1);
        json.set("frequency_penalty",0.0);
        json.set("presence_penalty",0.0);
        try{
            Proxy proxy = Proxys.http("127.0.0.1", 4780);  // 端口号不知道怎么来的私聊，说出来审核不通过
            HttpResponse response = HttpRequest.post(chatEndpoint) //gpt-3.5-turbo
                    .headerMap(headers, false)
                    .bearerAuth(apiKey) // 填写自己的 chatgpt API Keys
                    .setProxy(proxy)
                    .body(String.valueOf(json))
                    .timeout(600000)
                    .execute();
            System.out.println(response.body());
            JSONObject jsonObj = JSONUtil.parseObj(response.body());
            JSONArray choices = jsonObj.getJSONArray("choices");
            JSONObject result = choices.get(0,JSONObject.class,Boolean.TRUE);
            JSONObject messages = result.getJSONObject("message");
            return messages.getStr("content");
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
