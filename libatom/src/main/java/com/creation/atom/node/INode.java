package com.creation.atom.node;

public interface INode {
    int KEY_INT = 0;
    int KEY_STRING = 1;

    int SIZE_FIXED = 0;
    int SIZE_UNFIXED = 1;

    int CLASS_BOUNDED = 0;
    int CLASS_UNBOUNDED = 1;

    int getKeyType();
    int getSizeType();
    int getClassType();

    Class<?> getClazzBound();

    boolean hasOriginValue();
    Object getOriginValue();

    boolean isChild();
    void setChild(IChild child);
    IChild getChild();

    boolean isParent();
    void setParent(IParent parent);
    IParent getParent();
}
