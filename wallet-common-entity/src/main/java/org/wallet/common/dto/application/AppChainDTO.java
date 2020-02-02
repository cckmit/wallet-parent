package org.wallet.common.dto.application;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.wallet.common.dto.BaseNormalDTO;

/**
 * @author zengfucheng
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AppChainDTO extends BaseNormalDTO {
    private String coinName;
    private String intro;
    private String icon;
    private Long appCount;
    private Long hotAppCount;

    public AppChainDTO(Long id, String coinName, String name, String intro, String icon, Integer sort, Boolean enable, Long appCount) {
        setId(id);
        setCoinName(coinName);
        setName(name);
        setIntro(intro);
        setIcon(icon);
        setSort(sort);
        setEnable(enable);
        setAppCount(appCount);
    }
}
