package org.wallet.dap.common.bind.search;

import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.SortDTO;
import org.wallet.dap.common.bind.filter.SearchFilter;

/**
 * @author zengfucheng
 */
public class Searchs {
    public static Search of(SearchFilter... filters){
        return new Search(filters);
    }

    public static Search of(SortDTO sort, SearchFilter... filters){
        return new Search(filters).setSort(sort);
    }

    public static Search of(PageDTO page, SortDTO sort, SearchFilter... filters){
        return new Search(filters).setPage(page).setSort(sort);
    }
}
