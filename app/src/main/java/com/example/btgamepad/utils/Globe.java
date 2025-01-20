package com.example.btgamepad.utils;


public class Globe {


    public static String version = "";


    public static float scaleWidht = 0;
    public static float scaleHeight = 0;

    public static float landscapeScaleWidht = 0;
    public static float landscapeScaleHeight = 0;

    public final static int KEY_UP = 19;
    public final static int KEY_DOWN = 20;
    public final static int KEY_LEFT = 21;
    public final static int KEY_RIGHT = 22;
    public final static int KEY_OK = 66;
    public final static int KEY_OK1 = 23;
    public final static int KEY_OK2 = 188;


    public static final int HCENTER = 1;
    public static final int VCENTER = 2;
    public static final int LEFT = 4;
    public static final int RIGHT = 8;
    public static final int TOP = 16;
    public static final int BOTTOM = 32;
    public static final int BASELINE = 64;
    public static final int SOLID = 0;
    public static final int DOTTED = 1;

    public final static int KEY_CAIDAN = 82;
    public final static int KEY_CAIDAN1 = 189;
    public final static int KEY_SOFT_R = 4;// right soft key
    public final static int KEY_SOFT_R1 = 199;// right soft key

    public static final int ANCHOR_T_L = TOP | LEFT;
    public static final int ANCHOR_T_R = TOP | RIGHT;
    public static final int ANCHOR_B_L = BOTTOM | LEFT;
    public static final int ANCHOR_B_R = BOTTOM | RIGHT;
    public static final int ANCHOR_H_V = HCENTER | VCENTER;
    public static final int ANCHOR_L_V = LEFT | VCENTER;
    public static final int ANCHOR_R_V = RIGHT | VCENTER;
    public static final int ANCHOR_T_H = TOP | HCENTER;
    public static final int ANCHOR_B_H = BOTTOM | HCENTER;


    public final static int KEY_0 = 7;
    public final static int KEY_1 = 8;
    public final static int KEY_2 = 9;
    public final static int KEY_3 = 10;
    public final static int KEY_4 = 11;
    public final static int KEY_5 = 12;
    public final static int KEY_6 = 13;
    public final static int KEY_7 = 14;
    public final static int KEY_8 = 15;
    public final static int KEY_9 = 16;

    /********************************************************/
    /*************** ��ת���� *****************/
    /********************************************************/
    public static final int TRANS_MIRROR = 2;
    public static final int TRANS_MIRROR_ROT180 = 1;
    public static final int TRANS_MIRROR_ROT270 = 4;
    public static final int TRANS_MIRROR_ROT90 = 7;
    public static final int TRANS_NONE = 0;
    public static final int TRANS_ROT180 = 3;
    public static final int TRANS_ROT270 = 6;
    public static final int TRANS_ROT90 = 5;

    public static int SLEEP_TIME = 30;
    public static boolean isExit = false;
    public static boolean isPause = false;


    public static int SW = 1080;
    public static int SH = 1920;

    public static int factSW = 0;
    public static int factSH = 0;
    //真实全部分辨率
    public static int practicalFactSW = 0;
    public static int practicalFactSH = 0;
    public static int landscapepracticalFactSW = 0;
    public static int landscapepracticalFactSH = 0;
    public static int landscapeSW = 1920;
    public static int landscapeSH = 1080;
    public static int landscapeFactSW = 0;
    public static int landscapeFactSH = 0;//横屏分辨率
    public static boolean isZoom = false;


}
