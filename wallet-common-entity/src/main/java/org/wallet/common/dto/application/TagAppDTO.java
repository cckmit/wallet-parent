package org.wallet.common.dto.application;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.wallet.common.enums.application.AppTagEnum;

import java.io.Serializable;

/**
 * @author zengfucheng
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagAppDTO implements Serializable {
    private Long appTagId;
    private Long appTypeId;
    private String appTypeName;
    private Long appInfoId;
    private String appInfoName;
    private String appInfoIcon;
    private String appInfoUrl;
    private AppTagEnum appTag;
    private Integer appTagSort;
    private String appTagImg;
}
