package com.creative.atom.data;

import com.creative.atom.data.node.ArraySubCreator;
import com.creative.atom.data.node.INode;
import com.creative.atom.data.node.INodeArray;
import com.creative.atom.data.node.INodeHolder;
import com.creative.atom.data.node.INodeMap;
import com.creative.atom.data.node.INodes;
import com.creative.atom.data.node.ISelfCreate;
import com.creative.atom.data.node.ISelfSplit;
import com.creative.atom.data.node.ISelfSub;
import com.creative.atom.data.node.IValueCreator;
import com.creative.atom.data.node.IValueGet;
import com.creative.atom.data.node.IValueSet;
import com.creative.atom.data.node.NodesCreator;
import com.creative.atom.data.node.ValueRecombine;
import com.creative.atom.data.node.object.IObject;
import com.creative.atom.data.node.object.IObjectNode;
import com.creative.atom.data.node.object.Nodes;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangshouchao on 2017/12/3.
 */

public class Molecule implements IObject, ISelfCreate, ISelfSub, ISelfSplit {
    private static final IValueGet sValueGet;
    private static final IValueSet sValueSet;
    public static final IValueCreator sValueCreator;

    static {
        INodeHolder nodeHolder = NodesCreator.getNodeHolder(Object.class);
        sValueGet = nodeHolder.getValueGet();
        sValueSet = nodeHolder.getValueSet();
        sValueCreator = nodeHolder.getValueCreator();
    }

    public static Molecule create(Class<? extends Molecule> clazz) {
        return (Molecule) sValueCreator.createMapValue(clazz, null);
    }

    public interface ITraverseGetValue {
        Object onGetValue(Molecule molecule, Object parent, String name, Object sub,
                          Class<?> clazz, IObjectNode node);
    }

    private String name;
    private String tag;

    private Molecule mRootParent[] = new Molecule[] {this};
    private Molecule mParent;
    private boolean mAsArrayInParent;
    private INodeMap mNodeMap;
    private Map<String, Object> mSubMap;

    public Molecule(Molecule molecule) {
        this();
        recombineFrom(molecule);
    }

    protected Molecule() {
        this(true);
    }

    protected Molecule(boolean init) {
        if (!init) {
            return;
        }
        tryInit();
    }

    protected void tryInit() {
        Class<? extends Molecule> clazz = getClass();
        Nodes nodes = clazz.getAnnotation(Nodes.class);
        String simpleName = clazz.getSimpleName();
        if(nodes == null) {
            name = simpleName;
            tag = name;
        } else {
            name = nodes.name();
            if(name.equals("")) {
                name = simpleName;
            }
            tag = nodes.tag();
            if (tag.equals("")) {
                tag = name;
            }
        }
    }

    public String name() {
        return name;
    }

    public String tag() {
        return tag;
    }

    public Molecule create() {
        return create(getClass());
    }

    public void setSub(String name, Object value) {
        INode node = getNode(name);
        if (node == null) {
            return;
        }
        sValueSet.setMapValue(node, null, this, name, value);
    }

    public Object getSub(String name) {
        INode node = getNode(name);
        if (node == null) {
            return null;
        }
        return sValueGet.getMapValue(node, this, name);
    }

    /***INJECT METHOD***/

    @Override
    public Molecule selfCreate() {
        throw new RuntimeException();
    }

    @Override
    public Object selfCreateArray(int length) {
        throw new RuntimeException();
    }

    @Override
    public void selfSetSub(String name, Object value) {
        throw new RuntimeException();
    }

    @Override
    public Object selfGetSub(String name) {
        throw new RuntimeException();
    }

    @Override
    public void selfSetArraySub(Object array, int index, Object value) {
        throw new RuntimeException();
    }

    @Override
    public Object selfGetArraySub(Object array, int index) {
        throw new RuntimeException();
    }

    @Override
    public INode[] selfSplitSubs() {
        throw new RuntimeException();
    }

    /***INJECT METHOD***/

    public HashMap toMap() {
        HashMap<String, Object> hashMap = new HashMap<>();
        recombineTo(hashMap);
        return hashMap;
    }

    public Molecule getRootParent() {
        return mRootParent[0];
    }

    public Molecule getParent() {
        return mParent;
    }

    public boolean asArrayInParent() {
        return mAsArrayInParent;
    }

    public void addParent(Molecule parent, String name) {
        addParent(parent, name, false, 0);
    }

    public void addParent(Molecule parent, String name, boolean asArray) {
        addParent(parent, name, asArray, 1);
    }

    public void addParent(Molecule parent, String name, boolean asArray, int arrayLength) {
        if (mParent != null) {
            return;
        }
        parent.setChild(this, name, asArray, arrayLength);
        mParent = parent;
        mRootParent[0] = parent;
        parent.mRootParent = mRootParent;
    }

    public Atoms splitToAtoms() {
        return splitToAtoms(null);
    }

    public Atoms splitToAtoms(Map<Class<?>, String[]> namesMap) {
        HashMap<String, Object> atomMap = new HashMap<>();
        recombineTo(atomMap, namesMap);

        Atoms atoms = new Atoms();
        atoms.valueMap = atomMap;
        atoms.name = name();

        return atoms;
    }

    public void combineFromAtoms(Atoms atoms) {
        combineFromAtoms(atoms, null);
    }

    public void combineFromAtoms(Atoms atoms, Map<Class<?>, String[]> namesMap) {
        recombineFrom(atoms.valueMap, null, namesMap);
    }

    public void recombineTo(Object another) {
        recombineTo(another, null);
    }

    public void recombineTo(Object another, Map<Class<?>, String[]> namesMap) {
        ValueRecombine.recombine(this, another, ValueRecombine.TYPE_NORMAL, namesMap);
    }

    public void recombineTo(Object another, int recombineType) {
        recombineTo(another, recombineType, null);
    }

    public void recombineTo(Object another, int recombineType, Map<Class<?>, String[]> namesMap) {
        ValueRecombine.recombine(this, another, recombineType, namesMap);
    }

    public void recombineFrom(Object another) {
        recombineFrom(another, null);
    }

    public void recombineFrom(Object another, Map<Class<?>, ArraySubCreator> arraySubCreatorMap) {
        recombineFrom(another, arraySubCreatorMap, null);
    }

    public void recombineFrom(Object another, Map<Class<?>, ArraySubCreator> arraySubCreatorMap, Map<Class<?>, String[]> namesMap) {
        ValueRecombine.recombine(another, this, ValueRecombine.TYPE_NORMAL, arraySubCreatorMap, namesMap);
    }

    public Object setChild(Molecule molecule, String name) {
        return setChild(molecule, name, false, -1);
    }

    public Object setChild(Molecule molecule, String name, boolean asArray, int arrayLength) {
        molecule.mAsArrayInParent = asArray;

        Object sub;
        if (asArray) {
            Class<?> clazz = molecule.getClass();
            sub = sValueCreator.createArrayValue(clazz, null, arrayLength);
            Arrays.fill(( Molecule[]) sub, molecule);
        } else {
            sub = molecule;
        }

        setSub(name, sub);
        return sub;
    }

    public Molecule getChild(String name) {
        Object sub = getSub(name);
        if (!(sub instanceof Molecule)) {
            return null;
        }
        return (Molecule) sub;
    }

    public void fillChildren(boolean iteration) {
        traverseSubValue(false, iteration, new ITraverseGetValue() {
            @Override
            public Object onGetValue(Molecule molecule, Object parent, String name, Object sub, Class<?> clazz, IObjectNode node) {
                if (clazz.isArray()) {
                    Class<?> componentType = clazz.getComponentType();
                    sub = sValueCreator.createArrayValue(componentType, null, 1);
                    if (!Molecule.class.isAssignableFrom(componentType)) {
                        return sub;
                    }
                    Molecule arraySub = (Molecule) sValueCreator.createMapValue(componentType, null);
                    arraySub.fillChildren(true);
                    sValueSet.setArrayValue(null, null, sub, 0, arraySub);
                    return sub;
                }
                if (!Molecule.class.isAssignableFrom(clazz)) {
                    return sub;
                }
                if (sub != null) {
                    return sub;
                }
                sub = sValueCreator.createMapValue(clazz, null);
                return sub;
            }
        });
    }

    public void traverseSubValue(boolean update, boolean iteration, ITraverseGetValue getValue) {
        Map<String, Object> subMap = new HashMap<>();
        final boolean needUpdate = update || mSubMap == null;
        traverseSubNode(false, false, new ITraverseSubNode() {
            @Override
            public void onTraverseSubNode(String name, IObjectNode node) {
                Object value;
                if (needUpdate) {
                    value = Molecule.this.getSub(name);
                    subMap.put(name, value);
                } else {
                    value = mSubMap.get(name);
                }

                traverseSubValueInner(update, iteration, name, node, value, Molecule.this,
                        Molecule.this, -1, getValue, subMap);
                if (value == null) {
                    return;
                }

                Class<?> clazz = value.getClass();
                if (clazz.isArray() &&
                        Molecule.class.isAssignableFrom(clazz.getComponentType())) {
                    int length = Array.getLength(value);
                    for (int i = 0; i < length; i++) {
                        Object arraySub = sValueGet.getArrayValue(null, value, i);
                        traverseSubValueInner(update, iteration, name, node, arraySub, Molecule.this,
                                value, i, getValue, subMap);
                    }
                }
            }
            @Override
            public void onTraverseSubNode(int index, IObjectNode node) {}
        });
        if (needUpdate) {
            mSubMap = subMap;
        } else {
            mSubMap.putAll(subMap);
        }
    }

    private void traverseSubValueInner(boolean update, boolean iteration, String name, IObjectNode node, Object value,
                                       Molecule molecule, Object parent, int index, ITraverseGetValue getValue, Map<String, Object> subMap) {
        if (value instanceof Molecule) {
            Molecule subMolecule = (Molecule) value;
            subMolecule.mRootParent = mRootParent;
            subMolecule.mParent = this;
        }

        Class<?> clazz;
        if (value == null) {
            clazz = node.getClazz();
        } else {
            clazz = value.getClass();
        }
        Object newValue = getValue.onGetValue(molecule, parent, name, value, clazz, node);
        if (name != null) {
            subMap.put(name, newValue);
        }
        if (newValue != value) {
            if (index < 0) {
                sValueSet.setMapValue(node, null, parent, name, newValue);
            } else {
                sValueSet.setArrayValue(node, null, parent, index, newValue);
            }
        }
        if (iteration && newValue instanceof Molecule) {
            Molecule newMolecule = (Molecule) newValue;
            newMolecule.traverseSubValue(update, true, getValue);
        }
    }

    protected IObjectNode getNode(String name) {
        INodeMap nodeMap = getNodeMap(false);
        Map<String, INode> subNodeMap = nodeMap.getSubNodeMap();
        return (IObjectNode) subNodeMap.get(name);
    }

    private INodeMap getNodeMap(boolean update) {
        if (!update && mNodeMap != null) {
            return mNodeMap;
        }
        mNodeMap = createNodeMap();
        return mNodeMap;
    }

    public INodeMap createNodeMap() {
        return (INodeMap) NodesCreator.createNodes(this);
    }

    private void traverseSubNode(boolean update, boolean iteration, ITraverseSubNode onTraverseSubNode) {
        INodeMap nodeMap = getNodeMap(update);
        traverseSubNode(nodeMap, iteration, onTraverseSubNode);
    }

    private void traverseSubNode(INodes nodes, boolean iteration, ITraverseSubNode onTraverseSubNode) {
        if (nodes instanceof INodeMap) {
            INodeMap nodeMap = (INodeMap) nodes;
            Map<String, INode> subNodeMap = nodeMap.getSubNodeMap();
            for (Map.Entry<String, INode> entry : subNodeMap.entrySet()) {
                String name = entry.getKey();
                IObjectNode node = (IObjectNode) entry.getValue();
                Class<?> clazz = node.getClazz();
                if (Molecule.class.isAssignableFrom(clazz) && iteration) {
                    traverseSubNode((INodes) node, true, onTraverseSubNode);
                }
                onTraverseSubNode.onTraverseSubNode(name, node);
            }
        } else if (nodes instanceof INodeArray) {
            INodeArray nodeArray = (INodeArray) nodes;
            INode[] subNodeArray = nodeArray.getSubNodeArray();
            int length = subNodeArray.length;
            for (int i = 0; i < length; i++) {
                IObjectNode node = (IObjectNode) subNodeArray[i];
                Class<?> clazz = node.getClazz();
                if (Molecule.class.isAssignableFrom(clazz) && iteration) {
                    traverseSubNode((INodes) node, true, onTraverseSubNode);
                }
                onTraverseSubNode.onTraverseSubNode(i, node);
            }
        } else {
            throw new RuntimeException();
        }
    }

    private interface ITraverseSubNode {
        void onTraverseSubNode(String name, IObjectNode node);
        void onTraverseSubNode(int index, IObjectNode node);
    }

}
