package org.wallet.common.dto.wallet.req;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 交易统计请求数据实体
 * @author zengfucheng
 **/
@Data
public class TransferStatisticsReqDTO implements Serializable {
    /** 主链ID */
    @NotNull
    private Long chainId;
    @JSONField(format = "yyyyMMdd")
    @DateTimeFormat(pattern = "yyyyMMdd")
    private Date startDate;
    @JSONField(format = "yyyyMMdd")
    @DateTimeFormat(pattern = "yyyyMMdd")
    private Date endDate;
}
