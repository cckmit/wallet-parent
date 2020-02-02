package org.wallet.common.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author zengfucheng
 **/
@Getter
@Setter
public abstract class BaseNormalTreeDTO<T> extends BaseNormalDTO implements TreeData<T> {
    private Long pid;
    private List<T> childList;
}
