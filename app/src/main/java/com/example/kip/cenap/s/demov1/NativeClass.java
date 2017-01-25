package com.example.kip.cenap.s.demov1;

import org.opencv.objdetect.CascadeClassifier;

/**
 * Created by Cenap on 21/1/2017.
 */

public class NativeClass {

    public native static String getMessageFromJNI();
    public native static int convertGray(long matAddrRgba, long matAddrGray);
    public native static void faceDetection(long addrGba, String face, String eye);
    public native static void loadXmlFiles( String face, String eye);
}
