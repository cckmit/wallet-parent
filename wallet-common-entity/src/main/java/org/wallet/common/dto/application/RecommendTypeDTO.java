package org.wallet.common.dto.application;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author zengfucheng
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendTypeDTO implements Serializable {
    private String name;
    private String icon;
    private List<AppInfoDTO> apps;
}
