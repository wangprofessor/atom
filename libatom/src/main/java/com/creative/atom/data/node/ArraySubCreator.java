package com.creative.atom.data.node;

import java.util.HashMap;

/**
 * Created by wangshouchao on 2017/12/29.
 */

public class ArraySubCreator {
    public interface IOnCreatorNotFound {
        Object onNotFound(Object source);
    }

    private final HashMap<Object, Object> mTargetMap = new HashMap<>();

    private final INodeCreator mTargetNodeCreator;
    private final IValueGet mSourceValueGet;
    private final String mTagName;

    private ArraySubCreator mNext;

    private IOnCreatorNotFound mOnCreatorNotFound;

    public ArraySubCreator(INodeCreator targetNodeCreator, IValueGet sourceValueGet, String tagName) {
        mTargetNodeCreator = targetNodeCreator;
        mSourceValueGet = sourceValueGet;
        mTagName = tagName;
    }

    public void putTag(Object tagValue, Object creator) {
        mTargetMap.put(tagValue, creator);
    }

    private Object getCreatorInner(Object source) {
        Object tagValue = mSourceValueGet.getMapValue(null, source, mTagName);
        if (tagValue == null) {
            return null;
        }
        return mTargetMap.get(tagValue);
    }

    private Object getCreator(Object source) {
        ArraySubCreator next = this;
        Object creator = null;
        while (next != null && (creator = next.getCreatorInner(source)) == null) {
            next = next.mNext;
        }
        return creator;
    }

    INode getNode(Object source) {
        Object creator = getCreator(source);
        if (creator == null) {
            if (mOnCreatorNotFound == null) {
                return null;
            } else {
                creator = mOnCreatorNotFound.onNotFound(source);
                if (creator == null) {
                    return null;
                }
            }
        }
        return mTargetNodeCreator.createSubNode(creator, null, null, true, null);
    }

    public ArraySubCreator setNext(ArraySubCreator next) {
        mNext = next;
        return next;
    }

    public void setOnNotFound(IOnCreatorNotFound onNotFound) {
        mOnCreatorNotFound = onNotFound;
    }
}
