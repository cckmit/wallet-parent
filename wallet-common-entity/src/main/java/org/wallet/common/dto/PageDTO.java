package org.wallet.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author zengfucheng
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDTO<T> implements Serializable {
    /** 当前页 */
    private Integer pageNo;
    /** 每页数量 */
    private Integer pageSize;
    /** 总页数 */
    private Integer pageTotal;
    /** 总数据量 */
    private Long total;
    /** 当前页数据 */
    private List<T> records;

    private PageDTO(Integer pageNo, Integer pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public static <T> PageDTO<T> of(PageDTO page, List<T> records) {
        return new PageDTO<>(page.getPageNo(), page.getPageSize(), page.getPageTotal(), page.getTotal(), records);
    }

    public static <T> PageDTO<T> of(int page, int size) {
        return new PageDTO<>(page, size);
    }
}
