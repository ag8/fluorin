package com.electronneutrino.Fluorin;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

public class VActivity extends Activity {
    private OutputStream outputStream;
    private InputStream inStream;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() throws IOException {
        BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();
        if (blueAdapter != null) {
            if (blueAdapter.isEnabled()) {
                Set<BluetoothDevice> bondedDevices = blueAdapter.getBondedDevices();

                if (bondedDevices.size() > 0) {
                    Object[] devices = bondedDevices.toArray();
                    BluetoothDevice device = getDevice("HC-06", devices); // Get HC-06 from the list of devices
                    if (device == null) {
                        toast("Could not find HC-06 in list of Bluetooth devices.");
                        System.exit(-1);
                    }
                    System.out.println("Raise a glass to freedom! " + device.getName());
                    ParcelUuid[] uuids = device.getUuids();
                    BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
                    socket.connect();
                    outputStream = socket.getOutputStream();
                    inStream = socket.getInputStream();
                }

                Log.e("error", "No appropriate paired devices.");
            } else {
                Log.e("error", "Bluetooth is disabled.");
            }
        }
    }

    @Nullable
    private BluetoothDevice getDevice(String s, Object[] devices) {
        for (Object d : devices) {
            if (((BluetoothDevice) d).getName().equals(s)) {
                return (BluetoothDevice) d;
            }
        }

        return null;
    }

    public void write(String s) throws IOException {
        outputStream.write(s.getBytes());
    }

    public boolean write(int a) throws IOException {
        if (a < -128 || a > 127) {
            return false;
        } else {
            outputStream.write((byte) a);
            return true;
        }
    }

    private void toast(String s) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, s, duration);
        toast.show();
    }

    public void sendZero(View view) throws IOException {
        write(0);
    }

    public void sendOne(View view) throws IOException {
        write(1);
    }

    public void sendTwo(View view) throws IOException, InterruptedException {
        write(2);
    }
}
