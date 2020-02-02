package org.wallet.common.enums.application;

import lombok.Getter;

/**
 * App 广告点位
 *
 * @author zengfucheng
 */
@Getter
public enum AppAdvertPointEnum {
    /** 发现页 - 顶部 */
    DISCOVERY_TOP(5),
    /** 发现页 - 中部 */
    DISCOVERY_MIDDLE(5),
    /** App 大全 - 顶部 */
    APP_HOME_TOP(5),
    /** App 推荐海报 */
    RECOMMEND_POSTER(20),
    /** App 启动画面 */
    SPLASH_SCREEN(20);

    /** 限制数量 */
    private int limit;

    AppAdvertPointEnum(int limit){
        this.limit = limit;
    }
}
