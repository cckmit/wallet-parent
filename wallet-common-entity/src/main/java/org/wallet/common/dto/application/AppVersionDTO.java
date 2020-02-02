package org.wallet.common.dto.application;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.wallet.common.dto.BaseNormalDTO;
import org.wallet.common.enums.Device;
import org.wallet.common.enums.application.AppUpgradeStrategyEnum;

/**
 * @author zengfucheng
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class AppVersionDTO extends BaseNormalDTO {
    private Device device;
    private Integer highestVersion;
    private Integer lowestVersion;
    private String currentVersion;
    private AppUpgradeStrategyEnum upgradeStrategy;
    private String url;
    private Double fileSize;
    private String intro;
}
