package org.wallet.common.dto.wallet;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.CollectionUtils;
import org.wallet.common.dto.BaseNormalDTO;
import org.wallet.common.dto.TableExtAttrValueDTO;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zengfucheng
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class WalletCoinDTO extends BaseNormalDTO {
    @NotNull
    private Long chainId;
    @NotEmpty
    private String fullName;
    @NotEmpty
    private String contractAddress;
    private Integer decimals;
    @JSONField(format = "#0.0")
    private BigDecimal usdRate;
    @NotEmpty
    private String logo;
    private String projectName;
    private String projectWebsite;
    private String projectIntro;
    private String thirdRating;
    private Date releaseTime;
    private String releaseTotalAmount;
    private Map<String, String> baseAttr;
    private List<TableExtAttrValueDTO> baseAttrs;

    public void sync(){
        if(!CollectionUtils.isEmpty(baseAttr) && CollectionUtils.isEmpty(baseAttrs)){
            baseAttrs = TableExtAttrValueDTO.toList(baseAttr);
        }

        if(CollectionUtils.isEmpty(baseAttr) && !CollectionUtils.isEmpty(baseAttrs)){
            baseAttr = TableExtAttrValueDTO.toMap(baseAttrs);
        }
    }
}
