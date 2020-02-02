package org.wallet.dap.common.bind.search;

import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.SortDTO;

/**
 * 默认的search
 */
class SearchDefault implements SearchFinal{

	/**
	 * 获取默认的Pageable
	 */
	PageDTO getDefaultPageable() {
		return PageDTO.of(DEFAULT_PAGE, DEFAULT_LIMIT);
	}

	/**
	 * 获取默认的Sort
	 */
	SortDTO getDefaultSort() {
		return SortDTO.by(SortDTO.Direction.DESC, DEFAULT_SORT);
	}
}
