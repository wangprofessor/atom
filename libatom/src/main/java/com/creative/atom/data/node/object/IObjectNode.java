package com.creative.atom.data.node.object;

import com.creative.atom.data.node.IField;
import com.creative.atom.data.node.INode;

public interface IObjectNode extends INode {
    IField getField();
    void setField(IField field);
    String tag();
    void setTag(String tag);
}
