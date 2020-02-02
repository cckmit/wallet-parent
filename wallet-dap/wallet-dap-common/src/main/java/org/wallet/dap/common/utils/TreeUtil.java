package org.wallet.dap.common.utils;

import org.springframework.util.CollectionUtils;
import org.wallet.common.dto.TreeData;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 树结构工具类
 *
 * @author zengfucheng
 */
public class TreeUtil {

    /**
     * 根据pid，构建树节点
     */
    public static <T extends TreeData> List<T> build(List<T> treeNodes, Long pid) {
        List<T> treeList = new ArrayList<>();
        for(T treeNode : treeNodes) {
            if (pid.equals(treeNode.getPid())) {
                treeList.add(findChildren(treeNodes, treeNode));
            }
        }

        return treeList;
    }

    /**
     * 查找子节点
     */
    @SuppressWarnings("unchecked")
    private static <T extends TreeData> T findChildren(List<T> treeNodes, T rootNode) {
        for(T treeNode : treeNodes) {
            if(rootNode.getId().equals(treeNode.getPid())) {
                rootNode.getChildList().add(findChildren(treeNodes, treeNode));
            }
        }
        return rootNode;
    }

    /**
     * 构建树节点
     */
    @SuppressWarnings("unchecked")
    public static <T extends TreeData> List<T> build(List<T> treeNodes) {
        List<T> result = new ArrayList<>();

        Map<Long, T> nodeMap = new LinkedHashMap<>(treeNodes.size());
        for(T treeNode : treeNodes){
            nodeMap.put(treeNode.getId(), treeNode);
        }

        for(T node : nodeMap.values()) {
            T parent = nodeMap.get(node.getPid());
            if(parent != null && !(node.getId().equals(parent.getId()))){
                if(CollectionUtils.isEmpty(parent.getChildList())){
                    parent.setChildList(new ArrayList());
                }
                parent.getChildList().add(node);
                continue;
            }

            result.add(node);
        }

        return result;
    }

}