package com.example.btgamepad.utils;

import android.util.Log;

/**
 *
 */
public class Gamepad {

    /**
     * 手柄请求方法
     */
    private GamepadAction gamepadAction;

    public Gamepad(GamepadAction action) {
        this.gamepadAction = action;
    }

    /**
     * 设置请求方法
     * 
     * @param callback
     */
    public void setCallback(GamepadAction callback) {
        this.gamepadAction = callback;
    }

    /**
     * 日志
     */
    private final String TAG = "Gamepad";

    /**
     * 蓝牙手柄协议
     */
    public final byte[] gamepadDescriptor = new byte[] {
            // 游戏手柄的 HID 描述符
            0x05, 0x01, // USAGE_PAGE (Game Controls) - 游戏控制设备
            0x09, 0x05, // USAGE (Gamepad) - 游戏手柄
            (byte) 0xA1, 0x01, // COLLECTION (Application) - 应用集合
            (byte) 0xA1, 0x00, // COLLECTION (Physical) - 物理集合

            // 按钮部分
            0x05, 0x09, // USAGE_PAGE (Button) - 按钮输入
            0x19, 0x01, // USAGE_MINIMUM (Button 1)
            0x29, 0x10, // USAGE_MAXIMUM (Button 16)
            0x15, 0x00, // LOGICAL_MINIMUM (0) - 按钮的逻辑最小值
            0x25, 0x01, // LOGICAL_MAXIMUM (1) - 按钮的逻辑最大值
            0x75, 0x01, // REPORT_SIZE (1) - 每个按钮占用 1 位
            (byte) 0x95, 0x10, // REPORT_COUNT (16) - 总共有 16 个按钮
            (byte) 0x81, 0x02, // INPUT (Data, Var, Abs) - 按钮输入，数据类型、变量、绝对值

            // 左摇杆
            0x05, 0x01, // USAGE_PAGE (Generic Desktop)
            0x09, 0x30, // USAGE (X) - 左摇杆水平轴
            0x09, 0x31, // USAGE (Y) - 左摇杆垂直轴
            0x15, 0x00, // LOGICAL_MINIMUM (0) - 摇杆的逻辑最小值
            0x25, (byte) 0xFF, // LOGICAL_MAXIMUM (255) - 摇杆的逻辑最大值
            0x75, 0x08, // REPORT_SIZE (8)
            (byte) 0x95, 0x02, // REPORT_COUNT (2)
            (byte) 0x81, 0x02, // INPUT (Data, Var, Abs)

            // // 右摇杆
            0x05, 0x01, // USAGE_PAGE (Generic Desktop)
            // 0x09, 0x33, // USAGE (Rx) - 右摇杆水平轴
            // 0x09, 0x34, // USAGE (Ry) - 右摇杆垂直轴
            0x09, 0x32, // USAGE (Z) - 右摇杆水平轴
            0x09, 0x35, // USAGE (Rz) - 右摇杆垂直轴
            0x15, 0x00, // LOGICAL_MINIMUM (0) - 摇杆的逻辑最小值
            0x25, (byte) 0xFF, // LOGICAL_MAXIMUM (255) - 摇杆的逻辑最大值
            0x75, 0x08, // REPORT_SIZE (8)
            (byte) 0x95, 0x02, // REPORT_COUNT (2)
            (byte) 0x81, 0x02, // INPUT (Data, Var, Abs)

            // 方向键（HAT switch）
            0x09, 0x39, // USAGE (Hat switch)
            0x15, 0x00, // LOGICAL_MINIMUM (0)
            0x25, 0x07, // LOGICAL_MAXIMUM (7) x
            0x35, 0x00, // PHYSICAL_MINIMUM (0)
            0x46, 0x3B, 0x01, // PHYSICAL_MAXIMUM (315)
            0x65, 0x14, // UNIT (Eng Rot: Angular Pos)
            0x75, 0x04, // REPORT_SIZE (4)
            (byte) 0x95, 0x01, // REPORT_COUNT (1)
            (byte) 0x81, 0x42, // INPUT (Data, Var, Abs, Null)

            // 触发器（L2 和 R2）
            0x75, 0x04, // REPORT_SIZE (4) - 触发器占用 4 位
            (byte) 0x95, 0x01, // REPORT_COUNT (1) - 只有 1 个触发器
            (byte) 0x81, 0x01, // INPUT (Cnst, Ary, Abs) - 触发器输入，常量、数组、绝对值

            // 震动输出报告
            0x09, 0x70, // USAGE (Vibration) - 震动
            0x15, 0x00, // LOGICAL_MINIMUM (0) - 震动强度的逻辑最小值
            0x26, (byte) 0xFF, 0x00, // LOGICAL_MAXIMUM (255) - 震动强度的逻辑最大值
            0x75, 0x08, // REPORT_SIZE (8) - 每个震动强度占用 8 位
            (byte) 0x95, 0x02, // REPORT_COUNT (2) - 左、右震动马达各 1 个字节
            (byte) 0x91, 0x02, // OUTPUT (Data, Var, Abs) - 震动输出，数据类型、变量、绝对值

            // 结束集合
            (byte) 0xC0, // END_COLLECTION (Physical) - 结束物理集合
            (byte) 0xC0 // END_COLLECTION (Application) - 结束应用集合
    };

    /**
     * 按钮A
     */
    public GamepadButton BUTTON_A = new GamepadButton("BUTTON_A");

    /**
     * 按钮B
     */
    public GamepadButton BUTTON_B = new GamepadButton("BUTTON_B");

    /**
     * 按钮X
     */
    public GamepadButton BUTTON_X = new GamepadButton("BUTTON_X");

    /**
     * 按钮Y
     */
    public GamepadButton BUTTON_Y = new GamepadButton("BUTTON_Y");

    /**
     * 按钮L1
     */
    public GamepadButton BUTTON_L1 = new GamepadButton("BUTTON_L1");

    /**
     * 按钮R1
     */
    public GamepadButton BUTTON_R1 = new GamepadButton("BUTTON_R1");

    /**
     * 按钮L2
     */
    public GamepadButton BUTTON_L2 = new GamepadButton("BUTTON_L2");

    /**
     * 按钮R2
     */
    public GamepadButton BUTTON_R2 = new GamepadButton("BUTTON_R2");

    /**
     * 按钮Start
     */
    public GamepadButton BUTTON_START = new GamepadButton("BUTTON_START");

    /**
     * 按钮Select
     */
    public GamepadButton BUTTON_SELECT = new GamepadButton("BUTTON_SELECT");

    /**
     * 按钮L3
     */
    public GamepadButton BUTTON_L3 = new GamepadButton("BUTTON_L3");

    /**
     * 按钮R3
     */
    public GamepadButton BUTTON_R3 = new GamepadButton("BUTTON_R3");

    /**
     * 按钮DPad Up
     */
    public GamepadButton BUTTON_DPAD_UP = new GamepadButton("BUTTON_DPAD_UP");

    /**
     * 按钮DPad Down
     */
    public GamepadButton BUTTON_DPAD_DOWN = new GamepadButton("BUTTON_DPAD_DOWN");

    /**
     * 按钮DPad Left
     */
    public GamepadButton BUTTON_DPAD_LEFT = new GamepadButton("BUTTON_DPAD_LEFT");

    /**
     * 按钮DPad Right
     */
    public GamepadButton BUTTON_DPAD_RIGHT = new GamepadButton("BUTTON_DPAD_RIGHT");

    /**
     * 占位
     */
    public GamepadButton Placeholder = new GamepadButton("");

    /**
     * 第一位请求数据
     */

    public boolean KeyDown(GamepadButton btn) {
        Log.d(TAG, "KeyDown——old: " + btn.Name + "===<>>>" + btn.getStatus());
        btn.setStatus(true);
        Log.d(TAG, "KeyDown: " + btn.Name + "===<>>>" + btn.getStatus());
        GenerateParams();
        return true;
    }

    public boolean KeyUp(GamepadButton btn) {
        Log.d(TAG, "KeyDown——-up-old: " + btn.Name + "===<>>>" + btn.getStatus());
        btn.setStatus(false);
        Log.d(TAG, "KeyDown——-up: " + btn.Name + "===<>>>" + btn.getStatus());
        GenerateParams();
        return true;
    }

    /**
     * 左摇杆
     */
    private byte report3 = 0x00;
    private byte report4 = 0x00;
    /**
     * 右摇杆
     */
    private byte report5 = 0x00;
    private byte report6 = 0x00;

    /**
     * 设置坐标
     * 
     * @param x
     * @param y
     */
    public void SetLeftRemoteSensing(float x, float y) {
        report3 = (byte) mapToJoystick(x);
        report4 = (byte) mapToJoystick(y);
        GenerateParams();
    }

    public void SetRightRemoteSensing(float x, float y) {
        report5 = (byte) mapToJoystick(x);
        report6 = (byte) mapToJoystick(y);
        GenerateParams();
    }

    /**
     * 将数组转换为二进制字符串
     * 
     * @param boolArray
     * @return
     */
    public String booleanArrayToBinaryString(GamepadButton[] boolArray) {
        if (boolArray == null || boolArray.length != 8) {
            throw new IllegalArgumentException("数组必须是8位长度的boolean数组");
        }

        StringBuilder sb = new StringBuilder();
        for (GamepadButton b : boolArray) {
            sb.insert(0, b.getStatus() ? "1" : "0");
        }

        return sb.toString();
    }

    /**
     * 按钮1 00000000
     */
    public GamepadButton[] report1GamepadButton = { BUTTON_A, BUTTON_B, Placeholder, BUTTON_X, BUTTON_Y, Placeholder,
            BUTTON_L1, BUTTON_R1 };
    /**
     * 按钮2 00000000
     */
    public GamepadButton[] report2GamepadButton = { BUTTON_L2, BUTTON_R2, BUTTON_SELECT, BUTTON_START, Placeholder,
            BUTTON_L3, BUTTON_R3, Placeholder };

    /**
     * 生成请求字节 00000000
     * 
     * @param boolArray
     * @return
     */
    public byte GetGenerateParams(GamepadButton[] boolArray) {
        String binaryString = booleanArrayToBinaryString(boolArray);
        // 将二进制字符串转换为整数
        int intValue = Integer.parseInt(binaryString, 2); // 第二个参数是基数（2表示二进制）
        // 将整数转换为byte
        byte byteValue = (byte) intValue;
        return byteValue;
    }

    public int mapToJoystick(float value) {
        // 保证值在 [-1, 1] 范围内
        value = Math.max(-1, Math.min(1, value));
        return (int) ((value + 1) * 127.5);
    }

    /**
     * 方向键
     */
    private byte dpadState = 0x08; // 默认中立状态

    /**
     * 设置方向键的方法
     */
    public void SetDpadState() {
        if (BUTTON_DPAD_UP.getStatus()) {
            if (BUTTON_DPAD_LEFT.getStatus()) {
                dpadState = 0x07; // 左上
            } else if (BUTTON_DPAD_RIGHT.getStatus()) {
                dpadState = 0x01; // 右上
            } else {
                dpadState = 0x00; // 上
            }
        } else if (BUTTON_DPAD_DOWN.getStatus()) {
            if (BUTTON_DPAD_LEFT.getStatus()) {
                dpadState = 0x05; // 左下
            } else if (BUTTON_DPAD_RIGHT.getStatus()) {
                dpadState = 0x03; // 右下
            } else {
                dpadState = 0x04; // 下
            }
        } else if (BUTTON_DPAD_LEFT.getStatus()) {
            dpadState = 0x06; // 左
        } else if (BUTTON_DPAD_RIGHT.getStatus()) {
            dpadState = 0x02; // 右
        } else {
            dpadState = 0x08; // 中立
        }
    }

    /**
     * 将生成的请求参数通过蓝牙发送
     */
    public void GenerateParams() {
        // 设置方向键
        SetDpadState();
        // 按钮1
        byte report1 = GetGenerateParams(report1GamepadButton);
        byte report2 = GetGenerateParams(report2GamepadButton);
        byte[] report = new byte[8];
        // 设置按钮 A 按下（假设 A 是第一个按钮）
        report[0] = report1; // 按钮1 A，B,X,Y L1,R1
        report[1] = report2;// 按钮2 L2,R2,SELECT,START,L3,R3
        // 设置左摇杆为中立值
        report[2] = report3; // 左摇杆水平中立位置
        report[3] = report4; // 左摇杆垂直中立位置
        // 右遥感
        report[4] = report5; // 右摇杆水平中立位置
        report[5] = report6; // 右摇杆垂直中立位置
        // 方向键
        report[6] = dpadState; // 左右触发器未按下
        // 保留字节
        report[7] = 0x00; // 保留字段
        Log.d(TAG, "SetLeftRemoteSensing: report7" + dpadState);
        gamepadAction.SendRequest(report);
    }

}
