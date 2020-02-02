package org.wallet.common.dto.application;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.wallet.common.dto.BaseNormalDTO;
import org.wallet.common.dto.SearchOperator;
import org.wallet.common.dto.SearchProperty;

/**
 * @author zengfucheng
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class AppMessageDTO extends BaseNormalDTO {
    @SearchProperty(SearchOperator.allLike)
    private String title;
    private String content;
    private String url;
}
