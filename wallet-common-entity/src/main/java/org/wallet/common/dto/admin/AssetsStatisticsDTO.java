package org.wallet.common.dto.admin;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 资产统计数据实体
 *
 * @author zengfucheng
 */
@Data
public class AssetsStatisticsDTO implements Serializable {
    /** 总资产 **/
    @JSONField(format = "#0.0")
    private BigDecimal totalAssets;
    /** 日新增资产 **/
    @JSONField(format = "#0.0")
    private BigDecimal assetsDaily;
    /** 总资产日环比 **/
    @JSONField(format = "#0.0")
    private BigDecimal assetsDailyRingRatio;
    /** 总资产周环比 **/
    @JSONField(format = "#0.0")
    private BigDecimal assetsWeeklyRingRatio;
    /** 人均资产 **/
    @JSONField(format = "#0.0")
    private BigDecimal avgAssets;
    /** 总地址数 **/
    private Long totalAddress;
    /** 日新增地址数 **/
    private Long addressDaily;
    /** 总地址数日环比 **/
    @JSONField(format = "#0.0")
    private BigDecimal addressDailyRingRatio;
    /** 总地址数周环比 **/
    @JSONField(format = "#0.0")
    private BigDecimal addressWeeklyRingRatio;
}