package org.wallet.common.dto.application;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.wallet.common.dto.BaseNormalDTO;

/**
 * @author zengfucheng
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class AppTypeDTO extends BaseNormalDTO {
    private Long chainId;
    private String intro;
    private String icon;
    private Long appCount;
    private Long featuredCount;
    private Long recommendCount;

    public AppTypeDTO() {
    }

    public AppTypeDTO(Long id, String name, String icon, String intro) {
        setId(id);
        setName(name);
        this.icon = icon;
        this.intro = intro;
    }
}
