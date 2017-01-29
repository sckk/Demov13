package com.example.kip.cenap.s.demov1;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2{

    private static final String TAG = "MainActivity";
    CameraBridgeViewBase mOpenCvCameraView;

    Mat mRgba;
    public String face="";
    public String eye="";

    BaseLoaderCallback mLoaderCallBack = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status){
                case BaseLoaderCallback.SUCCESS:{
                    load_cascade();
                    mOpenCvCameraView.enableView();
                    break;
                }
                default:{
                    super.onManagerConnected(status);
                    break;
                }
            }

        }
    };

    static{
        System.loadLibrary("MyLibs");
    }

    static{
        if(OpenCVLoader.initDebug()){
            Log.d(TAG, "OpenCV okey");
        }
        else{
            Log.d(TAG,"nope");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mOpenCvCameraView = (JavaCameraView)findViewById(R.id.javaCameraView);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);

        //jni ornek !!
       //
        //((TextView)findViewById(R.id.textView)).setText(NativeClass.getMessageFromJNI());
    }
    @Override
    protected void onPause(){
        super.onPause();
        if(mOpenCvCameraView !=null);
            mOpenCvCameraView.disableView();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        if(mOpenCvCameraView !=null)
            mOpenCvCameraView.disableView();

    }
    @Override
    protected void onResume(){
        super.onResume();
        if(OpenCVLoader.initDebug()){
            Log.d(TAG, "OpenCV okey");
            mLoaderCallBack.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        else{
            Log.d(TAG,"nope");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0,this,mLoaderCallBack);
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8SC4);
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        NativeClass.faceDetection(mRgba.getNativeObjAddr(),face,eye);
        return mRgba;
    }

    public void load_cascade(){
        try {
            InputStream is = getResources().openRawResource(R.raw.haarcascade_frontalface_alt);
            File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
            File faceCascadeFile = new File(cascadeDir, "haarcascade_frontalface_alt.xml");
            FileOutputStream os = new FileOutputStream(faceCascadeFile);

            byte[] buffer = new byte[3000000];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();
            face = faceCascadeFile.getAbsolutePath();

            InputStream is2 = getResources().openRawResource(R.raw.haarcascade_eye);
            cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
            File eyeCascadeFile = new File(cascadeDir, "haarcascade_eye.xml");
            FileOutputStream os2 = new FileOutputStream(eyeCascadeFile);

            byte[] buffer2 = new byte[3000000];
            int bytesRead2;
            while ((bytesRead2 = is2.read(buffer)) != -1) {
                os2.write(buffer2, 0, bytesRead2);
            }
            is2.close();
            os2.close();


            eye = faceCascadeFile.getAbsolutePath();

           // NativeClass.loadXmlFiles(face,eye);

        } catch (IOException e) {
            e.printStackTrace();
            Log.v("MyActivity", "Failed to load cascade. Exception thrown: " + e);
        }
    }

}
