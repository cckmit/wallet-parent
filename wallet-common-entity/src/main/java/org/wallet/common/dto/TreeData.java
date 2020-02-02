package org.wallet.common.dto;

import java.util.List;

/**
 * 树结构数据
 */
public interface TreeData<T> {
    /**
     * 获取ID
     * @return ID
     */
    Long getId();

    /**
     * 获取父节点ID
     * @return 父节点ID
     */
    Long getPid();

    /**
     * 获取子节点集合
     * @return 子节点集合
     */
    List<T> getChildList();

    /**
     * 设置子节点
     * @param list 子节点
     */
    void setChildList(List<T> list);
}
