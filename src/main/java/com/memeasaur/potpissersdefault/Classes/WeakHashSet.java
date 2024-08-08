package com.memeasaur.potpissersdefault.Classes;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.WeakHashMap;

public class WeakHashSet<E> extends AbstractSet<E> {
    private final WeakHashMap<E, Object> map;
    static final Object PRESENT = new Object();
    public WeakHashSet() {
        this.map = new WeakHashMap<>();
    }
    @Override
    public boolean add(E e) {
        return map.put(e, PRESENT) == null;
    }
    @Override
    public boolean remove(Object o) {
        return map.remove(o) == PRESENT;
    }
    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }
    @Override
    public void clear() {
        map.clear();
    }
    @Override
    public int size() {
        return map.size();
    }
    @Override
    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }
}
