package org.wallet.common.dto.application.req;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 查询DApp访问日志请求数据实体
 * @author zengfucheng
 **/
@Data
public class FindAppVisitLogReqDTO implements Serializable {
    private Long chainId;
    private String appName;
    @JSONField(format = "yyyyMMdd")
    @DateTimeFormat(pattern = "yyyyMMdd")
    private Date startDate;
    @JSONField(format = "yyyyMMdd")
    @DateTimeFormat(pattern = "yyyyMMdd")
    private Date endDate;
}
