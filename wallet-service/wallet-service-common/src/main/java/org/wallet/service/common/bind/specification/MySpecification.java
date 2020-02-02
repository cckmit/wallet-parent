package org.wallet.service.common.bind.specification;

import org.springframework.data.jpa.domain.Specification;
import org.wallet.common.entity.BaseEntity;
import org.wallet.dap.common.bind.filter.SearchFilter;
import org.wallet.dap.common.bind.search.Search;
import org.wallet.dap.common.exception.SearchException;

import javax.persistence.criteria.Predicate;
import java.util.List;

/**
 * Search 转 Specification
 */
public class MySpecification<M extends BaseEntity> extends BaseSpecification<M> {

    private Search search;

    public MySpecification(Search search) {
        this.search = search;
    }

    /**
     * 获取 search 转 Specification 对象
     */
    public Specification<M> toPredicate() {
        this.getSearchFilters();
        return (Specification<M>) (root, query, cb) -> {
            if (search.getOrSearchFilters().size() == 0 && search.getSearchFilters().size() == 0) {
                return cb.conjunction();
            }
            Predicate predicate = getPredicate(root, cb, search.getSearchFilters(), AND);
            Predicate predicateOr = getPredicate(root, cb, search.getOrSearchFilters(), OR);
            if (predicate == null) {
                return cb.or(predicateOr);
            }
            if (predicateOr == null) {
                return predicate;
            }
            if (predicateOr.getExpressions().size() == 1) {
                return cb.or(predicateOr, predicate);
            }
            return cb.and(predicateOr, predicate);
        };
    }

    /**
     * 获取Search Filter
     */
    private void getSearchFilters() {
        if (this.search == null) {
            throw new SearchException("缺少Search对象");
        }
        validSearchFilter(search.getSearchFilters());
    }

    /**
     * 使用Specification查询时不能使用 ? 问号传参
     */
    private void validSearchFilter(List<SearchFilter> searchFilters) {
        for (SearchFilter filter : searchFilters) {
            if ("?".equals(filter.toString())) {
                throw new SearchException("使用Specification查询时不能使用 ? 内部已实现");
            }
        }
    }

    public Search getSearch() {
        return search;
    }

    public void setSearch(Search search) {
        this.search = search;
    }


}
