package com.creative.atom.data.node;

import com.creative.atom.data.node.json.JsonNodeHolder;
import com.creative.atom.data.node.map.MapNodeHolder;
import com.creative.atom.data.node.object.ObjectNodeHolder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by wangshouchao on 2017/12/12.
 */

public class NodesCreator {
    private static final boolean DEBUG = false;
    private static void println(String message) {
        if (DEBUG) {
            System.out.println(message);
        }
    }

    public static boolean APPLY_SELF = true;

    public static final int SPLIT_TYPE_NORMAL = 0;
    public static final int SPLIT_TYPE_PARENT_TREE = 1;
    public static final int SPLIT_TYPE_SUB_TREE = 2;

    public static INodes createNodes(Object type) {
        NodeHolders nodeHolders = new NodeHolders();
        return createNodes(type, nodeHolders);
    }

    static INodes createNodes(Object type, NodeHolders nodeHolders) {
        return createNodes(type, nodeHolders, 0);
    }

    private static INodes createNodes(Object type, NodeHolders nodeHolders, int depth) {
        INodeCreator rootCreator = nodeHolders.getNodeCreator(type);
        INodes rootNodes = rootCreator.createRootNodes(type);

        createNodeMapInner(nodeHolders, rootNodes, type, depth);
        return rootNodes;
    }

    static void createNodes(INodes nodes, Object type, int depth) {
        NodeHolders nodeHolders = new NodeHolders();

        createNodeMapInner(nodeHolders, nodes, type, depth);
    }

    private static void createNodeMapInner(NodeHolders nodeHolders, INodes nodes, Object type, int depth) {
        INode[] subs;
        if (nodes.isSplit()) {
            subs = nodes.getSplitSubNodes();
        } else {
            INodeSplit nodeSplit = nodeHolders.getNodeSplit(type);
            INodeCreator nodeCreator = nodeHolders.getNodeCreator(type);
            subs = nodeSplit.splitSubs(type, nodes, nodeCreator, SPLIT_TYPE_NORMAL);
            nodes.setIsSplit(true);
        }

        int length = subs.length;
        println(NodeUtils.getDepthString(depth) + "拆分, 子节点个数:" + length + ", 父节点:" + nodes);
        for (int i = 0; i < length; i++) {
            INode subNode = subs[i];
            println(NodeUtils.getDepthString(depth) + "遍历拆分, 子节点序号:" + i + ", 子节点:" + subNode);
            Object subType = subNode.getType();
            if (subNode instanceof INodes) {
                INodes subNodes = (INodes) subNode;
                INodeSplit subNodeSplit = nodeHolders.getNodeSplit(subType);
                INodes cacheSubNodes = subNodeSplit.getCacheSplitNodes(subType);
                if (cacheSubNodes == null) {
                    println(NodeUtils.getDepthString(depth + 1) + "不存在拆分缓存, 递归");
                    createNodeMapInner(nodeHolders, subNodes, subType, depth + 1);
                } else {
                    println(NodeUtils.getDepthString(depth + 1) + "存在拆分缓存, 缓存为:" + cacheSubNodes);
                    if (subNodes instanceof INodeMap) {
                        INodeMap subMapNodes = (INodeMap) subNodes;
                        INodeMap subCacheMapNodes = (INodeMap) cacheSubNodes;
                        subMapNodes.mapSetTypeMap(subCacheMapNodes.mapGetTypeMap());
                    } else if (subNodes instanceof INodeArray) {
                        INodeArray subArrayNodes = (INodeArray) subNodes;
                        INodeArray subCacheArrayNodes = (INodeArray) cacheSubNodes;
                        subArrayNodes.arraySetTypeArray(subCacheArrayNodes.arrayGetTypeArray());
                    } else {
                        throw new RuntimeException();
                    }
                }
            } else if (subNode.isVariation()) {
                INodes subNodes = createNodes(subType, nodeHolders, depth);
                subNode.setVariationNodes(subNodes);
            }

            if (nodes instanceof INodeMap) {
                INodeMap nodeMap = (INodeMap) nodes;
                nodeMap.mapSetType(subNode);
            } else if (nodes instanceof INodeArray) {
                INodeArray nodeArray = (INodeArray) nodes;
                nodeArray.arraySetType(i, subNode);
            } else {
                throw new RuntimeException();
            }
        }

        if (depth == 0) {
            println("");
        }
    }

    private static final HashMap<Class<?>, INodeHolder> sNodeHolderMap = new HashMap<>();
    private static final ObjectNodeHolder sObjectNodeHolder = new ObjectNodeHolder();

    public static void registerNodeHolder(Class<?> clazz, INodeHolder nodeHolder) {
        sNodeHolderMap.put(clazz, nodeHolder);
    }

    static {
        INodeHolder json = new JsonNodeHolder();
        sNodeHolderMap.put(JSONObject.class, json);
        sNodeHolderMap.put(JSONArray.class, json);

        sNodeHolderMap.put(HashMap.class, new MapNodeHolder());
    }

    static INodeHolder getNodeHolder(HashMap<Class<?>, INodeHolder> nodeHolderMap, Object type) {
        Class<?> clazz = NodeUtils.getTypeClass(type);
        INodeHolder nodeHolder = nodeHolderMap.get(clazz);
        if (nodeHolder == null) {
            nodeHolder = getNodeHolder(clazz);
            if (nodeHolder == null) {
                throw new RuntimeException();
            }
            nodeHolderMap.put(clazz, nodeHolder.copy());
        }
        return nodeHolder;
    }

    public static INodeHolder getNodeHolderByType(Object type) {
        Class<?> clazz = NodeUtils.getTypeClass(type);
        return getNodeHolder(clazz);
    }

    public static INodeHolder getNodeHolder(Class<?> clazz) {
        INodeHolder nodeHolder = sNodeHolderMap.get(clazz);
        if(nodeHolder == null) {
            nodeHolder = sObjectNodeHolder;
        }
        return nodeHolder;
    }

}
