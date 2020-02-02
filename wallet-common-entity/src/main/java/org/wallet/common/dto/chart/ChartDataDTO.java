package org.wallet.common.dto.chart;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 交易统计响应数据实体
 * @author zengfucheng
 **/
@Data
public class ChartDataDTO implements Serializable {
    @JSONField(format = "yyyy-MM-dd")
    private Date date;
    private Long num;
    @JSONField(format = "#0.0")
    private BigDecimal amount;

    public ChartDataDTO() {
    }

    public ChartDataDTO(Date date, Long num, BigDecimal amount) {
        this.date = date;
        this.num = num;
        this.amount = amount;
    }
}
