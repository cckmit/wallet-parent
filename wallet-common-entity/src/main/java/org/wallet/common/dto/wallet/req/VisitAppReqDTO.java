package org.wallet.common.dto.wallet.req;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 访问DApp请求数据实体
 * @author zengfucheng
 **/
@Data
public class VisitAppReqDTO implements Serializable {
    /** AppID */
    @NotNull
    private Long appId;
    private String contract;
    private String coinName;
    @NotEmpty
    private String account;
}
