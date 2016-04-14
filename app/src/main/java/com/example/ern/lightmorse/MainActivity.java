package com.example.ern.lightmorse;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MainActivity extends Activity implements MorseCamAction.MorseCallbacks {

//    Button button_led;
    Button button_Morse;
    ToggleButton toggleButton;
    TextView textView_message;
    private Camera camera = null;

    private boolean hasFlash;
    CamObject camObject;
    private static final String TAG_MORSECAMACTION_FRAGMENT = "morse_cam_action";
    private MorseCamAction morseCamAction;
    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        FragmentManager fragmentManager = getFragmentManager();
        morseCamAction = (MorseCamAction) fragmentManager.findFragmentByTag(TAG_MORSECAMACTION_FRAGMENT);

        if (morseCamAction == null) {
            morseCamAction = new MorseCamAction();
            fragmentManager.beginTransaction().add(morseCamAction, TAG_MORSECAMACTION_FRAGMENT).commit();
        }

        if (morseCamAction.isRunning()) {
            setButtons(false);
        } else {
            setButtons(true);
        }
    }


    private void init() {

//        button_led = (Button) findViewById(R.id.button_led);
        button_Morse = (Button) findViewById(R.id.button_morse);
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        textView_message = (TextView) findViewById(R.id.editText);

        hasFlash = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        Log.d("hasFlash", Boolean.toString(hasFlash));

        if (!hasFlash) {
            // device doesn't support flash
            // Show alert message and close the application
            AlertDialog alert = new AlertDialog.Builder(MainActivity.this)
                    .create();
            alert.setTitle("Error");
            alert.setMessage("Sorry, your device doesn't support flash light!");
            alert.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // closing the application
                    finish();
                }
            });
            alert.show();
            return;
        }

        // działa
//        morseCamAction = (MorseCamAction) getLastNonConfigurationInstance();
//        if(morseCamAction == null){
//            morseCamAction = new MorseCamAction(button_led);
//        }
//        morseCamAction.connectContext(this);


        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    camObject.turnOnFlash();
                } else {
                    camObject.turnOffFlash();
                }
            }
        });


//        button_led.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (camObject.isFlashOn()) {
//                    Log.d("LED", "wylaczenie led");
//                    camObject.turnOffFlash();
////                    camObject.setIsFlashOn(false);
//                } else {
//                    Log.d("LED", "wlaczenie led");
//                    camObject.turnOnFlash();
////                    camObject.setIsFlashOn(true);
//                }
//            }
//        });

        button_Morse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (camObject != null && camObject.isFlashOn()) {
                    toggleButton.setChecked(false);
                }

                //wprowadzić poprawkę jak przy wyłączeniu morsa skasuje się znaki z pola....


                String msg = textView_message.getText().toString();
                Log.d("msg_LENGTH", Integer.toString(msg.length()));

                if (morseCamAction.isRunning()) {
                    Log.d("stop_morse", "morseCamAction.cancel()");
                    morseCamAction.cancel();
                } else {
                    if (msg.length() != 0) {
                        camObject.cameraStop();
                        String message = msg.toUpperCase();
                        morseCamAction.start(message);
                    }
                }
            }
        });

        //RESTORE SAVED STATE


    }//init


    public void setCamera() {
        if (camObject == null) {
            camObject = new CamObject();
        } else {
            camObject.init();
            Toast.makeText(getBaseContext(), "SET CAMERA",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void setButtons(boolean state){
        if(state==true){
            toggleButton.setEnabled(true);
            toggleButton.setBackgroundResource(R.drawable.buttonled_selector);
            button_Morse.setBackgroundResource(R.drawable.button_morse_background);
            button_Morse.setText(R.string.button_mors);
            button_Morse.setTextColor(Color.WHITE);

        }else {
            toggleButton.setEnabled(false);
            toggleButton.setBackgroundResource(R.drawable.button_led_off);
            button_Morse.setBackgroundResource(R.drawable.button_morse_background_stop);
            button_Morse.setText(R.string.button_morse_stop);
            button_Morse.setTextColor(getApplicationContext().getResources().getColor(R.color.colorTextBtnMorseStop));
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d("onStart", "onStart()");

        if (!morseCamAction.isRunning()) {
            if (camObject == null) {
                Log.d("onStart():", "camObject == null");
                try {
                    Log.d("onStart():", "camObject = new CamObject();");
                    camObject = new CamObject();
                } catch (RuntimeException e) {
                    Log.e("Cam_error", e.getMessage());
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onResume", "onResume()");

        if (!morseCamAction.isRunning()) {
            Log.d("onResume():", "morseCamAction.isRunning = false");
            if (!camObject.isCameraActive()) {
                Log.d("onResume():", "camObject.init()");
                camObject.init(); //wywali się jak wróci na pierwszy plan po onPause();
            }
        } else {
            Log.d("onResume():", "wątek działa!");
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (camObject != null) {
            if (camObject.isCameraActive()) {
                Log.d("onPause", "camera is active");
                camObject.setIsFlashOn(false);
            }
        }

    }

    @Override
    protected void onStop() {
        super.onStop();


        //zwalnia kamerę
        if (camObject != null) {
            Log.d("onStop()", "camObject.cameraStop()");
            camObject.cameraStop();
        }
    }

    //metody z wątku a

    @Override
    public void onPreExecute() {
//        toggleButton.setBackgroundResource(R.drawable.buttonled_selector);
        setButtons(false);
    }

    @Override
    public void onProgressUpdate(int percent) {
    }


    @Override
    public void onCancelled() {
        Log.d("onCancelled()", "cancel");
        setButtons(true);
        setCamera();

    }

    @Override
    public void onPostExecute() {
        Log.d("onPostExecute()", "onPostExecute");
        setButtons(true);
        setCamera();

    }
}

