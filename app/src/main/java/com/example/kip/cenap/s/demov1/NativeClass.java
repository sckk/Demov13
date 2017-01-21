package com.example.kip.cenap.s.demov1;

/**
 * Created by Cenap on 21/1/2017.
 */

public class NativeClass {
    public native static String getMessageFromJNI();
    public native static int convertGray(long matAddrRgba, long matAddrGray);
    public native static void faceDetection(long addrGba);
}
