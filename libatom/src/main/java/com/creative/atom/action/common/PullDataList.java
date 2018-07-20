package com.creative.atom.action.common;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by wangshouchao on 2017/12/18.
 */

public class PullDataList<T> extends ArrayList<T> {
    public final int createSource;
    public int getSource;
    public boolean hasRemaining = false;
    public long version = 0;

    public PullDataList() {
        this(IPull.SOURCE_UNDEFINE);
    }

    public PullDataList(int createSource) {
        this.createSource = createSource;
        this.getSource = createSource;
    }

    @Override
    public T set(int i, T t) {
        version++;
        return super.set(i, t);
    }

    @Override
    public boolean add(T t) {
        version++;
        return super.add(t);
    }

    @Override
    public void add(int i, T t) {
        version++;
        super.add(i, t);
    }

    @Override
    public T remove(int i) {
        version++;
        return super.remove(i);
    }

    @Override
    public boolean remove(Object o) {
        version++;
        return super.remove(o);
    }

    @Override
    public void clear() {
        version++;
        super.clear();
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        version++;
        return super.addAll(collection);
    }

    @Override
    public boolean addAll(int i, Collection<? extends T> collection) {
        version++;
        return super.addAll(i, collection);
    }

    @Override
    protected void removeRange(int i, int i1) {
        version++;
        super.removeRange(i, i1);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        version++;
        return super.removeAll(collection);
    }
}
