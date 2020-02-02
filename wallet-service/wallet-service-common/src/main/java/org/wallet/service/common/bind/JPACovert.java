package org.wallet.service.common.bind;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.SortDTO;
import org.wallet.dap.common.bind.search.SearchFinal;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zengfucheng
 **/
public class JPACovert {

    /**
     * 将自定义分页转换为JPA支持的分页对象
     * @param page 分页数据
     * @return 分页对象
     */
    public static Pageable covertPage(PageDTO page){
        if(null == page){ page = PageDTO.of(SearchFinal.DEFAULT_PAGE, SearchFinal.DEFAULT_LIMIT); }
        if(null == page.getPageNo()){ page.setPageNo(SearchFinal.DEFAULT_PAGE); }
        if(null == page.getPageSize()){ page.setPageSize(SearchFinal.DEFAULT_LIMIT); }
        return PageRequest.of(page.getPageNo() - 1, page.getPageSize());
    }

    /**
     * 将自定义分页和排序对象转换为JPA支持的分页对象
     * @param page 分页数据
     * @param sort 排序
     * @return 分页对象
     */
    public static Pageable covertPage(PageDTO page, SortDTO sort){
        return PageRequest.of(page.getPageNo() - 1, page.getPageSize(), covertSort(sort));
    }

    /**
     * 将自定义排序对象转换为JPA支持的排序对象
     * @param sort 排序
     * @return 排序对象
     */
    public static Sort covertSort(SortDTO sort){
        if(null == sort){
            return Sort.unsorted();
        }
        List<Sort.Order> orders = new ArrayList<>();
        if(!CollectionUtils.isEmpty(sort.getOrders())){
            sort.getOrders().forEach(order -> {
                if(order.getDirection().equals(SortDTO.Direction.ASC)){
                    orders.add(Sort.Order.asc(order.getProperty()));
                }else{
                    orders.add(Sort.Order.desc(order.getProperty()));
                }
            });
        }
        return Sort.by(orders);
    }
}
