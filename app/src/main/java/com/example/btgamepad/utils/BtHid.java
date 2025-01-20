package com.example.btgamepad.utils;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHidDevice;
import android.bluetooth.BluetoothHidDeviceAppQosSettings;
import android.bluetooth.BluetoothHidDeviceAppSdpSettings;
import android.bluetooth.BluetoothProfile;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.btgamepad.App;

import java.util.concurrent.Executors;

@RequiresApi(api = Build.VERSION_CODES.P)
public class BtHid {
    private final String TAG = "BtHid";
    private BluetoothHidDevice mBtHidDevice;
    private final BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothDevice mBtDevice;
    private BluetoothHidDeviceAppQosSettings mBluetoothHidDeviceAppQosSettings;

    private Vibrator vibrator;




}
