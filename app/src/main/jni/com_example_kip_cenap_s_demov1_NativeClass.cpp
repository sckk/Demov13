
#include <com_example_kip_cenap_s_demov1_NativeClass.h>
#include "opencv2/imgproc.hpp"


JNIEXPORT jstring JNICALL Java_com_example_kip_cenap_s_demov1_NativeClass_getMessageFromJNI
  (JNIEnv *env, jclass obj){
    return env->NewStringUTF("jni call ornek");
}

JNIEXPORT void JNICALL Java_com_example_kip_cenap_s_demov1_NativeClass_loadXmlFiles
        (JNIEnv *env, jclass){
/*
    const char *faceStr = env->GetStringUTFChars( face, 0);
    const char *eyeStr = env->GetStringUTFChars(eye, 0);
    env->ReleaseStringUTFChars(face, faceStr);
    env->ReleaseStringUTFChars(eye, eyeStr);
*/


}
JNIEXPORT jint JNICALL Java_com_example_kip_cenap_s_demov1_NativeClass_convertGray
        (JNIEnv *, jclass, jlong addrRgba, jlong addrGray){
    Mat& mRgba = *(Mat*)addrRgba;
    Mat& mGray = *(Mat*)addrGray;

    int conv;
    jint retVal;
    conv = toGray(mRgba, mGray);
    retVal = (jint)conv;

    return retVal;
}

int toGray(Mat img, Mat& gray){
    cvtColor(img, gray, CV_RGBA2GRAY);
    if(gray.rows == img.rows && gray.cols == img.cols);
    return 1;
    return 0;
}

JNIEXPORT void Java_com_example_kip_cenap_s_demov1_NativeClass_faceDetection(JNIEnv *env, jclass, jlong addrRgba, jstring face, jstring eye) {
    Mat& frame = *(Mat*)addrRgba;

    const char *faceStr = env->GetStringUTFChars( face, 0);
    const char *eyeStr = env->GetStringUTFChars(eye, 0);
    env->ReleaseStringUTFChars(face, faceStr);
    env->ReleaseStringUTFChars(eye, eyeStr);
    detect(frame,faceStr,eyeStr);

}



void detect(Mat& frame,String face, String eye) {
    String face_cascade_name = "haarcascade_frontalface_alt.xml";
    String eyes_cascade_name = "haarcascade_eye_tree_eyeglasses.xml";

    CascadeClassifier face_cascade;
    CascadeClassifier eyes_cascade;

    if (!face_cascade.load(face)) {
        printf("--(!)Error loading\n");
        return;
    };
    if (!eyes_cascade.load(eye)) {
        printf("--(!)Error loading\n");
        return;
    };

    std::vector<Rect> faces;
    Mat frame_gray;

    cvtColor(frame, frame_gray, CV_BGR2GRAY);
    equalizeHist(frame_gray, frame_gray);

    //-- Detect faces
    face_cascade.detectMultiScale(frame_gray, faces, 1.1, 2, 0 | CV_HAAR_SCALE_IMAGE, Size(30, 30));

    for (size_t i = 0; i < faces.size(); i++) {

        Point center(faces[i].x + faces[i].width * 0.5, faces[i].y + faces[i].height * 0.5);

        ellipse(frame, center, Size(faces[i].width * 0.5, faces[i].height * 0.5), 0, 0, 360,
                Scalar(0, 0, 255), 4, 8, 0);


        //cv::rectangle(frame,center, Point(faces[i].y + faces[i].height,faces[i].x + faces[i].width ), Scalar(0, 0, 250), 4,  8, 0);


        Mat faceROI = frame_gray(faces[i]);
        std::vector<Rect> eyes;

        //-- In each face, detect eyes
        eyes_cascade.detectMultiScale(faceROI, eyes, 1.1, 2, 0 | CV_HAAR_SCALE_IMAGE, Size(30, 30));

        for (size_t j = 0; j < eyes.size(); j++) {
            /*
            Point center(eyes[i].x + eyes[j].x + eyes[j].width * 0.5,
                         eyes[i].y + eyes[j].y + eyes[j].height * 0.5);
            int radius = cvRound((eyes[j].width + eyes[j].height) * 0.10);
            circle(frame, center, radius, Scalar(255, 0, 0), 4, 8, 0);
            */
            cv::rectangle(frame,Point(eyes[i].x* 0.5,eyes[i].y*0.5),Point(eyes[i].x+
            eyes[i].width,eyes[i].y+eyes[i].height ), Scalar(255, 0, 0),4, 8, 0);
        }

    }

}