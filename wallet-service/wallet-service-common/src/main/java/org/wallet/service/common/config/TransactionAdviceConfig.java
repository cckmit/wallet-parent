package org.wallet.service.common.config;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.*;

import java.util.Collections;

/**
 * @author zengfucheng
 **/
@Aspect
@Configuration
@ConditionalOnProperty("spring.datasource.url")
public class TransactionAdviceConfig {
    private static final String AOP_POINTCUT_EXPRESSION = "execution (* org.wallet.service.*.service.impl..*(..))";

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public TransactionInterceptor txAdvice() {
        RuleBasedTransactionAttribute required = new RuleBasedTransactionAttribute();
        required.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
        required.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        // 默认事务超时时间为5秒
        required.setTimeout(5);

        DefaultTransactionAttribute readonly = new DefaultTransactionAttribute();
        readonly.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        readonly.setReadOnly(true);

        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        source.addTransactionalMethod("insert*", required);
        source.addTransactionalMethod("add*", required);
        source.addTransactionalMethod("create*", required);
        source.addTransactionalMethod("save*", required);
        source.addTransactionalMethod("delete*", required);
        source.addTransactionalMethod("update*", required);
        source.addTransactionalMethod("exec*", required);
        source.addTransactionalMethod("set*", required);
        source.addTransactionalMethod("*", required);

        source.addTransactionalMethod("get*", readonly);
        source.addTransactionalMethod("query*", readonly);
        source.addTransactionalMethod("find*", readonly);
        source.addTransactionalMethod("list*", readonly);
        source.addTransactionalMethod("count*", readonly);
        source.addTransactionalMethod("is*", readonly);
        source.addTransactionalMethod("exists", readonly);

        if(transactionManager instanceof JpaTransactionManager){
            JpaTransactionManager jpaTransactionManager = (JpaTransactionManager) transactionManager;
            // 设置是否在参与的事务失败后将现有事务全局标记为仅回滚。默认值为“true”
            // 关闭该配置以保证在Service层中调用JPA查询抛出异常后能够正常提交
            jpaTransactionManager.setGlobalRollbackOnParticipationFailure(false);
        }

        return new TransactionInterceptor(transactionManager, source);
    }

    @Bean
    public Advisor txAdviceAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
        return new DefaultPointcutAdvisor(pointcut, txAdvice());
    }
}

