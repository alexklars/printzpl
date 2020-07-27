package com.alex.print.printer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;

import androidx.preference.PreferenceManager;

import com.alex.print.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import static android.text.TextUtils.isEmpty;

public class BluetoothUtil {

    public static void turnOnBluetooth(Context context) {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        context.startActivity(intent);
    }

    public static LinkedHashMap<String, BluetoothDevice> getBluetoothDeviceMap(BluetoothAdapter adapter) {
        LinkedHashMap<String, BluetoothDevice> devices = new LinkedHashMap<>();
        if (adapter != null) {
            Set<BluetoothDevice> bondedDevices = adapter.getBondedDevices();
            if (bondedDevices != null)
                for (BluetoothDevice device : bondedDevices)
                    devices.put(device.getName() == null || device.getName().length() == 0
                                    ? device.getAddress()
                                    : device.getName(),
                            device);
        }
        return devices;
    }

    private static List<BluetoothDevice> getBluetoothDeviceList(BluetoothAdapter adapter) {
        List<BluetoothDevice> deviceList = new ArrayList<>();
        if (adapter != null) {
            Set<BluetoothDevice> bondedDevices = adapter.getBondedDevices();
            if (bondedDevices != null)
                deviceList.addAll(bondedDevices);
        }
        return deviceList;
    }


    public static String getLastConnectedBluetoothDeviceAddress(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.last_connected_bluetooth_device_address), "");
    }

    public static void setLastConnectedBluetoothDeviceAddress(Context context, String address) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(context.getString(R.string.last_connected_bluetooth_device_address), address)
                .apply();
    }

    public static BluetoothDevice findLastConnectedBluetoothDevice(Context context, BluetoothAdapter bluetoothAdapter) {
        List<BluetoothDevice> deviceList = BluetoothUtil.getBluetoothDeviceList(bluetoothAdapter);
        String address = getLastConnectedBluetoothDeviceAddress(context);

        for (BluetoothDevice device : deviceList) {
            if (!isEmpty(device.getAddress()) && device.getAddress().equals(address)) {
                return device;
            }
        }

        return null;
    }
}
