package com.example.smartgardenandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button irrigationButton;
    private Button led1button;
    private Button led2button;
    private Button led3plusButton;
    private TextView led3level;
    private Button led3minusButton;
    private TextView led4level;
    private Button led4plusButton;
    private Button led4minusButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        led3plusButton=findViewById(R.id.led3plusbutton);
        led3level=findViewById(R.id.led3level);
        led4level=findViewById(R.id.led4level);
        led3plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int level = Integer.parseInt((led3level.getText().toString()))+1;
                led3level.setText(String.valueOf(level));
            }
        });
        led3minusButton = findViewById(R.id.led3minusbutton);
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
        led4plusButton=findViewById(R.id.led4plusbutton);
        led4minusButton = findViewById(R.id.led4minusbutton);
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
    }




}