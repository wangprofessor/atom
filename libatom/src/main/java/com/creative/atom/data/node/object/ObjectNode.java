package com.creative.atom.data.node.object;

import com.creative.atom.data.node.BaseNode;
import com.creative.atom.data.node.IField;

/**
 * Created by wangshouchao on 2017/12/12.
 */

class ObjectNode extends BaseNode implements IObjectNode {
    private String tag;
    private IField field;

    ObjectNode(String name, boolean isArraySub, boolean isClass, Object type, boolean isVariation, String tag, IField field) {
        super(name, isArraySub, isClass, type, isVariation);
        this.tag = tag;
        this.field = field;


        Nodes nodes = null;
        try {
            nodes = getClazz().getAnnotation(Nodes.class);
        } catch (Throwable ignore) {}
        if (nodes != null) {
            setMapToArray(nodes.mapToArray());
        }
    }

    @Override
    public IField getField() {
        return field;
    }

    @Override
    public void setField(IField field) {
        this.field = field;
    }

    @Override
    public String tag() {
        return tag;
    }

    @Override
    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "ObjectNode{" +
                "tag='" + tag + '\'' +
                ", field=" + field +
                ", " + super.toString() +
                '}';
    }
}
