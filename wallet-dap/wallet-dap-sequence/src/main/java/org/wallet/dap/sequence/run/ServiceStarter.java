package org.wallet.dap.sequence.run;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.wallet.dap.common.boot.SpringBootstrap;
import org.wallet.dap.common.dubbo.ISequence;
import org.wallet.dap.sequence.SequenceGenerator;

import javax.annotation.PostConstruct;

@Slf4j
@SpringBootApplication(scanBasePackages = "org.wallet")
public class ServiceStarter {
    @Autowired
    ISequence sequence;

	public static void main(String[] args) {
		SpringBootstrap.run(ServiceStarter.class, args);
	}

	@Bean
	public SequenceGenerator sequenceGenerator(@Value("${sequence.nodeId:1}") Long nodeId){
		return new SequenceGenerator(nodeId);
	}

	@PostConstruct
    public void init(){
        sequence.getSequence();
    }
}
