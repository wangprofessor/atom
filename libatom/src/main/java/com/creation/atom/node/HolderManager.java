package com.creation.atom.node;

import com.creation.atom.node.array.ArrayHolder;
import com.creation.atom.node.list.ListHolder;
import com.creation.atom.node.map.MapHolder;
import com.creation.atom.node.object.ObjectHolder;
import com.creation.atom.node.primitive.PrimitiveHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HolderManager {
    public static final IHolder sPrimitiveHolder = new PrimitiveHolder();
    private static final IHolder sObjectHolder = new ObjectHolder();
    private static final IHolder sArrayHolder = new ArrayHolder();

    private static final ArrayList<IHolderProvider> sHolderProviderList = new ArrayList<>();

    static {
        registerHolderProvider(new MapHolderProvider());
        registerHolderProvider(new ListHolderProvider());
    }

    public static boolean isClassPrimitive(Class<?> clazz) {
        return clazz.isPrimitive() ||
                String.class.isAssignableFrom(clazz) ||
                Integer.class.isAssignableFrom(clazz) ||
                Long.class.isAssignableFrom(clazz) ||
                Boolean.class.isAssignableFrom(clazz) ||
                Float.class.isAssignableFrom(clazz) ||
                Double.class.isAssignableFrom(clazz) ||
                Character.class.isAssignableFrom(clazz) ||
                Byte.class.isAssignableFrom(clazz) ||
                Short.class.isAssignableFrom(clazz);
    }

    public static void registerHolderProvider(IHolderProvider holderProvider) {
        sHolderProviderList.add(holderProvider);
    }

    public static IHolder getHolder(Class<?> clazz) {
        int size = sHolderProviderList.size();
        for (int i = size -1; i >= 0; i--) {
            IHolderProvider holderProvider = sHolderProviderList.get(i);
            if (holderProvider.isMatch(clazz)) {
                return holderProvider.getHolder();
            }

        }

        if (clazz.isArray()) {
            return sArrayHolder;
        }
        if (isClassPrimitive(clazz)) {
            return null;
        }

        return sObjectHolder;
    }

    public interface IHolderProvider {
        boolean isMatch(Class<?> clazz);
        IHolder getHolder();
    }

    private static class MapHolderProvider implements IHolderProvider {
        private static final IHolder sMapHolder = new MapHolder();

        @Override
        public boolean isMatch(Class<?> clazz) {
            return Map.class.isAssignableFrom(clazz);
        }

        @Override
        public IHolder getHolder() {
            return sMapHolder;
        }
    }

    private static class ListHolderProvider implements IHolderProvider {
        private static final IHolder sListHolder = new ListHolder();

        @Override
        public boolean isMatch(Class<?> clazz) {
            return List.class.isAssignableFrom(clazz);
        }

        @Override
        public IHolder getHolder() {
            return sListHolder;
        }
    }
}
