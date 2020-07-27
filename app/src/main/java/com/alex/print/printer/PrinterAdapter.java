package com.alex.print.printer;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;

public class PrinterAdapter {

    private static PrinterAdapter instance;
    private OutputStream outputStream;
    private BluetoothDevice bluetoothDevice;


    public static PrinterAdapter getInstance() {
        if (instance == null) {
            instance = new PrinterAdapter();
        }
        return instance;
    }

    public boolean connect(BluetoothDevice bluetoothDevice) {
        try {
            Method m = bluetoothDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
            BluetoothSocket socket = (BluetoothSocket) m.invoke(bluetoothDevice, 1);
            socket.connect();
            outputStream = socket.getOutputStream();
            this.bluetoothDevice = bluetoothDevice;
            return isConnected();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void disconnect() {
        if (outputStream != null) {
            try {
                outputStream.close();
                outputStream = null;
                bluetoothDevice = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isConnected() {
        return outputStream != null;
    }

    public String getName() {
        return bluetoothDevice.getName();
    }

    public String getAddress() {
        return bluetoothDevice.getAddress();
    }

    public boolean printZPL(String zpl) {
        try {
            outputStream.write(zpl.getBytes());
            return true;
        } catch (IOException e) {
            disconnect();
            return false;
        }
    }
}
