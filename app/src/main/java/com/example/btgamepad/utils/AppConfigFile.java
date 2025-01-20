package com.example.btgamepad.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Map;

/**
 * APP中BaseConfigFile保存数据操作类 保证整个应用只有一个SharedPreferences
 */
public class AppConfigFile {
    public static final String SHARE_XML_NAME = "app_config"; //保存xml名称
    private static SharedPreferences sharedPreferences;
    private static Editor sharedEdit;

    private AppConfigFile() {
    }

    private static AppConfigFile baseSharedPreferences;

    public static AppConfigFile getInstance(Context context) {
        if (null == baseSharedPreferences) {
            baseSharedPreferences = new AppConfigFile();
        }
        if (null == sharedPreferences) {
            sharedPreferences = context.getSharedPreferences(SHARE_XML_NAME, Context.MODE_PRIVATE);
            sharedEdit = sharedPreferences.edit();
        }
        return baseSharedPreferences;
    }

    //保存数据
    public void saveParams(String key, int value) {
        sharedEdit.putInt(key, value);
        sharedEdit.commit();
    }


    public void saveParams(String key, String value) {
        sharedEdit.putString(key, value);
        sharedEdit.commit();
    }


    public void saveParams(String key, boolean value) {
        sharedEdit.putBoolean(key, value);
        sharedEdit.commit();
    }


    public void saveParams(String key, long value) {
        sharedEdit.putLong(key, value);
        sharedEdit.commit();
    }


    /**
     * @param @return
     * @return Map<String,?>
     * @description 获取所有的配置信息
     * @author jiaBF
     */
    public Map<String, ?> getAll() {
        return sharedPreferences.getAll();
    }

    //读取保存数据
    public String getStrParams(String key) {
        return sharedPreferences.getString(key, "");
    }


    public String getStrParams(String key, String defaultVal) {
        return sharedPreferences.getString(key, defaultVal);
    }


    public int getIntParams(String key) {
        return sharedPreferences.getInt(key, 0);
    }


    public int getIntParams(String key, int defaultVal) {
        return sharedPreferences.getInt(key, defaultVal);
    }


    public float getFloatParams(String key, float defaultVal) {
        return sharedPreferences.getFloat(key, defaultVal);
    }


    public boolean getBooleanParams(String key) {
        return sharedPreferences.getBoolean(key, false);
    }


    public boolean getBooleanParams(String key, boolean defaultVal) {
        return sharedPreferences.getBoolean(key, defaultVal);
    }


    public long getLongParams(String key) {
        return sharedPreferences.getLong(key, 0);
    }


    public long getLongParams(String key, long defaultVal) {
        return sharedPreferences.getLong(key, defaultVal);
    }


}
