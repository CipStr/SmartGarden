package com.example.smartgardenandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothServerSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


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
    private Button manualButton;
    String TAG = "MainActivity";
    private static final int REQUEST_ENABLE_BT = 1;
    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private BluetoothDevice mmDevice;
    private UUID deviceUUID;
    ConnectedThread mConnectedThread;
    EditText send_data;
    TextView view_data;
    StringBuilder messages;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //request bluetooth permissions
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, 1);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, 1);


        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new
                    Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }


        setContentView(R.layout.activity_main);
        manualButton = findViewById(R.id.manualbutton);
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
                    if (bluetoothAdapter == null) {
                        Toast.makeText(getApplicationContext(), "Bluetooth not supported", Toast.LENGTH_SHORT).show();
                        tgb.setChecked(false);
                    } else {
                        connectToArduinoGarden();
                    }
                }
            }
        });
        manualButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendManualMessage();
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void connectToArduinoGarden() {
        //make toast to inform user that bluetooth is being connected
        Toast.makeText(getApplicationContext(), "Connecting...", Toast.LENGTH_SHORT).show();
        //connect to arduinoGarden device
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                //log device
                Log.d("Device: ", device.getName() + " " + device.getAddress());
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
            //mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            //mmSocket.connect();
            ConnectThread connect = new ConnectThread(mmDevice, mmDevice.getUuids()[0].getUuid());
            connect.start();
            //if connection is successful make toast message
            Toast.makeText(getApplicationContext(), "Connected to arduinoGarden", Toast.LENGTH_SHORT).show();
        }
    }

    private class ConnectThread extends Thread {
        private BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice device, UUID uuid) {
            Log.d(TAG, "ConnectThread: started.");
            mmDevice = device;
            deviceUUID = uuid;
        }

        @SuppressLint("MissingPermission")
        public void run() {
            BluetoothSocket tmp = null;
            Log.i(TAG, "RUN mConnectThread ");

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                Log.d(TAG, "ConnectThread: Trying to create InsecureRfcommSocket using UUID: "
                        + MY_UUID_INSECURE);
                tmp = mmDevice.createRfcommSocketToServiceRecord(MY_UUID_INSECURE);
            } catch (IOException e) {
                Log.e(TAG, "ConnectThread: Could not create InsecureRfcommSocket " + e.getMessage());
            }

            mmSocket = tmp;

            // Make a connection to the BluetoothSocket

            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket.connect();

            } catch (IOException e) {
                // Close the socket
                try {
                    mmSocket.close();
                    Log.d(TAG, "run: Closed Socket.");
                } catch (IOException e1) {
                    Log.e(TAG, "mConnectThread: run: Unable to close connection in socket " + e1.getMessage());
                }
                Log.d(TAG, "run: ConnectThread: Could not connect to UUID: " + MY_UUID_INSECURE);
            }

            //will talk about this in the 3rd video
            connected(mmSocket);
        }

        public void cancel() {
            try {
                Log.d(TAG, "cancel: Closing Client Socket.");
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "cancel: close() of mmSocket in Connectthread failed. " + e.getMessage());
            }
        }
    }

    private void connected(BluetoothSocket mmSocket) {
        Log.d(TAG, "connected: Starting.");

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(mmSocket);
        mConnectedThread.start();
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "ConnectedThread: Starting.");

            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;


            try {
                tmpIn = mmSocket.getInputStream();
                tmpOut = mmSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream

            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                // Read from the InputStream
                try {
                    bytes = mmInStream.read(buffer);
                    final String incomingMessage = new String(buffer, 0, bytes);
                    Log.d(TAG, "InputStream: " + incomingMessage);

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            view_data.setText(incomingMessage);
                        }
                    });


                } catch (IOException e) {
                    Log.e(TAG, "write: Error reading Input Stream. " + e.getMessage());
                    break;
                }
            }
        }


        public void write(byte[] bytes) {
            String text = new String(bytes, Charset.defaultCharset());
            Log.d(TAG, "write: Writing to outputstream: " + text);
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Log.e(TAG, "write: Error writing to output stream. " + e.getMessage());
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

    private void SendManualMessage() {
        String message = "M";
        byte[] msgBuffer = message.getBytes();
        mConnectedThread.write(msgBuffer);
    }

    public void SendMessage(View v) {
        //put "M" in bytes array
        byte[] bytes = new byte[6];
        if(led1status){
            bytes[0] = '1';
        }
        else{
            bytes[0] = '0';
        }
        if(led2status){
            bytes[1] = '1';
        }
        else{
            bytes[1] = '0';
        }
        bytes[2] = (byte) led3level.getText().toString().charAt(0);
        bytes[3] = (byte) led4level.getText().toString().charAt(0);
        if(irrigationStatus){
            bytes[4] = '1';
        }
        else{
            bytes[4] = '0';
        }
        bytes[5] = (byte) irrigationLevel.getText().toString().charAt(0);
        //log bytes
        StringBuilder sb = new StringBuilder();
        for(byte b: bytes){
            sb.append(String.format("%02X ", b));
        }
        Log.d(TAG, "SendMessage: bytes: " + sb.toString());
       // byte[] bytes = send_data.getText().toString().getBytes(Charset.defaultCharset());
        mConnectedThread.write(bytes);
    }

    public void Start_Server(View view) {

        AcceptThread accept = new AcceptThread();
        accept.start();

    }

    private class AcceptThread extends Thread {

        // The local server socket
        private final BluetoothServerSocket mmServerSocket;

        @SuppressLint("MissingPermission")
        public AcceptThread() {
            BluetoothServerSocket tmp = null;

            // Create a new listening server socket
            try {
                tmp = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord("appname", MY_UUID_INSECURE);

                Log.d(TAG, "AcceptThread: Setting up Server using: " + MY_UUID_INSECURE);
            } catch (IOException e) {
                Log.e(TAG, "AcceptThread: IOException: " + e.getMessage());
            }

            mmServerSocket = tmp;
        }

        public void run() {
            Log.d(TAG, "run: AcceptThread Running.");

            BluetoothSocket socket = null;

            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                Log.d(TAG, "run: RFCOM server socket start.....");

                socket = mmServerSocket.accept();

                Log.d(TAG, "run: RFCOM server socket accepted connection.");

            } catch (IOException e) {
                Log.e(TAG, "AcceptThread: IOException: " + e.getMessage());
            }

            //talk about this is in the 3rd
            if (socket != null) {
                connected(socket);
            }

            Log.i(TAG, "END mAcceptThread ");
        }

        public void cancel() {
            Log.d(TAG, "cancel: Canceling AcceptThread.");
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "cancel: Close of AcceptThread ServerSocket failed. " + e.getMessage());
            }
        }
    }
}