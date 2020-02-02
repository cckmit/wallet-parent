package org.wallet.common.dto.chart;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 数据占比数据实体
 * @author zengfucheng
 **/
@Data
public class PercentDataDTO implements Serializable {
    private String name;
    @JSONField(format = "#0.0")
    private BigDecimal percent;
    @JSONField(format = "#0.0")
    private BigDecimal num;

    public PercentDataDTO() {
    }

    public PercentDataDTO(String name, BigDecimal percent, BigDecimal num) {
        this.name = name;
        this.percent = percent;
        this.num = num;
    }
}
