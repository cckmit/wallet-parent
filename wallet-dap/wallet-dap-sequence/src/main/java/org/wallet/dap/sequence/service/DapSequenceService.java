package org.wallet.dap.sequence.service;

import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wallet.dap.common.dubbo.DubboServiceGroup;
import org.wallet.dap.common.dubbo.ISequence;
import org.wallet.dap.sequence.SequenceGenerator;

/**
 * @author zengfucheng
 * @date 2018年4月10日
 */
@Service(group = DubboServiceGroup.DAP_SEQUENCE)
@Component
public class DapSequenceService implements ISequence {

	@Autowired
	private SequenceGenerator sequenceGenerator;

	@Override
	public long getSequence() {
		return sequenceGenerator.nextId();
	}
	
	@Override
	public long getSequence(String seqName) {
		return -1;
	}
}
