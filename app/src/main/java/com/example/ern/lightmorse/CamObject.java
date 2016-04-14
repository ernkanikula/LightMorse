package com.example.ern.lightmorse;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;

/**
 * Created by ern on 12.03.16.
 */
public class CamObject {
    private Camera camera;
    private boolean isFlashOn;
    Camera.Parameters parameters;

    public CamObject() {
        init();
    }



    public void init() {
            camera = Camera.open();
            parameters = camera.getParameters();
            Log.d("CameraObject", "kamera = init()");
    }


    public boolean isCameraActive() {
        if (camera == null) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isFlashOn() {
        return isFlashOn;
    }

    public void setIsFlashOn(boolean isFlashOn) {
        this.isFlashOn = isFlashOn;
    }

    public void turnOnFlash() {
        if (!isFlashOn) {
            if (camera == null || parameters == null) {
                Log.d("CamoObject", "camera == null || parameters == null");
                return;
            }
            parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameters);
            camera.startPreview();
            isFlashOn = true;
        }
    }

    public void turnOffFlash() {
        if (isFlashOn) {
            if (camera == null || parameters == null) {
                return;
            }
        }
        parameters = camera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(parameters);
        camera.stopPreview();
        isFlashOn = false;

    }

    public void cameraStop() {
        if (camera != null) {
            isFlashOn = false;
            camera.release();
            camera = null;
            Log.d("CamObject:", "cameraStop()");
        }
    }

}
