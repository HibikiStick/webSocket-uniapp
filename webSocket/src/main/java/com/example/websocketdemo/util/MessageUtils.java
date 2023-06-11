package com.example.websocketdemo.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

public class MessageUtils {
    public static String getMessage(boolean isSystemMessage, String fromName, Object message){
        try {
            ResultMessage resultMessage = new ResultMessage();
            resultMessage.setSystem(isSystemMessage);
            resultMessage.setMessage(message);
            if (StringUtils.isNotBlank(fromName)){
                resultMessage.setFromName(fromName);
            }
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(resultMessage);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
