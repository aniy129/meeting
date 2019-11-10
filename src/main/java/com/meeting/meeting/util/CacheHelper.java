package com.meeting.meeting.util;

import java.util.concurrent.ConcurrentHashMap;

public class CacheHelper {
    private static ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();

    public static void setData(String key, Object val) {
        map.remove(key);
        map.put(key, val);
    }

    public static Object getData(String key) {
        if (map.containsKey(key)) {
            return map.get(key);
        }
        return null;
    }
    public static void deleteData(String key){
        map.remove(key);
    }
}
