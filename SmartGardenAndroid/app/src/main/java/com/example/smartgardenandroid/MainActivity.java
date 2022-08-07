package com.example.smartgardenandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

<<<<<<< HEAD
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
=======
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
>>>>>>> 047c2eaacd3e90c2ff46ad79e0b2386584090f06
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

<<<<<<< HEAD
    private String deviceName = null;
    private String deviceAddress;
    public static Handler handler;
    public static BluetoothSocket mmSocket;

    private final static int CONNECTING_STATUS = 1; // used in bluetooth handler to identify message status
    private final static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update

=======
    BluetoothAdapter mBluetoothAdapter = null;
    BluetoothSocket mmSocket = null;
    BluetoothDevice mmDevice = null;
>>>>>>> 047c2eaacd3e90c2ff46ad79e0b2386584090f06

    private Button irrigationButton;
    private ToggleButton tgb;
    private Button led1button;
    private Button led2button;
    private Button led3plusButton;
    private TextView led3level;
    private Button led3minusButton;
    private TextView led4level;
    private Button led4plusButton;
    private Button led4minusButton;
    private Button irrigationMinusButton;
    private Button irrigationPlusButton;
    private TextView irrigationLevel;
    private boolean led1status = false;
    private boolean led2status = false;
    private boolean irrigationStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //request bluetooth permissions
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, 1);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, 1);

        setContentView(R.layout.activity_main);
        tgb = findViewById(R.id.tgb);
        led3plusButton = findViewById(R.id.led3plusbutton);
        led3level = findViewById(R.id.led3level);
        led4level = findViewById(R.id.led4level);
        led3minusButton = findViewById(R.id.led3minusbutton);
        led4plusButton = findViewById(R.id.led4plusbutton);
        led4minusButton = findViewById(R.id.led4minusbutton);
        irrigationButton = findViewById(R.id.irrigationbutton);
        led1button = findViewById(R.id.led1button);
        led2button = findViewById(R.id.led2button);
        irrigationMinusButton = findViewById(R.id.irrigationminusbutton);
        irrigationPlusButton = findViewById(R.id.irrigationlevelplus);
        irrigationLevel = findViewById(R.id.irrigationlevel);
        setupButtons();
    }

    private void setupButtons() {
        led3plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int level = Integer.parseInt((led3level.getText().toString())) + 1;
                led3level.setText(String.valueOf(level));
            }
        });
        led3minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int level = Integer.parseInt((led3level.getText().toString()));
                if (level > 0) {
                    level--;
                    led3level.setText(String.valueOf(level));
                }
            }
        });
        led4plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int level = Integer.parseInt((led4level.getText().toString())) + 1;
                led4level.setText(String.valueOf(level));
            }
        });
        led4minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int level = Integer.parseInt((led4level.getText().toString()));
                if (level > 0) {
                    level--;
                    led4level.setText(String.valueOf(level));
                }
            }
        });
        led1button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (led1status) {
                    led1status = false;
                    led1button.setText("ON");
                } else {
                    led1status = true;
                    led1button.setText("OFF");
                }
            }
        });
        led2button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (led2status) {
                    led2status = false;
                    led2button.setText("ON");
                } else {
                    led2status = true;
                    led2button.setText("OFF");
                }
            }
        });
        irrigationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (irrigationStatus) {
                    irrigationStatus = false;
                    irrigationButton.setText("OPEN");
                } else {
                    irrigationStatus = true;
                    irrigationButton.setText("CLOSE");
                }
            }
        });
        irrigationMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int level = Integer.parseInt((irrigationLevel.getText().toString()));
                if (level > 0) {
                    level--;
                    irrigationLevel.setText(String.valueOf(level));
                }
            }
        });
        irrigationPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int level = Integer.parseInt((irrigationLevel.getText().toString()));
                level++;
                irrigationLevel.setText(String.valueOf(level));
            }
        });
        //evento: tap sul togglebutton per la connessione del bluetooth
        tgb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //connect to arduinoGarden device
                if (tgb.isChecked()) {
                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter == null) {
                        Toast.makeText(getApplicationContext(), "Bluetooth not supported", Toast.LENGTH_SHORT).show();
                        tgb.setChecked(false);
                    } else {
                        connectToArduinoGarden();
                    }
                } else {
                    //disconnect from arduinoGarden device
                    if (mmSocket != null) {
                        try {
                            mmSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void connectToArduinoGarden() {
        //make toast to inform user that bluetooth is being connected
        Toast.makeText(getApplicationContext(), "Connecting...", Toast.LENGTH_SHORT).show();
    //connect to arduinoGarden device
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                //log device
                Log.d("Device: " ,device.getName() + " " + device.getAddress());
                if (device.getName().equals("arduinoGarden")) {
                    mmDevice = device;
                    break;
                }
            }
        }
        //log mmDevice
        if (mmDevice == null) {
            Toast.makeText(getApplicationContext(), "arduinoGarden not found", Toast.LENGTH_SHORT).show();
            tgb.setChecked(false);
        } else {
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            try {
                mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
                mmSocket.connect();
                //if connection is successful make toast message
                  Toast.makeText(getApplicationContext(), "Connected to arduinoGarden", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}