package org.wallet.dap.common.bind.filter;

import org.wallet.common.dto.SearchOperator;

public class SearchFilters{
	public static SearchFilter eq(String name, Object value){
		return new SearchFilter(name, SearchOperator.eq, value);
	}

	public static SearchFilter ne(String name, Object value){
		return new SearchFilter(name, SearchOperator.ne, value);
	}

	public static SearchFilter gt(String name, Object value){
		return new SearchFilter(name, SearchOperator.gt, value);
	}

	public static SearchFilter gte(String name, Object value){
		return new SearchFilter(name, SearchOperator.gte, value);
	}

	public static SearchFilter lt(String name, Object value){
		return new SearchFilter(name, SearchOperator.lt, value);
	}

	public static SearchFilter lte(String name, Object value){
		return new SearchFilter(name, SearchOperator.lte, value);
	}

    public static SearchFilter ll(String name, Object value){
        return new SearchFilter(name, SearchOperator.leftLike, value);
    }

    public static SearchFilter rl(String name, Object value){
        return new SearchFilter(name, SearchOperator.rightLike, value);
    }

	public static SearchFilter like(String name, Object value){
		return new SearchFilter(name, SearchOperator.allLike, value);
	}

    public static SearchFilter nl(String name, Object value){
        return new SearchFilter(name, SearchOperator.notLike, value);
    }

    public static SearchFilter isNull(String name, Object value){
        return new SearchFilter(name, SearchOperator.isNull, value);
    }

    public static SearchFilter isNotNull(String name, Object value){
        return new SearchFilter(name, SearchOperator.isNotNull, value);
    }

    public static SearchFilter in(String name, Object value){
        return new SearchFilter(name, SearchOperator.in, value);
    }

    public static SearchFilter ni(String name, Object value){
        return new SearchFilter(name, SearchOperator.notIn, value);
    }
}
