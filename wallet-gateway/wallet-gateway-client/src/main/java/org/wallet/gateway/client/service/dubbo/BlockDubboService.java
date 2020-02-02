package org.wallet.gateway.client.service.dubbo;

import cn.hutool.core.util.ReflectUtil;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.wallet.common.constants.BlockCoinConstants;
import org.wallet.common.dto.block.BlackReqDTO;
import org.wallet.common.dto.block.req.CreateAccountReqDTO;
import org.wallet.dap.common.dubbo.*;
import org.wallet.gateway.client.service.BlockService;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zengfucheng
 **/
@Service(group = DubboServiceGroup.CLIENT_BLOCK, timeout = 30000)
@org.springframework.stereotype.Service
public class BlockDubboService extends BaseDubboService implements IService {

    private static final String SERVICE_SUFFIX = BlockService.class.getSimpleName();
    private static Map<String, BlockService> blockServiceMap = new HashMap<>();

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void initBlockServiceMap(){
        Field[] fields = ReflectUtil.getFields(BlockCoinConstants.class);
        if(null != fields){
            for (Field field : fields) {
                String coinName = field.getName();
                String beanName = coinName + SERVICE_SUFFIX;
                try{
                    BlockService blockService = applicationContext.getBean(beanName, BlockService.class);
                    if(null != blockService){
                        blockServiceMap.put(coinName, blockService);
                    }
                } catch (BeansException e){
                    log.warn("系统缺少BlockService[{}]", beanName);
                }
            }
        }
    }

    private BlockService getBlockService(BlackReqDTO blackReqDTO){
        return blockServiceMap.get(blackReqDTO.getCoinName());
    }

    public ServiceResponse createAccount(ServiceRequest request, ServiceResponse response){
        CreateAccountReqDTO reqDTO = request.getParam();

        String coinName = reqDTO.getCoinName();

        BlockService blockService = getBlockService(reqDTO);

        if(null == blockService){
            return Responses.fail(ResponseCode.NOT_FOUND, String.format("暂不支持该币种[%s]", coinName));
        }

        return Responses.success(blockService.createAccount(reqDTO));
    }
}
