package com.barranquero.boundservice;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private BoundService mBoundService;
    private boolean mServiceBound;
    private Button btnPrintTime, btnStop;
    private TextView txvTime;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBoundService = ((BoundService.TheBinder) service).getService();
            mServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPrintTime = (Button) findViewById(R.id.btnPrintTime);
        btnPrintTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txvTime.setText(mBoundService.getTimeStamp());
                btnStop.setEnabled(true);
            }
        });
        btnStop = (Button) findViewById(R.id.btnStop);
        btnStop.setEnabled(false);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mServiceBound) {
                    unbindService(serviceConnection);
                    mServiceBound = false;
                }
                Intent intent = new Intent(MainActivity.this, BoundService.class);
                stopService(intent);
                btnStop.setEnabled(false);
                txvTime.setText("Se acabó lo que se daba");
            }
        });
        txvTime = (TextView) findViewById(R.id.txvTime);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(MainActivity.this, BoundService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mServiceBound) {
            unbindService(serviceConnection);
        }
        Intent intent = new Intent(MainActivity.this, BoundService.class);
        stopService(intent);
    }
}
