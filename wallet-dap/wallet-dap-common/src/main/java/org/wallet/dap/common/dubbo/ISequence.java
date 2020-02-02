package org.wallet.dap.common.dubbo;

/**
 * @author zengfucheng
 * @date 2018年4月10日
 */
public interface ISequence {

	/**
	 * 获得无序Sequence
	 * @return
	 */
	long getSequence();
	
	/**
	 * 获得有序的Sequence
	 * @param seqName
	 * @return
	 */
	long getSequence(String seqName);
}
