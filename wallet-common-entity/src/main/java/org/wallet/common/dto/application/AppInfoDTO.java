package org.wallet.common.dto.application;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.wallet.common.dto.BaseNormalDTO;

/**
 * @author zengfucheng
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class AppInfoDTO extends BaseNormalDTO {
    private Long chainId;
    private Long typeId;
    private String intro;
    private String icon;
    private String url;
    private Integer topSort;
    private String tagImg;

    public AppInfoDTO() {
    }

    public AppInfoDTO(Long id, String name, String intro, String icon, String url, String tagImg) {
        setId(id);
        setName(name);
        setIntro(intro);
        setIcon(icon);
        setUrl(url);
        setTagImg(tagImg);
    }
}
