package com.example.btgamepad.utils;

import android.content.Context;

import java.util.Map;

/**
 * AppConfigFile操作类
 */
public class AppConfigFileImpl {
    /**
     * 保存String类型的信息
     *
     * @param context
     */
    public static void saveParams(Context context, String key, String value) {
        AppConfigFile.getInstance(context).saveParams(key, value);
    }

    /**
     * 获取String类型的信息
     *
     * @param context
     */
    public static String getStringParams(Context context, String key) {
        return AppConfigFile.getInstance(context).getStrParams(key);
    }

    /**
     * 保存int类型的信息
     *
     * @param context
     */
    public static void saveParams(Context context, String key, int value) {
        AppConfigFile.getInstance(context).saveParams(key, value);
    }

    /**
     * 获取int类型的信息
     *
     * @param context
     */
    public static int getIntParams(Context context, String key) {
        return AppConfigFile.getInstance(context).getIntParams(key);
    }

    /**
     * 保存boolean类型的信息
     *
     * @param context
     */
    public static void saveParams(Context context, String key, boolean value) {
        AppConfigFile.getInstance(context).saveParams(key, value);
    }

    /**
     * 获取boolean类型的信息
     *
     * @param context
     */
    public static boolean getBooleanParams(Context context, String key) {
        return AppConfigFile.getInstance(context).getBooleanParams(key);
    }

    /**
     * 获取boolean类型的信息
     *
     * @param context
     */
    public static boolean getBooleanParams(Context context, String key, boolean defValue) {
        return AppConfigFile.getInstance(context).getBooleanParams(key, defValue);
    }

    /**
     * 保存long类型的信息
     *
     * @param context
     */
    public static void saveParams(Context context, String key, long value) {
        AppConfigFile.getInstance(context).saveParams(key, value);
    }

    /**
     * 获取long类型的信息
     *
     * @param context
     */
    public static long getLongParams(Context context, String key) {
        return AppConfigFile.getInstance(context).getLongParams(key);
    }


    /**
     * @param @return
     * @return Map<String,?>
     * @description 获取所有的配置信息
     */
    public static Map<String, ?> getAll(Context context) {
        return AppConfigFile.getInstance(context).getAll();
    }


}
