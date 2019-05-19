package com.suthar.jay.ma_001_beta;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Set;

public class MainActivity extends AppCompatActivity {

    Button btTurnOn, btScan, btnConnect;
    TextView btlist;
    BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
    String deviceHardwareAddress;
    String deviceName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btTurnOn = findViewById(R.id.button);
        btScan = findViewById(R.id.button2);
        btlist = findViewById(R.id.btList);
        btnConnect = findViewById(R.id.button3);
        btlist.setText("");

        btTurnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btAdapter == null) {
                    //Does not support Bluetooth
                    Toast.makeText(getApplicationContext(), "Bt Not Supported", Toast.LENGTH_SHORT);
                } else {
                    //Magic starts. Let's check if it's enabled
                    if (!btAdapter.isEnabled()) {
                        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivity(enableIntent);
                    }
                }
            }
        });

        btScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btAdapter == null) {
                    Toast.makeText(getApplicationContext(), "Bt Not Supported", Toast.LENGTH_SHORT);
                }
                btAdapter.startDiscovery();
                Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
                if (pairedDevices.size() > 0) {
                    int i = pairedDevices.size();
                    for (BluetoothDevice device : pairedDevices) {

                        deviceName = device.getName();

                        deviceHardwareAddress = device.getAddress();
                        btlist.append(deviceName + " : " + deviceHardwareAddress + "\n");
                    }
                }
            }
        });

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(getApplicationContext(),deviceName,Toast.LENGTH_LONG).show();
                Intent i = new Intent(MainActivity.this, MindWave_Start.class);
                i.putExtra("deviceName",deviceName);
                i.putExtra("deviceHardwareAddress",deviceHardwareAddress);
                startActivity(i);

            }
        });


    }

}
