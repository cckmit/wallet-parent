package org.wallet.common.dto.push;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 推送目标
 * @author zengfucheng
 **/
@Data
@Builder
public class PushMessage {
    /** 标题 */
    private String title;
    /** 内容 */
    private Object content;
    /** 数据 */
    private Object data;

    /**
     * 创建消息
     * @param content 内容
     * @return 消息
     */
    public static PushMessage create(String content){
        return PushMessage.builder().content(content).build();
    }

    /**
     * 创建消息
     * @param title 标题
     * @param content 内容
     * @return 消息
     */
    public static PushMessage create(String title, String content){
        return PushMessage.builder().title(title).content(content).build();
    }

    /**
     * 创建消息
     * @param title 标题
     * @param content 内容
     * @param data 数据
     * @return 消息
     */
    public static PushMessage create(String title, String content, Object data){
        return PushMessage.builder().title(title).content(content).data(data).build();
    }

    /**
     * 透传消息
     * @param data 透传数据
     * @return 消息
     */
    public static PushMessage data(Object data){
        return PushMessage.builder().data(data).build();
    }

    /**
     * 将数据转换为Map
     * @return Map
     */
    public Map<String, String> mapData(){
        if(null == data){
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(data));
        Map<String, String> map = new HashMap<>(jsonObject.size());
        jsonObject.forEach((k, v) -> map.put(k, null != v ? v.toString() : null));
        return map;
    }
}
