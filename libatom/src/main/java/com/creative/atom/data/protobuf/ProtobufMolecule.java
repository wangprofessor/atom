package com.creative.atom.data.protobuf;

import com.google.protobuf.Extension;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistryLite;
import com.creative.atom.data.Molecule;
import com.creative.atom.data.node.object.IObjectNode;
import com.creative.atom.data.node.object.Nodes;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangshouchao on 2017/12/30.
 */

@Nodes
public class ProtobufMolecule extends Molecule {
    private static final String TAG = "ProtobufMolecule";

    public final Class<?> clazz;
    public final boolean isActive;
    public final boolean isReceive;

    private final Object mBuilder;
    private Extension mExtension;
    private boolean mIsExtension = false;
    private boolean mIsParsed = false;

    private boolean mIsInitProtobuf = false;

    public ProtobufMolecule() {
        Protobuf protobuf = getClass().getAnnotation(Protobuf.class);
        if (protobuf == null) {
            throw new RuntimeException();
        }
        clazz = protobuf.clazz();
        isActive = protobuf.isActive();
        isReceive = protobuf.isReceive();

        Class<?> clazz = getClazz();
        try {
            mBuilder = invokeGetBuilder(clazz);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void addParent(ProtobufMolecule parent, String name, Extension extension) {
        super.addParent(parent, name);
        setExtension(extension);
    }

    public void setExtension(Extension extension) {
        mExtension = extension;
        mIsExtension = true;
    }

    private void tryInitProtobuf() {
        if (mIsInitProtobuf) {
            return;
        }
        initProtobuf();
        mIsInitProtobuf = true;
    }

    protected void initProtobuf() {

    }

    @Override
    public ProtobufMolecule getRootParent() {
        tryInitProtobuf();
        return (ProtobufMolecule) super.getRootParent();
    }

    public List<ProtobufMolecule> getSubList(boolean isActive) {
        tryInitProtobuf();
        final List<ProtobufMolecule> list = new ArrayList<>();
        traverseSubValue(true, false, new ITraverseGetValue() {
            @Override
            public Object onGetValue(Molecule molecule, Object parent, String name, Object sub, Class<?> clazz, IObjectNode node) {
                if (!(sub instanceof ProtobufMolecule)) {
                    return sub;
                }

                ProtobufMolecule protobufMolecule = (ProtobufMolecule) sub;
                protobufMolecule.tryInitProtobuf();
                if (!protobufMolecule.isReceive) {
                    return sub;
                }
                if (protobufMolecule.isActive != isActive) {
                    return sub;
                }
                if (!protobufMolecule.mIsParsed) {
                    return sub;
                }
                list.add(protobufMolecule);
                return sub;
            }
        });
        return list;
    }

    public byte[] toBytes() {
        try {
            Object protobuf = toProtobuf();
            return invokeToByteArray(protobuf);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object toProtobuf() {
        tryInitProtobuf();
        traverseSubValue(true, false, new ITraverseGetValue() {
            @Override
            public Object onGetValue(Molecule molecule, Object parent, String name, Object sub, Class<?> clazz, IObjectNode node) {
                if (sub == null) {
                    return null;
                }
                try {
                    if (sub instanceof ProtobufMolecule) {
                        ProtobufMolecule protobufMolecule = (ProtobufMolecule) sub;
                        protobufMolecule.tryInitProtobuf();
                        Object protobuf = protobufMolecule.toProtobuf();
                        invokeSetExtension(mBuilder, protobufMolecule.mExtension, protobuf);
                    } else {
                        invokeSet(mBuilder, name, sub);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return sub;
            }
        });
        try {
            return invokeBuild(mBuilder);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void fromBytes(byte[] bytes) {
        tryInitProtobuf();
        try {
            Class<?> clazz = getClazz();
            ExtensionRegistryLite extensionRegistry = getExtensionRegistry();
            Object protobuf = invokeParseFrom(clazz, bytes, extensionRegistry);
            fromProtobuf(protobuf);
        } catch (Exception e) {
            System.out.println(TAG + " fromBytes error:" + getClass());
            throw new RuntimeException(e);
        }
    }

    private ExtensionRegistryLite getExtensionRegistry() {
        tryInitProtobuf();
        fillChildren(true);
        ExtensionRegistryLite extensionRegistry = ExtensionRegistry.newInstance();
        if (mIsExtension) {
            extensionRegistry.add(mExtension);
        }
        traverseSubValue(false, true, new ITraverseGetValue() {
            @Override
            public Object onGetValue(Molecule molecule, Object parent, String name, Object sub, Class<?> clazz, IObjectNode node) {
                if (!(sub instanceof ProtobufMolecule)) {
                    return sub;
                }
                ProtobufMolecule protobufMolecule = (ProtobufMolecule) sub;
                protobufMolecule.tryInitProtobuf();
                if (!protobufMolecule.mIsExtension) {
                    return sub;
                }
                Extension extension = protobufMolecule.mExtension;
                extensionRegistry.add(extension);
                return sub;
            }
        });
        return extensionRegistry;
    }

    private void fromProtobuf(Object protobuf) {
        tryInitProtobuf();
        traverseSubValue(false, false, new ITraverseGetValue() {
            @Override
            public Object onGetValue(Molecule molecule, Object parent, String name, Object sub, Class<?> clazz, IObjectNode node) {
                try {
                    if (ProtobufMolecule.class.isAssignableFrom(clazz)) {
                        ProtobufMolecule protobufMolecule = (ProtobufMolecule) sub;
                        protobufMolecule.tryInitProtobuf();
                        boolean hasExtension = invokeHasExtension(protobuf, protobufMolecule.mExtension);
                        if (hasExtension) {
                            Object subProtobuf = invokeGetExtension(protobuf, protobufMolecule.mExtension);
                            protobufMolecule.fromProtobuf(subProtobuf);
                        }
                    } else {
                        return invokeGet(protobuf, name);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return sub;
            }
        });
        mIsParsed = true;
    }

    private static Object invokeGetBuilder(Class<?> clazz) throws Exception {
        final String newBuilderMethodName = "newBuilder";
        Method method = clazz.getMethod(newBuilderMethodName);
        return method.invoke(null);
    }

    private static void invokeSet(Object builder, String name, Object value) throws Exception {
        final String set = "set";
        String upperCaseName = upperCase(name);
        String setMethodName = set + upperCaseName;
        Class<?> valueClass = value.getClass();
        valueClass = getPrimitiveClass(valueClass);
        Class<?> builderClass = builder.getClass();
        Method setMethod = builderClass.getMethod(setMethodName, valueClass);
        setMethod.invoke(builder, value);
    }

    private static Object invokeGet(Object protobuf, String name) throws Exception {
        final String get = "get";
        String upperCaseName = upperCase(name);
        String setMethodName = get + upperCaseName;
        Class<?> protobufClass = protobuf.getClass();
        Method getMethod = protobufClass.getMethod(setMethodName);
        return getMethod.invoke(protobuf);
    }

    private static void invokeSetExtension(Object builder, Extension extension, Object protobuf) throws Exception {
        final String setExtension = "setExtension";
        Class<?> builderClass = builder.getClass();
        Method setMethod = builderClass.getMethod(setExtension, Extension.class, Object.class);
        setMethod.invoke(builder, extension, protobuf);
    }

    private static boolean invokeHasExtension(Object protobuf, Extension extension) throws Exception {
        final String hasExtension = "hasExtension";
        Class<?> protobufClass = protobuf.getClass();
        Method getMethod = protobufClass.getMethod(hasExtension, Extension.class);
        return (boolean) getMethod.invoke(protobuf, extension);
    }

    private static Object invokeGetExtension(Object protobuf, Extension extension) throws Exception {
        final String getExtension = "getExtension";
        Class<?> protobufClass = protobuf.getClass();
        Method getMethod = protobufClass.getMethod(getExtension, Extension.class);
        return getMethod.invoke(protobuf, extension);
    }

    private static Object invokeBuild(Object builder) throws Exception {
        final String buildMethodName = "build";
        Class<?> builderClass = builder.getClass();
        Method buildMethod = builderClass.getMethod(buildMethodName);
        return buildMethod.invoke(builder);
    }

    private static byte[] invokeToByteArray(Object protobuf) throws Exception {
        final String methodName = "toByteArray";
        Class<?> protobufClass = protobuf.getClass();
        Method method = protobufClass.getMethod(methodName);
        return (byte[]) method.invoke(protobuf);
    }

    private static Object invokeParseFrom(Class<?> clazz, byte[] bytes, ExtensionRegistryLite extensionRegistry) throws Exception {
        final String parseFrom = "parseFrom";
        Method method = clazz.getMethod(parseFrom, byte[].class, ExtensionRegistryLite.class);
        return method.invoke(null, bytes, extensionRegistry);
    }

    private static String upperCase(String fieldName) {
        char[] chars=fieldName.toCharArray();
        chars[0]-=32;
        return String.valueOf(chars);
    }

    private static Class<?> getPrimitiveClass(Class<?> wrapperClass) {
        Class<?> resultClass = wrapperClass;
        if (wrapperClass == Integer.class) {
            resultClass = int.class;
        } else if (wrapperClass == Long.class) {
            resultClass = long.class;
        } else if (wrapperClass == Short.class) {
            resultClass = short.class;
        } else if (wrapperClass == Byte.class) {
            resultClass = byte.class;
        } else if (wrapperClass == Boolean.class) {
            resultClass = boolean.class;
        } else if (wrapperClass == Float.class) {
            resultClass = float.class;
        } else if (wrapperClass == Double.class) {
            resultClass = double.class;
        }
        return resultClass;
    }
}
