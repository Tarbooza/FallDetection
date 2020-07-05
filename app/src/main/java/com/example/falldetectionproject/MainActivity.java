package com.example.falldetectionproject;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.util.TimeUtils;
import android.view.View;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import static android.app.PendingIntent.getActivity;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private AppBarConfiguration mAppBarConfiguration;
    private static final String TAG = "Main Activity";
    public boolean dismissAlert = false;
    private SensorManager sensorManager;
    Sensor accelerometer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        Log.d(TAG, "onCreate: Starting Sensor Service");
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        Log.d(TAG, "onCreate: Registered Accelerometer Listener");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public void onSensorChanged(SensorEvent foEvent) {
        Log.d(TAG, "onSensorChanged: x:" + foEvent.values[0] + "  y: " + foEvent.values[1] + "  z: " + foEvent.values[2]);
        if (foEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            double loX = foEvent.values[0];
            double loY = foEvent.values[1];
            double loZ = foEvent.values[2];

            double loAccelerationReader = Math.sqrt(Math.pow(loX, 2)
                    + Math.pow(loY, 2)
                    + Math.pow(loZ, 2));

            DecimalFormat precision = new DecimalFormat("0.00");
            double ldAccRound = Double.parseDouble(precision.format(loAccelerationReader));
            Log.d(TAG, "onSensorChanged: " + ldAccRound);

            if (ldAccRound > 15) {
                Log.d(TAG, "FALL DETECTED!");
                {
                    final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
                    tg.startTone(ToneGenerator.TONE_PROP_BEEP, 1000);
                }
                Toast.makeText(this, "FALL DETECTED!", Toast.LENGTH_LONG).show();

                //DialogFragment obj = new DialogActivity();
                //obj.show(getSupportFragmentManager(), "Title");

                /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Auto-closing Dialog");
                builder.setMessage("After 5 seconds, this dialog will be closed automatically!");
                builder.setCancelable(true);

                final AlertDialog dlg = builder.create();

                dlg.show();
                final Timer t = new Timer();
                t.schedule(new TimerTask() {
                    public void run() {
                        dlg.dismiss(); // when the task active then close the dialog
                        t.cancel();
                    }
                }, 5000);*/

                {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //Intent it = new Intent(Intent.ACTION_CALL);
                    //it.setData(Uri.parse("tel:03120780134"));
                    Uri callUri = Uri.parse("tel:080011111");
                    Intent callIntent = new Intent(Intent.ACTION_CALL, callUri);
                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION);

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermission();

                        return;
                    }

                    startActivity(callIntent);
                    //startActivity(it);
                }
            }
        }
        }

        public void requestPermission()
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},1);
        }

               @Override
                public void onAccuracyChanged (Sensor sensor,int accuracy){

                }


}