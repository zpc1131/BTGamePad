package com.example.btgamepad.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字符串转化为json
 *
 * @author chenye
 * @date 2019/01/10
 */
public class GsonJsonUtil {
    private static Gson gson = null;

    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    /**
     * 字符串转化为map
     *
     * @param string 字符串
     * @return map
     */
    public static Map stringToMap(String string) {
        Map map = new HashMap();
        if (gson != null) {
            map = gson.fromJson(string, new TypeToken<Map>() {
            }.getType());
        }
        return map;
    }

    /**
     * 字符串转类
     *
     * @param string 字符串
     * @param clazz  类
     * @param <T>    t
     * @return t
     */
    public static <T> T stringToObject(String string, Class clazz) {
        return (T) gson.fromJson(string, clazz);
    }


    /**
     * 转成list
     *
     * @param string 字符串
     * @param cls 类
     * @return list
     */
    public static <T> List<T> stringToList(String string, Class<T> cls) {
        List<T> list = null;
        if (gson != null) {
            list = gson.fromJson(string, new TypeToken<List<T>>() {
            }.getType());
        }
        return list;
    }

    /**
     * 转成json
     *
     * @param object
     * @return
     */
    public static String stringToJsonString(Object object) {
        String gsonString = null;
        if (gson != null) {
            gsonString = gson.toJson(object);
        }
        return gsonString;
    }
}
