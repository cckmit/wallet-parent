package org.wallet.common.entity;

import java.io.Serializable;

/**
 * 实体类
 */
public interface IBaseEntity extends Serializable{

	/**
	 * 获取ID
	 * @return ID
	 */
	Serializable getId();
}
