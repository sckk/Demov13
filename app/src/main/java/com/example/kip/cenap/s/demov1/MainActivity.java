package com.example.kip.cenap.s.demov1;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.opencv.imgproc.Imgproc.ellipse;
import static org.opencv.objdetect.Objdetect.CASCADE_FIND_BIGGEST_OBJECT;
import static org.opencv.objdetect.Objdetect.CASCADE_SCALE_IMAGE;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2{

    private static final String TAG = "MainActivity";
    private  CascadeClassifier face_cascade;
    private  CascadeClassifier eye_cascade;
    private static String mesaj = "goz kirpildi. ;)";
    private CameraBridgeViewBase mOpenCvCameraView;

    Mat mGrayImage;
    private String face="";
    private String eye="";

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

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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
        mGrayImage = new Mat(height, width, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {
        mGrayImage.release();
    }

    private Scalar eye1 = new Scalar(0, 255, 0, 255);
    private Scalar eye2 = new Scalar(255, 0, 0, 255);
    private Scalar faceScalar = new Scalar(0, 255, 0, 0);
    private Point mPt1 = new Point();
    private Point mPt2 = new Point();

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        Mat inputMat = inputFrame.rgba();
        mGrayImage = inputFrame.rgba();
        Size mMinSize = new Size(30, 30);
        Size mMaxSize ;
        Imgproc.cvtColor(inputMat, mGrayImage, Imgproc.COLOR_RGB2BGR);

        MatOfRect faces = new MatOfRect();



        face_cascade.detectMultiScale(mGrayImage,faces,1.1, 2, CASCADE_FIND_BIGGEST_OBJECT
                        | CASCADE_SCALE_IMAGE, new Size(30, 30), new Size()); //size 30 30
        Rect[] facesArray = faces.toArray();

        for(int i = 0; i<facesArray.length ; i++){



            Point center = new Point(facesArray[i].x + facesArray[i].width * 0.5, facesArray[i].y + facesArray[i].height * 0.5);
            Size faceSize = new Size(facesArray[i].width * 0.5, facesArray[i].height * 0.5);
            ellipse(inputMat, center, faceSize, 0, 0, 360, faceScalar, 4, 8, 0);

            Mat faceROI = mGrayImage.submat(facesArray[i]); //region of interest, gozleri yuzun icinde ariyoruz.
            MatOfRect eyes = new MatOfRect();

            eye_cascade.detectMultiScale(faceROI,eyes,1.1,2,CASCADE_FIND_BIGGEST_OBJECT
                    | CASCADE_SCALE_IMAGE, new Size(30, 30), new Size());
            Rect[] eyesArray = eyes.toArray();


            for (int j = 0; j< eyesArray.length; j++){
                Log.d(TAG,"eye =" +eyesArray.length);

                mPt1.x = (facesArray[i].x + eyesArray[j].x);
                mPt1.y = (facesArray[i].y + eyesArray[j].y) ;

                mPt2.x = (facesArray[i].x + eyesArray[j].x + eyesArray[j].width);
                mPt2.y = (facesArray[i].y + eyesArray[j].y + eyesArray[j].height)*0.75;
                Imgproc.rectangle(inputMat, mPt1,mPt2,eye2,2);
            }
        }






        //NativeClass.faceDetection(mGrayImage.getNativeObjAddr(),face,eye);
        return inputMat;
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

            InputStream is2 = getResources().openRawResource(R.raw.haarcascade_eye_tree_eyeglasses);
            cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
            File eyeCascadeFile = new File(cascadeDir, "haarcascade_eye_tree_eyeglasses.xml");
            FileOutputStream os2 = new FileOutputStream(eyeCascadeFile);

            byte[] buffer2 = new byte[3000000];
            int bytesRead2;
            while ((bytesRead2 = is2.read(buffer)) != -1) {
                os2.write(buffer2, 0, bytesRead2);
            }
            is2.close();
            os2.close();


            eye = faceCascadeFile.getAbsolutePath();
            face_cascade = new CascadeClassifier(face);
            eye_cascade = new CascadeClassifier(eye);
           // NativeClass.loadXmlFiles(face,eye);

        } catch (IOException e) {
            e.printStackTrace();
            Log.v("MyActivity", "Cascadeler yuklenemedi: " + e);
        }
    }

}
