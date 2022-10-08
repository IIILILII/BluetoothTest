package com.example.bluetoothtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btnBluetoothOn;
    Button btnBluetoothOff;

    TextView tvBluetoothStatus;
    BluetoothAdapter mBluetoothAdapter;

    //Mode
    final static int BT_REQUEST_ENABLE = 1;
    final static int BT_MESSAGE_READ = 2;
    final static int BT_CONNECTING_STATUS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize
        btnBluetoothOn = findViewById(R.id.btnBluetoothOn);
        btnBluetoothOff = findViewById(R.id.btnBluetoothOff);
        tvBluetoothStatus = findViewById(R.id.tvBluetoothStatus);

        //get Defualt of blooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //1. Bluetooth on/off
        btnBluetoothOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blueToothOn();
            }
        });

        btnBluetoothOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blueToothOff();
            }
        });

    }

    public void blueToothOn() {
        if (mBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "This device doesn't support bluetooth service", Toast.LENGTH_SHORT).show();
            tvBluetoothStatus.setText("NonActive");
        } else if (mBluetoothAdapter.isEnabled()) {
            Toast.makeText(getApplicationContext(), "Already On", Toast.LENGTH_SHORT).show();
        } else {
            Intent intentBluetoothEnable = new Intent(mBluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            }
            startActivityForResult(intentBluetoothEnable, BT_REQUEST_ENABLE);

            IntentFilter BTIntent = new IntentFilter(mBluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadCastReCever, BTIntent);

        }
    }

    public void blueToothOff() {
        if (!mBluetoothAdapter.isEnabled()) {
            Toast.makeText(getApplicationContext(), "Already OFF", Toast.LENGTH_SHORT).show();
        } else if (mBluetoothAdapter.isEnabled()) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mBluetoothAdapter.disable();
        }
    }

    private final BroadcastReceiver mBroadCastReCever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);

                switch (state) {
                    case BluetoothAdapter.STATE_ON:
                        Toast.makeText(getApplicationContext(), "Bluetooth on", Toast.LENGTH_SHORT).show();
                        tvBluetoothStatus.setText("Active");
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        Toast.makeText(getApplicationContext(), "Bluetooth off", Toast.LENGTH_SHORT).show();
                        tvBluetoothStatus.setText("Nonactive");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Toast.makeText(getApplicationContext(), "Bluetooth turing On", Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Toast.makeText(getApplicationContext(), "Bluetooth turing Off", Toast.LENGTH_SHORT).show();
                        break;

                }

            }

        }
        protected void onDestory() {
            Toast.makeText(getApplicationContext(),"onDestory called", Toast.LENGTH_SHORT).show();
            Log.d("onDestroy","onDestroy called");
            MainActivity.super.onDestroy();
            unregisterReceiver(mBroadCastReCever);
        }
    };
    
}