package com.creative.atom.data.node;

import java.lang.reflect.Array;
import java.util.Map;

/**
 * Created by wangshouchao on 2017/12/16.
 */

public class ValueRecombine {
    private static final String TAG = "ValueRecombine";
    private static final boolean DEBUG = false;
    private static void println(String message) {
        if (DEBUG) {
            System.out.println(TAG + " " + message);
        }
    }

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_COPY = 1;

    public static void recombine(Object source, Object target) {
        recombine(source, target, TYPE_NORMAL);
    }

    public static void recombine(Object source, Object target, int recombineType) {
        recombine(source, target, recombineType, null);
    }

    public static void recombine(Object source, Object target, int recombineType, Map<Class<?>, String[]> namesMap) {
        recombine(source, target, recombineType, null, namesMap);
    }

    public static void recombine(Object source, Object target, int recombineType, Map<Class<?>, ArraySubCreator> arraySubCreatorMap, Map<Class<?>, String[]> namesMap) {
        NodeHolders nodeHolders = new NodeHolders();
        INodes sourceNodes = NodesCreator.createNodes(source, nodeHolders);
        INodes targetNodes = NodesCreator.createNodes(target, nodeHolders);

        NodeArrayMap<Object, Object> recombines = new NodeArrayMap<>();
        recombines.put(source, target);
        recombineInner(
                source, sourceNodes,
                target, targetNodes,
                nodeHolders, recombineType,
                arraySubCreatorMap, recombines, 0, namesMap
        );
    }

    private static void recombineInner(Object source, INodes sourceSelfNodes,
                                       Object target, INodes targetSelfNodes,
                                       NodeHolders nodeHolders, int recombineType,
                                       Map<Class<?>, ArraySubCreator> arraySubCreatorMap,
                                       NodeArrayMap<Object, Object> recombineNodes, int depth, Map<Class<?>, String[]> namesMap) {
        boolean supportAdd = targetSelfNodes.supportAdd();
        IValueGet sourceValueGet = nodeHolders.getValueGet(sourceSelfNodes.getType());
        println(NodeUtils.getDepthString(depth) + "重组, sourceNode：" + sourceSelfNodes + ", targetNode：" + targetSelfNodes);
        if (sourceSelfNodes.isMapToArray() ^ targetSelfNodes.isMapToArray()) {
            if (sourceSelfNodes.isMapToArray()) {
                if (!(targetSelfNodes instanceof INodeArray)) {
                    return;
                }
                INodes sourceSubNodes = (INodes) sourceSelfNodes.getSplitSubNodes()[0];
                INodes sourceSelfSubNodes = sourceSubNodes.isVariation() ? sourceSubNodes.getVariationNodes() : sourceSubNodes;

                Object sourceSubValue = sourceValueGet.getMapValue(sourceSubNodes, source, sourceSubNodes.name());

                recombineInner(
                        sourceSubValue, sourceSelfSubNodes,
                        target, targetSelfNodes,
                        nodeHolders, recombineType,
                        arraySubCreatorMap, recombineNodes, depth, namesMap
                );
            } else {
                if (!(sourceSelfNodes instanceof INodeArray)) {
                    return;
                }
                INodes targetSubNodes = (INodes) targetSelfNodes.getSplitSubNodes()[0];
                INodes targetSelfSubNodes = targetSubNodes.isVariation() ? targetSubNodes.getVariationNodes() : targetSubNodes;

                IValueCreator valueCreator = nodeHolders.getValueCreator(targetSelfSubNodes.getType());
                Object targetSubValue = valueCreator.createArrayValue(targetSelfSubNodes.getClazz(), sourceSelfNodes.getClazz(), ((INodeArray) sourceSelfNodes).getSubNodeArray().length);

                IValueSet targetValueSet = nodeHolders.getValueSet(targetSelfNodes.getType());
                targetValueSet.setMapValue(targetSubNodes, sourceSelfNodes, target, targetSubNodes.name(), targetSubValue);

                recombineInner(
                        source, sourceSelfNodes,
                        targetSubValue, targetSelfSubNodes,
                        nodeHolders, recombineType,
                        arraySubCreatorMap, recombineNodes, depth, namesMap
                );
            }
        } else if (sourceSelfNodes instanceof INodeMap && targetSelfNodes instanceof INodeMap) {
            Map<String, INode> sourceSelfNodeMap = ((INodeMap) sourceSelfNodes).getSubNodeMap();
            Map<String, INode> targetSelfNodeMap = ((INodeMap) targetSelfNodes).getSubNodeMap();

            println(NodeUtils.getDepthString(depth) +
                    "重组map, 类型:" + recombineType +
                    ", 子节点生成器:" + (arraySubCreatorMap != null) +
                    ", source子节点数:" + sourceSelfNodeMap.size() +
                    ", target子节点数:" + targetSelfNodeMap.size()
            );
            int i = 0;
            for (Map.Entry<String, INode> entry : sourceSelfNodeMap.entrySet()) {
                String name = entry.getKey();
                INode sourceSubNode = entry.getValue();

                Class<?> sourceSubClass = sourceSubNode.getClazz();
                if (isNameInvalid(namesMap, sourceSubClass, name)) {
                    continue;
                }

                Object sourceSubValue = sourceValueGet.getMapValue(sourceSubNode, source, sourceSubNode.name());

                INode targetSubNode = targetSelfNodeMap.get(name);
                if (!supportAdd && targetSubNode == null) {
                    continue;
                } else if (targetSubNode != null) {
                    Class<?> targetSubClass = targetSubNode.getClazz();
                    if (isNameInvalid(namesMap, targetSubClass, name)) {
                        continue;
                    }
                }

                println(NodeUtils.getDepthString(depth) +
                        "遍历重组,子节点序号:" + i +
                        ", source子节点:" + sourceSubNode +
                        ", target子节点:" + targetSubNode
                );

                recombineValueInner(
                        sourceSubValue, sourceSubNode,
                        target, targetSelfNodes, targetSubNode,
                        nodeHolders, recombineType, -1,
                        arraySubCreatorMap, false,
                        recombineNodes, depth, namesMap
                );
                i++;
            }
        } else if (sourceSelfNodes instanceof INodeArray && targetSelfNodes instanceof INodeArray) {
            INodeArray targetSelfArrayNodes = (INodeArray) targetSelfNodes;
            INode[] sourceSelfNodeArray = ((INodeArray) sourceSelfNodes).getSubNodeArray();
            INode[] targetSelfNodeArray = targetSelfArrayNodes.getSubNodeArray();

            boolean isTargetClass = targetSelfArrayNodes.isClass();
            int length = sourceSelfNodeArray.length;

            if (!supportAdd) {
                if (arraySubCreatorMap == null) {
                    if (isTargetClass) {
                        length = sourceSelfNodeArray.length;
                    } else {
                        length = Math.min(sourceSelfNodeArray.length, targetSelfNodeArray.length);
                    }
                } else {
                    length = sourceSelfNodeArray.length;
                }
            }

            println(NodeUtils.getDepthString(depth) +
                    "重组array, 类型:" + recombineType +
                    ", 子节点生成器:" + (arraySubCreatorMap != null) +
                    ", source子节点数:" + sourceSelfNodeArray.length +
                    ", target子节点数:" + (targetSelfNodeArray == null ? null : targetSelfNodeArray.length) +
                    ", 采用长度:" + length
            );

            for (int i = 0; i < length; i++) {
                INode sourceSubNode = sourceSelfNodeArray[i];

                Object sourceSubValue = sourceValueGet.getArrayValue(sourceSubNode, source, i);

                INode targetSubNode;
                ArraySubCreator arraySubCreator = null;
                if (supportAdd) {
                    targetSubNode = null;
                } else {
                    if (arraySubCreatorMap != null) {
                        Class<?> creatorClazz;
                        Class<?> clazz;
                        Object type = targetSelfArrayNodes.getType();
                        boolean arrayClass = true;
                        if (type == null) {
                            clazz = targetSelfArrayNodes.getClazz();
                        } else {
                            if (type instanceof Class<?>) {
                                clazz = (Class<?>) type;
                            } else {
                                int size = Array.getLength(type);
                                if (size == 0) {
                                    clazz = type.getClass();
                                } else {
                                    Object first = Array.get(type, 0);
                                    clazz = first.getClass();
                                    arrayClass = false;
                                }
                            }
                        }
                        if (arrayClass) {
                            creatorClazz = clazz.getComponentType();
                        } else {
                            creatorClazz = clazz;
                        }

                        arraySubCreator = arraySubCreatorMap.get(creatorClazz);
                    }
                    if (arraySubCreator == null) {
                        if (targetSelfNodeArray == null) {
                            throw new RuntimeException();
                        }
                        if (isTargetClass) {
                            targetSubNode = targetSelfNodeArray[0];
                        } else {
                            if (i >= targetSelfNodeArray.length) {
                                break;
                            }
                            targetSubNode = targetSelfNodeArray[i];
                        }
                    } else {
                        targetSubNode = arraySubCreator.getNode(sourceSubValue);
                        if (targetSubNode != null && targetSubNode instanceof INodes) {
                            println(NodeUtils.getDepthString(depth + 1) + "使用子节点生成器重新拆分子节点");
                            NodesCreator.createNodes((INodes) targetSubNode, targetSubNode.getType(), depth + 1);
                        }
                    }
                }

                println(NodeUtils.getDepthString(depth) +
                        "遍历重组, 子节点序号:" + i +
                        ", source子节点:" + sourceSubNode +
                        ", target子节点:" + targetSubNode
                );

                recombineValueInner(
                        sourceSubValue, sourceSubNode,
                        target, targetSelfNodes, targetSubNode,
                        nodeHolders, recombineType, i,
                        arraySubCreatorMap, arraySubCreator != null,
                        recombineNodes, depth, namesMap
                );
            }
        } else {
            return;
        }

        if (depth == 0) {
            println("");
        }
    }

    private static boolean isNameInvalid(Map<Class<?>, String[]> namesMap, Class<?> clazz, String name) {
        if (namesMap == null) {
            return false;
        }
        String[] names = namesMap.get(clazz);
        if (names == null) {
            return false;
        }

        boolean valid = false;
        for (String each : names) {
            if (each.equals(name)) {
                valid = true;
                break;
            }
        }
        return !valid;
    }

    private static void recombineValueInner(Object sourceSubValue, INode sourceSubNode,
                                            Object target, INodes targetSelfNodes, INode targetSubNode,
                                            NodeHolders nodeHolders, int recombineType, int index,
                                            Map<Class<?>, ArraySubCreator> arraySubCreatorMap, boolean hasSubCreator,
                                            NodeArrayMap<Object, Object> recombineNodes, int depth, Map<Class<?>, String[]> namesMap) {
        boolean supportAdd = targetSelfNodes.supportAdd();
        if (!supportAdd && targetSubNode == null) {
            return;
        }

        INode sourceSelfSubNode = sourceSubNode.isVariation() ? sourceSubNode.getVariationNodes() : sourceSubNode;
        INode targetSelfSubNode = null;
        if (targetSubNode != null) {
            if (targetSubNode.isVariation()) {
                targetSelfSubNode = targetSubNode.getVariationNodes();
            } else {
                targetSelfSubNode = targetSubNode;
            }
        }

        Object targetSubValue;
        boolean createNewSubNodesValue = true;

        if (sourceSubValue == null) {
            targetSubValue = null;
        } else {
            if (sourceSelfSubNode instanceof INodes) {
                targetSubValue = recombineNodes.get(sourceSubValue);
                if(targetSubValue == null) {
                    INodes createClassNodes;
                    boolean mapOrArray;
                    if (supportAdd || recombineType == TYPE_COPY) {
                        createClassNodes = getCreateClassNode(targetSelfNodes, targetSelfSubNode, sourceSelfSubNode, recombineType);
                        IValueCreator valueCreator = nodeHolders.getValueCreator(createClassNodes.getType());
                        println(NodeUtils.getDepthString(depth) +
                                "supportAdd or copy, valueCreator:" + valueCreator.getClass().getName() +
                                ", createClassNodes:" + createClassNodes.getClass().getName() +
                                ", isMap:" + (sourceSelfSubNode instanceof INodeMap));
                        if (sourceSelfSubNode instanceof INodeMap) {
                            targetSubValue = valueCreator.createMapValue(targetSelfSubNode == null ? null : targetSelfSubNode.getClazz(), sourceSelfSubNode.getClazz());
                            mapOrArray = true;
                        } else if (sourceSelfSubNode instanceof INodeArray) {
                            int valueLength = ((INodeArray) sourceSelfSubNode).getSubNodeArray().length;
                            targetSubValue = valueCreator.createArrayValue(targetSelfSubNode == null ? null : targetSelfSubNode.getClazz(), sourceSelfSubNode.getClazz(), valueLength);
                            mapOrArray = false;
                        } else {
                            throw new RuntimeException();
                        }
                    } else {
                        createClassNodes = (INodes) targetSelfSubNode;
                        IValueCreator valueCreator = nodeHolders.getValueCreator(createClassNodes.getType());
                        println(NodeUtils.getDepthString(depth) +
                                "normal, valueCreator:" + valueCreator.getClass().getName() +
                                ", createClassNodes:" + createClassNodes.getClass().getName() +
                                ", isMap:" + (sourceSelfSubNode instanceof INodeMap));
                        if (sourceSelfSubNode instanceof INodeMap) {
                            Object type = createClassNodes.getType();
                            if (type == null || type instanceof Class || hasSubCreator) {
                                targetSubValue = valueCreator.createMapValue(targetSelfSubNode.getClazz(), sourceSelfSubNode.getClazz());
                            } else {
                                targetSubValue = type;
                                createNewSubNodesValue = false;
                            }
                            mapOrArray = true;
                        } else if (sourceSelfSubNode instanceof INodeArray) {
                            int valueLength = ((INodeArray) sourceSelfSubNode).getSubNodeArray().length;
                            if (targetSelfSubNode.isMapToArray()) {
                                targetSubValue = createClassNodes.getType();
                                createNewSubNodesValue = false;
                            } else {
                                targetSubValue = valueCreator.createArrayValue(targetSelfSubNode.getClazz(), sourceSelfSubNode.getClazz(), valueLength);
                            }
                            mapOrArray = false;
                        } else {
                            throw new RuntimeException();
                        }
                    }

                    recombineNodes.put(sourceSubValue, targetSubValue);

                    if (createNewSubNodesValue) {
                        if (mapOrArray == createClassNodes instanceof INodeMap) {
                            targetSelfSubNode = createClassNodes;
                        } else {
                            targetSelfSubNode = NodesCreator.createNodes(targetSubValue, nodeHolders);
                        }
                    }
                    recombineInner(
                            sourceSubValue, (INodes) sourceSelfSubNode,
                            targetSubValue, (INodes) targetSelfSubNode,
                            nodeHolders, recombineType,
                            arraySubCreatorMap, recombineNodes, depth + 1, namesMap
                    );
                }
            } else {
                targetSubValue = sourceSubValue;
            }
        }

        IValueSet targetValueSet = nodeHolders.getValueSet(targetSelfNodes.getType());
        INode targetValueSetNode = supportAdd && targetSubNode == null ? targetSelfNodes : targetSubNode;
        if (index < 0) {
            if (createNewSubNodesValue) {
                targetValueSet.setMapValue(targetValueSetNode, sourceSubNode, target, sourceSubNode.name(), targetSubValue);
            }
        } else {
            targetValueSet.setArrayValue(targetValueSetNode, sourceSubNode, target, index, targetSubValue);
        }
    }

    private static INodes getCreateClassNode(INodes targetSelfNodes, INode targetSelfSubNode, INode sourceSelfSubNode, int recombineType) {
        if (recombineType == TYPE_COPY) {
            return (INodes) sourceSelfSubNode;
        }
        if (targetSelfSubNode == null) {
            return targetSelfNodes;
        }
        return (INodes) targetSelfSubNode;
    }
}
