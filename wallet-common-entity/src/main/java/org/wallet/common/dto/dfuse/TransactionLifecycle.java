package org.wallet.common.dto.dfuse;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zengfucheng
 **/
@Data
public class TransactionLifecycle {
    private String id;
    /**
     * pending
     * delayed
     * canceled
     * expired
     * executed
     * soft_fail
     * hard_fail
     */
    @JsonProperty(value = "transaction_status")
    @JSONField(name = "transaction_status")
    private String transactionStatus;
    private Transaction transaction;
    @JsonProperty(value = "execution_trace")
    @JSONField(name = "execution_trace")
    private TransactionTrace executionTrace;
    @JsonProperty(value = "execution_block_header")
    @JSONField(name = "execution_block_header")
    private BlockHeader executionBlockHeader;
    private List<DTrxOp> dtrxops;
    @JsonProperty(value = "creation_tree")
    @JSONField(name = "creation_tree")
    private List<List<Integer>> creationTree;
    private List<DBOp> dbops;
    private List<RAMOp> ramops;
    private List<TableOp> tableops;
    @JsonProperty(value = "pub_keys")
    @JSONField(name = "pub_keys")
    private List<String> pubKeys;
    @JsonProperty(value = "created_by")
    @JSONField(name = "created_by")
    private ExtDTrxOp createdBy;
    @JsonProperty(value = "canceled_by")
    @JSONField(name = "canceled_by")
    private ExtDTrxOp canceledBy;
    @JsonProperty(value = "execution_irreversible")
    @JSONField(name = "execution_irreversible")
    private Boolean executionIrreversible;
    @JsonProperty(value = "creation_irreversible")
    @JSONField(name = "creation_irreversible")
    private Boolean creationIrreversible;
    @JsonProperty(value = "cancelation_irreversible")
    @JSONField(name = "cancelation_irreversible")
    private Boolean cancelationIrreversible;
}
