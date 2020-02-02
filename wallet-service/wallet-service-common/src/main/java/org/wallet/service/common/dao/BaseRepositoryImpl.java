package org.wallet.service.common.dao;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * 接口实现类 实现我们自定义的方法
 */
public class BaseRepositoryImpl<T, ID extends Serializable>
			extends SimpleJpaRepository<T, Serializable> implements BaseRepository<T>{

	private final Class<T> domainClass;
	private final EntityManager entityManager;

	BaseRepositoryImpl(JpaEntityInformation<T, ID> entityInformation,
					   EntityManager entityManager) {
		super(entityInformation, entityManager);
		domainClass = entityInformation.getJavaType();
		this.entityManager = entityManager;
	}

    public BaseRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.domainClass = domainClass;
        this.entityManager = entityManager;
    }

	@Override
    public boolean support(String modelType) {
        return domainClass.getName().equals(modelType);
    }

}
