package org.wallet.common.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author zengfucheng
 **/
@Getter
@Setter
public abstract class BaseModifiableDTO extends BaseDTO {
    private Long updater;
    private Date updateDate;

    @Override
    public void removeAdminAttr() {
        super.removeAdminAttr();
        setUpdater(null);
        setUpdateDate(null);
    }
}
