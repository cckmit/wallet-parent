package org.wallet.common.dto.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.wallet.common.dto.BaseNormalDTO;

/**
 * 字典
 * 
 * @author zengfucheng
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDictDTO extends BaseNormalDTO {
	private String dictType;
	private String dictName;
	private String dictValue;
	private String dictLabel;
}