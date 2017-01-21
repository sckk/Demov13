LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

#opencv
OPENCVROOT:= C:\Users\Cenap\Desktop\OpenCV-android-sdk
OPENCV_CAMERA_MODULES:=on
OPENCV_INSTALL_MODULES:=on
OPENCV_LIB_TYPE:=SHARED
include ${OPENCVROOT}/sdk/native/jni/OpenCV.mk

LOCAL_SRC_FILES := com_example_kip_cenap_s_demov1_NativeClass.cpp

LOCAL_LDLIBS += -llog
LOCAL_MODULE := MyLibs

include $(BUILD_SHARED_LIBRARY)