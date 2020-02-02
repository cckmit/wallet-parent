package org.wallet.common.enums.application;

import lombok.Getter;

/**
 * App 标签
 *
 * @author zengfucheng
 */
@Getter
public enum AppTagEnum {
    /** 精选 */
    FEATURED(3),
    /** 热门 */
    HOT(4),
    /** 推荐 */
    RECOMMEND(5),
    /** 分类推荐 */
    TYPE_RECOMMEND(10),
    /** 热门搜索 */
    HOTSPOT(10);

    /** 标签限制App 数量 */
    private int limit;

    AppTagEnum(int limit){
        this.limit = limit;
    }
}
