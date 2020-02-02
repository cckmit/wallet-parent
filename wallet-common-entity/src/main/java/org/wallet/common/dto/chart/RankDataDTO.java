package org.wallet.common.dto.chart;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author zengfucheng
 **/
@Data
public class RankDataDTO implements Serializable {
    private Integer no;
    private String name;
    @JSONField(format = "#0.0")
    private BigDecimal amount;
}
