package com.software.reuze;
import java.util.List;

public interface gb_SpatialIndex<T> {

    public void clear();

    public boolean index(T p);

    public boolean isIndexed(T item);

    public List<T> itemsWithinRadius(T p, float radius);

    public boolean reindex(T p, T q);

    public int size();

    public boolean unindex(T p);

}