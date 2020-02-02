package org.wallet.dap.common.bind.search;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.wallet.common.constants.field.EntityField;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.SearchOperator;
import org.wallet.common.dto.SearchProperty;
import org.wallet.common.dto.SortDTO;
import org.wallet.dap.common.bind.filter.SearchFilter;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 接收参数 对象
 */
public class Search extends SearchDefault implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(Search.class);

    private static final long serialVersionUID = 1L;

    private PageDTO page;

    private SortDTO sort;

    private static final String TABLE_ALIAS = "x.";

    /**
     * 接受页面参数只会封装到这个filter里面
     * 示例：where 1=1 and x.name=张三 and x.age=23 都是且的关系
     */
    private List<SearchFilter> searchFilters = new ArrayList<>();

    /**
     * 添加or 关系 filter
     * 示例：where 1=1 and x.name=张三 or x.age=23 只有一个 可以这样使用
     * 示例：where 1=1 and x.name=张三 and（x.age=23 or x.age=22） 多个 可以这样使用
     */
    private List<SearchFilter> orSearchFilters = new ArrayList<>();

    /**
     * 装载的时参数名称和值 name,value,来自于searchFilters和orSearchFilters
     * 只能获取paramMap 添加通过Search.addSearchFilter().
     * 默认采用 :name
     */
    private Map<String, Object> paramMap = new HashMap<>();

    /**
     * 默认构造器 初始化 Pageable Sort 使用默认值
     */
    public Search() {
        this.sort = getDefaultSort();
    }

    public Search(SearchFilter... filters) {
        this();
        getSearchFilters().addAll(Arrays.asList(filters));
    }

    public Search(Object object, PageDTO page, SortDTO sort) {
        if(null != page && page.getPageNo() != null && page.getPageSize() != null){
            setPage(page);
        }
        if(null != sort && !CollectionUtils.isEmpty(sort.getOrders())){
            setSort(sort);
        }else{
            setSort(SortDTO.unsorted());
        }
        if(null != object){
            Class clazz = object.getClass();
            Field[] fields = FieldUtils.getAllFields(clazz);
            for(Field field : fields){
                field.setAccessible(true);
                Object filedValue = ReflectionUtils.getField(field, object);
                if(null != filedValue){
                    SearchProperty property = field.getAnnotation(SearchProperty.class);
                    if(null != property){
                        String name = EntityField.NONE.equals(property.name()) ? field.getName() : property.name();
                        addSearchFilter(name, property.value(), filedValue, property.valueType());
                    }else{
                        addSearchFilter(field.getName(), SearchOperator.eq, filedValue);
                    }
                }
            }
        }
    }

    /**
     * 获取paramMap
     */
    public Map<String, Object> getParamMap() {
        if (this.paramMap.size() > 0) {
            this.paramMap.clear();
        }
        for (SearchFilter filter : searchFilters) {
            this.paramMap.put(filter.getProperty(), filter.getValue());
        }
        for (SearchFilter filter : orSearchFilters) {
            this.paramMap.put(filter.getProperty(), filter.getValue());
        }
        return paramMap;
    }

    public Search addSearchFilter(String property, SearchOperator searchOperator, Object value) {
        this.searchFilters.add(new SearchFilter(property, searchOperator, value));
        return this;
    }

    public Search addSearchFilter(String property, SearchOperator searchOperator, Object value, Class valueType) {
        this.searchFilters.add(new SearchFilter(property, searchOperator, value, valueType));
        return this;
    }

    public Search addSearchFilter(SearchFilter searchFilter) {
        this.searchFilters.add(searchFilter);
        return this;
    }

    public Search addOrSearchFilter(String property, SearchOperator operator, Object value) {
        this.orSearchFilters.add(new SearchFilter(property, operator, value));
        return this;
    }

    public Search addOrSearchFilter(String property, SearchOperator operator, Object value, Class valueType) {
        this.orSearchFilters.add(new SearchFilter(property, operator, value, valueType));
        return this;
    }

    public Search addOrSearchFilter(SearchFilter searchFilter) {
        this.orSearchFilters.add(searchFilter);
        return this;
    }

    public PageDTO getPage() {
        return page;
    }

    public Search setPage(PageDTO page) {
        this.page = page;
        return this;
    }

    public SortDTO getSort() {
        return sort;
    }

    public Search setSort(SortDTO sort) {
        this.sort = sort;
        return this;
    }

    public List<SearchFilter> getSearchFilters() {
        return searchFilters;
    }

    public Search setSearchFilters(List<SearchFilter> searchFilters) {
        this.searchFilters = searchFilters;
        return this;
    }

    public List<SearchFilter> getOrSearchFilters() {
        return orSearchFilters;
    }

    public Search setOrSearchFilters(List<SearchFilter> orSearchFilters) {
        this.orSearchFilters = orSearchFilters;
        return this;
    }

    public Search setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
        return this;
    }
}
