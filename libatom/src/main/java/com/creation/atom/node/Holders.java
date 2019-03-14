package com.creation.atom.node;

import java.util.HashMap;

public class Holders {
    private final HashMap<Class<?>, IHolder> holderMap = new HashMap<>();

    public IHolder getHolder(Class<?> clazz) {
        if (holderMap.containsKey(clazz)) {
            return holderMap.get(clazz);
        }

        IHolder holder = HolderManager.getHolder(clazz);
        holderMap.put(clazz, holder);

        return holder;
    }
}
