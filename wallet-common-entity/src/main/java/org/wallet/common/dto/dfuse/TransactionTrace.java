package org.wallet.common.dto.dfuse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @author zengfucheng
 **/
@Data
public class TransactionTrace {
    private final static String TRANSFER_NAME = "transfer";
    private String id;
    @JsonProperty(value = "block_num")
    @JSONField(name = "block_num")
    private Long blockNum;
    @JsonProperty(value = "block_time")
    @JSONField(name = "block_time")
    private String blockTime;
    @JsonProperty(value = "producer_block_id")
    @JSONField(name = "producer_block_id")
    private String producerBlockId;
    private TransactionReceiptHeader receipt;
    private Long elapsed;
    @JsonProperty(value = "net_usage")
    @JSONField(name = "net_usage")
    private Long netUsage;
    @JsonProperty(value = "action_traces")
    @JSONField(name = "action_traces")
    private List<ActionTrace> actionTraces;
    @JsonProperty(value = "failed_dtrx_trace")
    @JSONField(name = "failed_dtrx_trace")
    private TransactionTrace failedDtrxTrace;
    private String except;

    public TransferData getTransferData(String account, String to){
        return getTransferData(actionTraces, account, to);
    }

    private TransferData getTransferData(List<ActionTrace> actionTraces, String account, String to){
        if(!CollectionUtils.isEmpty(actionTraces)){
            for (ActionTrace actionTrace : actionTraces){
                Action action = actionTrace.getAct();
                if(null != action){
                    boolean isTransfer = TRANSFER_NAME.equals(action.getName());
                    boolean accountEqual = account.equals(action.getAccount());
                    if(isTransfer && accountEqual){
                        Object dataObj = action.getData();
                        if(dataObj instanceof Map){
                            TransferData data = JSON.parseObject(JSON.toJSONString(dataObj), TransferData.class);
                            if(to.equals(data.getTo())){
                                return data;
                            }
                        }
                    }
                }
                if(!CollectionUtils.isEmpty(actionTrace.getInlineTraces())){
                    TransferData data = getTransferData(actionTrace.getInlineTraces(), account, to);
                    if(null != data){
                        return data;
                    }
                }
            }
        }
        return null;
    }
}
