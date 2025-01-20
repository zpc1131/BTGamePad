package com.example.btgamepad;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHidDevice;
import android.bluetooth.BluetoothHidDeviceAppQosSettings;
import android.bluetooth.BluetoothHidDeviceAppSdpSettings;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.btgamepad.customize.JoystickView2;
import com.example.btgamepad.customize.MoveButton;
import com.example.btgamepad.utils.AppConfigFileImpl;
import com.example.btgamepad.utils.Gamepad;
import com.example.btgamepad.utils.GamepadAction;
import com.example.btgamepad.utils.GamepadButton;
import com.example.btgamepad.utils.Globe;
import com.example.btgamepad.utils.GsonJsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity implements GamepadAction {

    private final String TAG = "MainActivity";
    private int SCREEN_WIDTH = 1920;
    private int SCREEN_HEIGHT = 1080;

    private boolean isShock = false;// 震动
    private FrameLayout visualAngle;
    private MoveButton yaogan_left, direction_key, yaogan_right, Move_a, Move_b, Move_x, Move_y, Move_rs, Move_rt,
            Move_rb, Move_lt, Move_lb, Move_ls, Move_back, Move_start;
    ImageView dpad_up, dpad_right,dpad_left,dpad_down;
    Map CustomButtonMap = new HashMap<>();

    private float downJoystickX = 0, downJoystickY = 0, moveJoystickX, moveJoystickY;


    private BluetoothHidDevice mBtHidDevice;
    private final BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothDevice mBtDevice;
    private BluetoothHidDeviceAppQosSettings mBluetoothHidDeviceAppQosSettings;
    private static final int REQUEST_ENABLE_BT = 99;
    private Vibrator vibrator;

    private final ArrayList<BluetoothDevice> mDevices = new ArrayList<>();

    public Gamepad gamepad=new Gamepad(this);
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        hideBottomUIMenu();
        initUI();
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onResume() {
        super.onResume();

        // Get bluetooth enabled before continuing
        if (!mBtAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            btListDevices();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @SuppressLint("MissingPermission")
    private void btListDevices() {
        getProxy(); // need bluetooth to have been enabled first

        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

        // Add devices to adapter
        List<String> names = new ArrayList<>();

        // add empty
        names.add("(disconnected)");
        mDevices.add(null);

        for (BluetoothDevice btDev : pairedDevices) {
            names.add(btDev.getName());
            mDevices.add(btDev);
            Log.d(TAG, "btListDevices: btDev==" + btDev.getName());
            Log.d(TAG, "btListDevices: btDev==" + btDev.getAddress());
            Log.d(TAG, "btListDevices: btDev,getUuids==" + btDev.getUuids());

        }

        Spinner btList = findViewById(R.id.devices);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        btList.setAdapter(adapter);

        btList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                BluetoothDevice dev = mDevices.get(position);
                Log.d(TAG, "onItemSelected(): " + dev + " " + position + " " + id);
                btConnect(dev);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO handle this
            }
        });
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.P)
    private void btConnect(BluetoothDevice device) {
        Log.i(TAG, "btConnect: device=" + device);
        Log.i(TAG, "btConnect: mBtHidDevice=" + mBtHidDevice);
        // disconnect from everything else
        if (mBtHidDevice != null) {
            for (BluetoothDevice btDev : mBtHidDevice.getDevicesMatchingConnectionStates(new int[] {
                    // BluetoothProfile.STATE_CONNECTING,
                    BluetoothProfile.STATE_CONNECTED,
            })) {
                mBtHidDevice.disconnect(btDev);
                Log.i(TAG, "btConnect: disconnect");
            }
        }

        if (device != null) {
            mBtDevice = device;
            mBtHidDevice.connect(device);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onPause() {
        super.onPause();

        if (mBtHidDevice != null) {
            btConnect(null); // disconnect
            Spinner btList = findViewById(R.id.devices);
            btList.setSelection(0);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void getProxy() {
        mBtAdapter.getProfileProxy(App.getInstance(), new BluetoothProfile.ServiceListener() {
            @Override
            @SuppressLint({ "NewApi", "MissingPermission" })
            public void onServiceConnected(int profile, BluetoothProfile proxy) {
                if (profile == BluetoothProfile.HID_DEVICE) {
                    Log.d(TAG, "Got HID device");
                    mBtHidDevice = (BluetoothHidDevice) proxy;
                    mBluetoothHidDeviceAppQosSettings = new BluetoothHidDeviceAppQosSettings(1, 800, 9, 0, 11250, -1);
                    BluetoothHidDeviceAppSdpSettings sdp = new BluetoothHidDeviceAppSdpSettings(
                            "虚拟蓝牙手柄",
                            "Android BLE HID Keyboard",
                            "Android",
                            (byte) 0x00,
                           gamepad.gamepadDescriptor);
                    mBtHidDevice.registerApp(sdp, null, mBluetoothHidDeviceAppQosSettings,
                            Executors.newSingleThreadExecutor(), new BluetoothHidDevice.Callback() {
                                @Override
                                public void onGetReport(BluetoothDevice device, byte type, byte id, int bufferSize) {
                                    Log.v(TAG, "onGetReport: device=" + device + " type=" + type
                                            + " id=" + id + " bufferSize=" + bufferSize);
                                }
                                @Override
                                public void onSetReport(BluetoothDevice device, byte type, byte id, byte[] data) {
                                    Log.v(TAG, "onSetReport: device=" + device + " type=" + type
                                            + " id=" + id + " data=" + data.length);
                                    // 解析输出报告数据
                                    if (data.length >= 2) {
                                        int leftMotorStrength = data[0] & 0xFF;
                                        int rightMotorStrength = data[1] & 0xFF;
                                        handleVibration(leftMotorStrength, rightMotorStrength);
                                    }
                                }

                                @Override
                                public void onConnectionStateChanged(BluetoothDevice device, final int state) {
                                    Log.v(TAG, "onConnectionStateChanged: device=" + device + " state=" + state);
                                    if (device.equals(mBtDevice)) {
                                        Log.d(TAG, "onConnectionStateChanged: state==" + state);
                                        // 0 断开的
                                        // 1 连接中
                                        // 2 已连接
                                        // 3 正在断开连接…
                                    }
                                    if(state==2){
                                        Log.d(TAG, "onConnectionStateChanged:32132132132132===》 重新new对象");
//                                        gamepad.setCallback((b)->{
//                                            Log.d(TAG, "onConnectionStateChanged:32132132132132===》 "+b[0]);
//                                        });
                                    }
                                }
                            });
                }

            }

            @Override
            public void onServiceDisconnected(int profile) {
                if (profile == BluetoothProfile.HID_DEVICE) {
                    Log.d(TAG, "Lost HID device");
                }
            }
        }, BluetoothProfile.HID_DEVICE);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initUI() {
        // 判断屏幕大小
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);// display =
                                                              // getWindowManager().getDefaultDisplay();display.getMetrics(dm)（把屏幕尺寸信息赋值给DisplayMetrics
                                                              // dm）;
        if (dm.widthPixels > dm.heightPixels) {
            SCREEN_WIDTH = dm.widthPixels/*- getNavigationHeight(PlayGameActivity.this)*/;
            SCREEN_HEIGHT = dm.heightPixels;
        } else {
            SCREEN_WIDTH = dm.heightPixels/*- getNavigationHeight(PlayGameActivity.this)*/;
            SCREEN_HEIGHT = dm.widthPixels;
        }
        Globe.landscapeScaleWidht = ((float) SCREEN_WIDTH / Globe.landscapeSW);
        Globe.landscapeScaleHeight = ((float) SCREEN_HEIGHT / Globe.landscapeSH);

        visualAngle = findViewById(R.id.visual_angle);

        String CustomButton = AppConfigFileImpl.getStringParams(getApplicationContext(), "CustomButton");
        if (!TextUtils.isEmpty(CustomButton)) {
            Log.d("TAG", "CustomButton   " + CustomButton);
            CustomButtonMap = GsonJsonUtil.stringToMap(CustomButton);
        } else {
            initDefault();// 默认配置
        }
        yaogan_left = findViewById(R.id.yaogan_left);
        direction_key = findViewById(R.id.direction_key);
        yaogan_right = findViewById(R.id.yaogan_right);
        Move_a = findViewById(R.id.Move_a);
        Move_b = findViewById(R.id.Move_b);
        Move_x = findViewById(R.id.Move_x);
        Move_y = findViewById(R.id.Move_y);
        Move_rs = findViewById(R.id.Move_rs);
        Move_rt = findViewById(R.id.Move_rt);
        Move_rb = findViewById(R.id.Move_rb);
        Move_lt = findViewById(R.id.Move_lt);
        Move_lb = findViewById(R.id.Move_lb);
        Move_ls = findViewById(R.id.Move_ls);
        Move_back = findViewById(R.id.Move_back);
        Move_start = findViewById(R.id.Move_start);
        initButtonPosition();
        dpad_up = findViewById(R.id.dpad_up);
        dpad_left = findViewById(R.id.dpad_left);
        dpad_down = findViewById(R.id.dpad_down);
        dpad_right = findViewById(R.id.dpad_right);
        dpad_up.setClickable(true);
        dpad_left.setClickable(true);
        dpad_down.setClickable(true);
        Move_a.setClickable(true);
        dpad_right.setClickable(true);

        JoystickView2 joystickLeft = findViewById(R.id.joystick_left);
        JoystickView2 joystickRight = findViewById(R.id.joystick_right);
        joystickLeft.setOnJoystickListener(new JoystickView2.OnJoystickListener() {
            @SuppressLint("MissingPermission")
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onPosition(float x, float y) {
                // Config.keyEvent_time = 0;
                Log.w("TAG", "stick x=" + x + "  y=" + y);
                gamepad.SetLeftRemoteSensing(x,y);
                // System.out.println("player1 setXY x="+x+"y="+y);
            }

            @Override
            public void onVibrator() {
                // joystickLeft.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS,
                // HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
                if (isShock) {
                    joystickLeft.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS,
                            HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
                }
            }
        });
        joystickRight.setOnJoystickListener(new JoystickView2.OnJoystickListener() {
            @SuppressLint("MissingPermission")
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onPosition(float x, float y) {
                // Config.keyEvent_time = 0;
                Log.w("TAG", "stick x=" + x + "  y=" + y);
                gamepad.SetRightRemoteSensing(x,y);

            }

            @Override
            public void onVibrator() {
                // joystickLeft.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS,
                // HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
                if (isShock) {
                    joystickLeft.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS,
                            HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
                }
            }
        });

        setBtnKey(Move_a,gamepad.BUTTON_A);
        setBtnKey(Move_b,gamepad.BUTTON_B);
        setBtnKey(Move_x,gamepad.BUTTON_X);
        setBtnKey(Move_y,gamepad.BUTTON_Y);
        setBtnKey(Move_lb,gamepad.BUTTON_L1);
        setBtnKey(Move_rb,gamepad.BUTTON_R1);
        setBtnKey(Move_lt,gamepad.BUTTON_L2);
        setBtnKey(Move_rt,gamepad.BUTTON_R2);
        setBtnKey(Move_start,gamepad.BUTTON_START);
        setBtnKey(Move_back,gamepad.BUTTON_SELECT);
        setBtnKey(Move_ls,gamepad.BUTTON_L3);
        setBtnKey(Move_rs,gamepad.BUTTON_R3);
        setBtnKey(dpad_up,gamepad.BUTTON_DPAD_UP);
        setBtnKey(dpad_right,gamepad.BUTTON_DPAD_RIGHT);
        setBtnKey(dpad_down,gamepad.BUTTON_DPAD_DOWN);
        setBtnKey(dpad_left,gamepad.BUTTON_DPAD_LEFT);
    }
    public void  setBtnKey(MoveButton btn, GamepadButton gamepadButton){
        btn.setOnTouchListener(createButtonTouchListener(gamepadButton));
    }
    public void  setBtnKey(ImageView btn, GamepadButton gamepadButton){
        btn.setOnTouchListener(createButtonTouchListener(gamepadButton));
    }
 
    public void initDefault() {
        try {
            CustomButtonMap.put("yaogan_left",
                    106 * Globe.landscapeScaleWidht + "@" + 653.5 * Globe.landscapeScaleHeight);
            CustomButtonMap.put("direction_key",
                    565 * Globe.landscapeScaleWidht + "@" + 730.0 * Globe.landscapeScaleHeight);
            CustomButtonMap.put("yaogan_right",
                    1248 * Globe.landscapeScaleWidht + "@" + 770 * Globe.landscapeScaleHeight);
            CustomButtonMap.put("Move_a", 1645 * Globe.landscapeScaleWidht + "@" + 799 * Globe.landscapeScaleHeight);
            CustomButtonMap.put("Move_b", 1774 * Globe.landscapeScaleWidht + "@" + 680 * Globe.landscapeScaleHeight);
            CustomButtonMap.put("Move_x", 1521 * Globe.landscapeScaleWidht + "@" + 680 * Globe.landscapeScaleHeight);
            CustomButtonMap.put("Move_y", 1645 * Globe.landscapeScaleWidht + "@" + 562 * Globe.landscapeScaleHeight);
            CustomButtonMap.put("Move_rs", 1547 * Globe.landscapeScaleWidht + "@" + 300 * Globe.landscapeScaleHeight);
            CustomButtonMap.put("Move_rt", 1698 * Globe.landscapeScaleWidht + "@" + 343 * Globe.landscapeScaleHeight);
            CustomButtonMap.put("Move_rb", 1452 * Globe.landscapeScaleWidht + "@" + 453 * Globe.landscapeScaleHeight);
            CustomButtonMap.put("Move_ls", 134 * Globe.landscapeScaleWidht + "@" + 303 * Globe.landscapeScaleHeight);
            CustomButtonMap.put("Move_lt", 44 * Globe.landscapeScaleWidht + "@" + 480 * Globe.landscapeScaleHeight);
            CustomButtonMap.put("Move_lb", 261 * Globe.landscapeScaleWidht + "@" + 404 * Globe.landscapeScaleHeight);
            CustomButtonMap.put("Move_back", 1774 * Globe.landscapeScaleWidht + "@" + 30 * Globe.landscapeScaleHeight);
            CustomButtonMap.put("Move_start",
                    1774 * Globe.landscapeScaleWidht + "@" + 149 * Globe.landscapeScaleHeight);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 调用前必须先初始化按钮
    public void initButtonPosition() {
        try {
            if (CustomButtonMap.get("yaogan_left") != null
                    && !TextUtils.isEmpty(CustomButtonMap.get("yaogan_left").toString())) {
                String[] split = CustomButtonMap.get("yaogan_left").toString().split("@");
                yaogan_left.setPosition(Float.parseFloat(split[0]), Float.parseFloat(split[1]));
            }
            if (CustomButtonMap.get("direction_key") != null
                    && !TextUtils.isEmpty(CustomButtonMap.get("direction_key").toString())) {
                String[] split = CustomButtonMap.get("direction_key").toString().split("@");
                direction_key.setPosition(Float.parseFloat(split[0]), Float.parseFloat(split[1]));
            }
            if (CustomButtonMap.get("yaogan_right") != null
                    && !TextUtils.isEmpty(CustomButtonMap.get("yaogan_right").toString())) {
                String[] split = CustomButtonMap.get("yaogan_right").toString().split("@");
                yaogan_right.setPosition(Float.parseFloat(split[0]), Float.parseFloat(split[1]));
            }
            if (CustomButtonMap.get("Move_a") != null && !TextUtils.isEmpty(CustomButtonMap.get("Move_a").toString())) {
                String[] split = CustomButtonMap.get("Move_a").toString().split("@");
                // Log.d(TAG, "initButtonPosition: aaaaaaaaaaaaaaaaaaaaaaaa");
                Move_a.setPosition(Float.parseFloat(split[0]), Float.parseFloat(split[1]));
            }
            if (CustomButtonMap.get("Move_b") != null && !TextUtils.isEmpty(CustomButtonMap.get("Move_b").toString())) {
                String[] split = CustomButtonMap.get("Move_b").toString().split("@");
                Move_b.setPosition(Float.parseFloat(split[0]), Float.parseFloat(split[1]));
            }
            if (CustomButtonMap.get("Move_x") != null && !TextUtils.isEmpty(CustomButtonMap.get("Move_x").toString())) {
                String[] split = CustomButtonMap.get("Move_x").toString().split("@");
                Move_x.setPosition(Float.parseFloat(split[0]), Float.parseFloat(split[1]));
            }
            if (CustomButtonMap.get("Move_y") != null && !TextUtils.isEmpty(CustomButtonMap.get("Move_y").toString())) {
                String[] split = CustomButtonMap.get("Move_y").toString().split("@");
                Move_y.setPosition(Float.parseFloat(split[0]), Float.parseFloat(split[1]));
            }
            if (CustomButtonMap.get("Move_rs") != null
                    && !TextUtils.isEmpty(CustomButtonMap.get("Move_rs").toString())) {
                String[] split = CustomButtonMap.get("Move_rs").toString().split("@");
                Move_rs.setPosition(Float.parseFloat(split[0]), Float.parseFloat(split[1]));
            }
            if (CustomButtonMap.get("Move_rt") != null
                    && !TextUtils.isEmpty(CustomButtonMap.get("Move_rt").toString())) {
                String[] split = CustomButtonMap.get("Move_rt").toString().split("@");
                Move_rt.setPosition(Float.parseFloat(split[0]), Float.parseFloat(split[1]));
            }
            if (CustomButtonMap.get("Move_rb") != null
                    && !TextUtils.isEmpty(CustomButtonMap.get("Move_rb").toString())) {
                String[] split = CustomButtonMap.get("Move_rb").toString().split("@");
                Move_rb.setPosition(Float.parseFloat(split[0]), Float.parseFloat(split[1]));
            }
            if (CustomButtonMap.get("Move_lt") != null
                    && !TextUtils.isEmpty(CustomButtonMap.get("Move_lt").toString())) {
                String[] split = CustomButtonMap.get("Move_lt").toString().split("@");
                Move_lt.setPosition(Float.parseFloat(split[0]), Float.parseFloat(split[1]));
            }
            if (CustomButtonMap.get("Move_lb") != null
                    && !TextUtils.isEmpty(CustomButtonMap.get("Move_lb").toString())) {
                String[] split = CustomButtonMap.get("Move_lb").toString().split("@");
                Move_lb.setPosition(Float.parseFloat(split[0]), Float.parseFloat(split[1]));
            }
            if (CustomButtonMap.get("Move_ls") != null
                    && !TextUtils.isEmpty(CustomButtonMap.get("Move_ls").toString())) {
                String[] split = CustomButtonMap.get("Move_ls").toString().split("@");
                Move_ls.setPosition(Float.parseFloat(split[0]), Float.parseFloat(split[1]));
            }
            if (CustomButtonMap.get("Move_back") != null
                    && !TextUtils.isEmpty(CustomButtonMap.get("Move_back").toString())) {
                String[] split = CustomButtonMap.get("Move_back").toString().split("@");
                Move_back.setPosition(Float.parseFloat(split[0]), Float.parseFloat(split[1]));
            }
            if (CustomButtonMap.get("Move_start") != null
                    && !TextUtils.isEmpty(CustomButtonMap.get("Move_start").toString())) {
                String[] split = CustomButtonMap.get("Move_start").toString().split("@");
                Move_start.setPosition(Float.parseFloat(split[0]), Float.parseFloat(split[1]));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    protected void hideBottomUIMenu() {
        // 隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT <= 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT > 19) {
            // for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);

        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if ((event.getSource() & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD) {
            String keyName = "";
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BUTTON_X:
                    keyName = "BUTTON_X";
                    break;
                case KeyEvent.KEYCODE_BUTTON_Y:
                    keyName = "BUTTON_Y";
                    break;
                case KeyEvent.KEYCODE_BUTTON_A:
                    keyName = "BUTTON_A";
                    break;
                case KeyEvent.KEYCODE_BUTTON_B:
                    keyName = "BUTTON_B";
                    break;
                case KeyEvent.KEYCODE_BUTTON_L1:
                    keyName = "BUTTON_L1";
                    break;
                case KeyEvent.KEYCODE_BUTTON_R1:
                    keyName = "BUTTON_R1";
                    break;
                case KeyEvent.KEYCODE_BUTTON_L2:
                    keyName = "BUTTON_L2";
                    break;
                case KeyEvent.KEYCODE_BUTTON_R2:
                    keyName = "BUTTON_R2";
                    break;
                case KeyEvent.KEYCODE_BUTTON_START:
                    keyName = "BUTTON_START";
                    break;
                case KeyEvent.KEYCODE_BUTTON_SELECT:
                    keyName = "BUTTON_SELECT";
                    break;
                default:
                    Log.d(TAG, "dispatchKeyEvent-other-KeyCode:" + event.getKeyCode());
                    break;
            }
            Log.d(TAG, "key code:" + event.getKeyCode() + ";keyName:"+keyName+";action:" + event.getAction());
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    private View.OnTouchListener createButtonTouchListener(GamepadButton gamepadButton) {
        return new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "Button event: " + event.getAction()+",key=");
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        gamepad.KeyDown(gamepadButton);
                        break;
                    case MotionEvent.ACTION_UP:
                        gamepad.KeyUp(gamepadButton);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    default:
                        break;
                }
                return true;
            }
        };
    }


    /**
     * 发送手柄数据
     * @param params
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    @SuppressLint("MissingPermission")
    @Override
    public void SendRequest(byte[] params) {
        Log.d(TAG, "clickKey: =====>>>>"+mBtHidDevice.getConnectedDevices().size());
        for (BluetoothDevice btDev : mBtHidDevice.getConnectedDevices()) {
            // 发送 HID 报文
            mBtHidDevice.sendReport(btDev, 1, params);
        }
    }

    /**
     *
     * @param leftMotor
     * @param rightMotor
     */
    private void handleVibration(int leftMotor, int rightMotor) {
        Log.d(TAG, "Vibration - 左边震动: " + leftMotor + ", 右边震动: " + rightMotor);
        // 如果有硬件支持，在这里控制震动马达
        if (vibrator != null) {
            // 震动逻辑示例：根据震动强度控制震动时间
            long vibrationDuration = Math.max(leftMotor, rightMotor);
            if (vibrationDuration > 0) {
                vibrator.vibrate(vibrationDuration);
            }
        }
    }
}