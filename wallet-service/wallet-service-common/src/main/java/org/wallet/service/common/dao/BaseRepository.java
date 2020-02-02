package org.wallet.service.common.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;

/**
 * @author zengfucheng
 **/
@NoRepositoryBean
public interface BaseRepository<T> extends JpaRepository<T, Serializable>, JpaSpecificationExecutor<T>, PagingAndSortingRepository<T, Serializable> {

    /**
     * 验证是不是我们要的领域模型
     */
    boolean support(String modelType);
}