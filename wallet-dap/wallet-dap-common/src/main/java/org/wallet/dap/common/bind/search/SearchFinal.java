package org.wallet.dap.common.bind.search;

/**
 * search 常量
 */
public interface SearchFinal {

    /**
     * 接收前端参数排序字段 KEY 示例： person/findAll?sort=name&order=desc
     */
    String SORT = "sort";

    /**
     * 接收前端参数排序方式 KEY 示例： person/findAll?sort=name&order=desc
     */
    String ORDER = "order";

    /**
     * 降序
     */
    String DESC = "DESC";

    /**
     * 升序
     */
    String ASC = "ASC";

    /**
     * 默认排序字段
     */
    String DEFAULT_SORT = "id";

    /**
     * 每页显示大小 实例：person/findAll?sort=name&order=desc&limit=5&page=1
     */
    String LIMIT = "limit";

    /**
     * 第几条开始 实例：person/findAll?sort=name&order=desc&limit=5&page=1
     */
    String PAGE = "page";

    /**
     * 默认每页显示几条
     */
    int DEFAULT_LIMIT = 10;

    /**
     * 默认第一页
     */
    int DEFAULT_PAGE = 1;

}
