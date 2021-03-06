/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
#include <stdio.h>
#include "opencv2/opencv.hpp"



using namespace cv;
using namespace std;


/* Header for class com_example_kip_cenap_s_demov1_NativeClass */

#ifndef _Included_com_example_kip_cenap_s_demov1_NativeClass
#define _Included_com_example_kip_cenap_s_demov1_NativeClass
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_example_kip_cenap_s_demov1_NativeClass
 * Method:    getMessageFromJNI
 * Signature: ()Ljava/lang/String;
 */
int toGray(Mat img, Mat& gray );
void detect(Mat& frame,String face, String eye);



void Java_com_example_kip_cenap_s_demov1_NativeClass_faceDetection(JNIEnv *env, jclass, jlong addrRgba,  jstring face, jstring eye);


JNIEXPORT jstring JNICALL Java_com_example_kip_cenap_s_demov1_NativeClass_getMessageFromJNI
  (JNIEnv *, jclass);

JNIEXPORT void JNICALL Java_com_example_kip_cenap_s_demov1_NativeClass_loadXmlFiles
        (JNIEnv *env, jclass, jstring face, jstring eye);

JNIEXPORT jint JNICALL Java_com_example_kip_cenap_s_demov1_NativeClass_convertGray
        (JNIEnv *, jclass, jlong, jlong );

#ifdef __cplusplus
}
#endif
#endif
