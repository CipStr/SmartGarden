package com.example.smartgardenandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private String deviceName = null;
    private String deviceAddress;
    public static Handler handler;
    public static BluetoothSocket mmSocket;

    private final static int CONNECTING_STATUS = 1; // used in bluetooth handler to identify message status
    private final static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update


    private Button irrigationButton;
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

        setContentView(R.layout.activity_main);
        led3plusButton=findViewById(R.id.led3plusbutton);
        led3level=findViewById(R.id.led3level);
        led4level=findViewById(R.id.led4level);
        led3minusButton = findViewById(R.id.led3minusbutton);
        led4plusButton=findViewById(R.id.led4plusbutton);
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
                int level = Integer.parseInt((led3level.getText().toString()))+1;
                led3level.setText(String.valueOf(level));
            }
        });
        led3minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int level = Integer.parseInt((led3level.getText().toString()));
                if(level > 0) {
                    level--;
                    led3level.setText(String.valueOf(level));
                }
            }
        });
        led4plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int level = Integer.parseInt((led4level.getText().toString()))+1;
                led4level.setText(String.valueOf(level));
            }
        });
        led4minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int level = Integer.parseInt((led4level.getText().toString()));
                if(level > 0) {
                    level--;
                    led4level.setText(String.valueOf(level));
                }
            }
        });
        led1button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(led1status) {
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
                if(led2status) {
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
                if(irrigationStatus) {
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
                if(level > 0) {
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
    }


}