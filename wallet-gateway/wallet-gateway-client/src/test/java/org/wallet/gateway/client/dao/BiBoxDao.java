package org.wallet.gateway.client.dao;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.wallet.common.entity.platform.bibox.BiBoxResult;

/**
 * @author zengfucheng
 **/
@Component
public class BiBoxDao {

    @Cacheable(value = "bibox:result", key = "#ver")
    public BiBoxResult getResultByVer(String ver){
        BiBoxResult<String> result = new BiBoxResult<>();
        result.setCmd("thisCmd");
        result.setVer(ver);
        result.setResult("水电费");
        System.out.println("put cache " + ver);
        return result;
    }
}
