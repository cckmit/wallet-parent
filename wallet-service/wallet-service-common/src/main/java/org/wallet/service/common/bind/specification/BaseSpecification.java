package org.wallet.service.common.bind.specification;

import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;
import org.springframework.util.StringUtils;
import org.wallet.common.dto.SearchOperator;
import org.wallet.dap.common.bind.filter.SearchFilter;
import org.wallet.dap.common.exception.SearchException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * 提供转换方法
 */
abstract class BaseSpecification<M> {

    final static String AND = "AND";
    final static String OR = "OR";

    Predicate getPredicate(Root<M> root, CriteriaBuilder cb, List<SearchFilter> filters, String type) {
        if (filters.size() <= 0) {
            return null;
        }
        ArrayList<Predicate> ls = new ArrayList<>();
        for (SearchFilter filter : filters) {
            ls.add(this.filterToPredicate(filter, root, cb));
        }
        Predicate[] ps = new Predicate[ls.size()];
        for (int i = 0, j = ls.size(); i < j; i++) {
            ps[i] = ls.get(i);
        }
        if (AND.equals(type)) {
            return cb.and(ps);
        } else {
            return cb.or(ps);
        }
    }

    /**
     * SearchFilter to predicate
     */
    @SuppressWarnings("unchecked")
    private Predicate filterToPredicate(SearchFilter filter, Root<M> root, CriteriaBuilder cb) {
        CriteriaBuilderImpl builderImpl = (CriteriaBuilderImpl) cb;
        String property = filter.getProperty();
        SearchOperator operator = filter.getOperator();
        Object value = filter.getValue();
        switch (operator) {
            case eq: return cb.equal(root.get(property), value);
            case ne: return cb.notEqual(root.get(property), value);
            case gt: return cb.gt(root.get(property), Integer.valueOf(value.toString()));
            case gte: {
                if(value instanceof Comparable){
                    return cb.greaterThanOrEqualTo(root.get(property), (Comparable) value);
                } else {
                    return cb.greaterThanOrEqualTo(root.get(property), value.toString());
                }
            }
            case lt: return cb.lt(root.get(property), Integer.valueOf(value.toString()));
            case lte: {
                if(value instanceof Comparable){
                    return cb.lessThanOrEqualTo(root.get(property), (Comparable) value);
                } else {
                    return cb.lessThanOrEqualTo(root.get(property), value.toString());
                }
            }
            case leftLike: return cb.like(root.get(property), "%" + value);
            case rightLike: return cb.like(root.get(property), value + "%");
            case allLike: return cb.like(root.get(property), "%" + value + "%");
            case notLike: return cb.notLike(root.get(property), value.toString());
            case isNull: return cb.isNull(root.get(property));
            case isNotNull: return cb.isNotNull(root.get(property));
            case in: return builderImpl.in(root.get(property), value);
            case notIn: return builderImpl.not(builderImpl.in(root.get(property), value));
            default: throw new SearchException("没有对应的操作符，参照SearchOperator.java");
        }
    }

    BaseSpecification() {
    }

}
