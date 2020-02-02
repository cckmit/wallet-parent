package org.wallet.common.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zengfucheng
 **/
@Getter
@Setter
public abstract class BaseNormalDTO extends BaseModifiableDTO {
    @SearchProperty(SearchOperator.allLike)
    private String name;
    private String remark;
    private Boolean enable;
    private Integer sort;

    @Override
    public void removeAdminAttr() {
        super.removeAdminAttr();
        setEnable(null);
        setSort(null);
    }
}
