package org.wallet.common.dto.push;

import lombok.Builder;
import lombok.Data;
import org.wallet.common.enums.Device;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 推送目标
 * @author zengfucheng
 **/
@Data
@Builder
public class PushTarget {
    /**
     * 推送设备
     */
    private Device device;
    /**
     * 客户端ID
     */
    private List<String> clientIds;
    /**
     * 客户端别名
     */
    private List<String> alias;
    /**
     * 客户端标签
     */
    private List<String> tags;

    public static PushTarget singleClient(String clientId){
        return PushTarget.builder().clientIds(Collections.singletonList(clientId)).build();
    }

    public static PushTarget singleAlias(String alias){
        return PushTarget.builder().alias(Collections.singletonList(alias)).build();
    }

    public static PushTarget singleTag(String tag){
        return PushTarget.builder().tags(Collections.singletonList(tag)).build();
    }

    public static PushTarget clients(String... clientId){
        return PushTarget.builder().clientIds(Arrays.asList(clientId)).build();
    }

    public static PushTarget alias(String... alias){
        return PushTarget.builder().alias(Arrays.asList(alias)).build();
    }

    public static PushTarget tags(String... tags){
        return PushTarget.builder().tags(Arrays.asList(tags)).build();
    }

    public static PushTarget allDevice(Device device){
        return PushTarget.builder().device(device).build();
    }

    public static PushTarget all(){
        return PushTarget.builder().device(Device.ALL).build();
    }
}
