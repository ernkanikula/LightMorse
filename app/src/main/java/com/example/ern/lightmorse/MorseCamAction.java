package com.example.ern.lightmorse;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.logging.Handler;

/**
 * Created by ern on 14.03.16.
 */

public class MorseCamAction extends Fragment {

    interface MorseCallbacks {
        void onProgressUpdate(int percent);

        void onPreExecute();

        void onCancelled();

        void onPostExecute();
    }

    private MorseCallbacks morseCallbacks;
    private boolean mRunning;
    private MorseTask morseTaskk;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(!(activity instanceof MorseCallbacks)){
            throw new IllegalStateException("Aktyność musi implementować interferjs");
        }
        morseCallbacks = (MorseCallbacks) activity;
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//        Activity a;
//
//        if (context instanceof Activity){
//            a=(Activity) context;
//        }
//
//    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        morseCallbacks = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancel();
    }


    public void start(String message) {
        if(!mRunning) {
            morseTaskk = new MorseTask();
            morseTaskk.execute(message);
            mRunning=true;
        }
    }

    public void cancel(){
        if(mRunning){
            morseTaskk.cancel(false);
            morseTaskk = null;
            mRunning = false;
        }
    }

    public boolean isRunning(){
        return mRunning;
    }


    private class MorseTask extends AsyncTask<String, Void, Void> {
        private Activity context;
        //    private MainActivity context;
        CamObject camObject;
        HashMap morseHashMap;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(morseCallbacks != null){
                morseCallbacks.onPreExecute();
                mRunning = true;
            }
        }

        @Override
        protected Void doInBackground(String... params) {
            camObject = new CamObject();
            morseHashMap = MorseData.getHashMap();
            String message = params[0];

            Log.d("mes:", message);
            String[] tabMessage = message.split("");   //konwersja napisu do tablicy stringów
            Log.d("dlugosc", String.valueOf(tabMessage.length));

            String leter;   //pojedyncza litera
            String code;    //wartość pojedynczego zanaku z alfabetu morsa
            char[] buffChars;



            outerLoop:
                for (int i = 1; i < tabMessage.length; i++) {
                    try {
                        leter = tabMessage[i];
                        code = morseHashMap.get(leter).toString();
                        Log.d("code:", code);
                        if (leter == " ") {
                            Thread.sleep(1500);
                        } else {
                            buffChars = code.toCharArray();
                            for (int k = 0; k < buffChars.length; k++) {
                                if(mRunning==false){
                                    break outerLoop;
                                }
                                if (buffChars[k] == '.') {
                                    Log.d("kropka:", Character.toString(buffChars[k]));
                                    camObject.turnOnFlash();
                                    Thread.sleep(500);
                                    camObject.turnOffFlash();
                                } else if (buffChars[k] == '-') {
                                    Log.d("kreska:", Character.toString(buffChars[k]));
                                    camObject.turnOnFlash();
                                    Thread.sleep(1500);
                                    camObject.turnOffFlash();
                                }
                                Thread.sleep(500);
                            }
                        }
                    } catch (RuntimeException e) {
                        Log.e("znak", "Blad - znak jest z poza zaimplementowanego alfabetu!!");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            camObject.cameraStop();
            camObject = null;

            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if(morseCallbacks != null){
                morseCallbacks.onCancelled();
                mRunning = false;
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(morseCallbacks != null){
                morseCallbacks.onPostExecute();
                mRunning = false;
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


}