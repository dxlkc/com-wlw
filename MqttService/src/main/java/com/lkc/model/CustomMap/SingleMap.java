package com.lkc.model.CustomMap;

import java.util.HashMap;
import java.util.Map;

public class SingleMap {
    private Map<String, String> map = new HashMap<>();
    private static SingleMap singleMap = new SingleMap();

    private SingleMap() {
    }

    public static SingleMap getInstance() {
        return singleMap;
    }

    synchronized public void put(String key, String value) {
        map.put(key, value);
    }

    synchronized public String get(String key) {
        return map.get(key);
    }

    synchronized public void delete(String key) {
        map.remove(key);
    }

}
