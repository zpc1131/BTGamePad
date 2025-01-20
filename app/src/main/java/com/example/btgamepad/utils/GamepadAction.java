package com.example.btgamepad.utils;

/**
 * 手柄请求方法
 */

@FunctionalInterface
public interface GamepadAction{
    /**
     * 发送消息回调-
     * @return
     */
    void SendRequest(byte[] params);
}
