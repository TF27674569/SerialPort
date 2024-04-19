#include <jni.h>

#ifndef _Included_com_fun_broad_SerialPort
#define _Included_com_fun_broad_SerialPort
#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jint JNICALL
Java_com_fun_board_SerialPort_open(JNIEnv *env, jobject thiz, jstring dev, jint baud_rate);

JNIEXPORT jint JNICALL
Java_com_fun_board_SerialPort_write(JNIEnv *env, jobject thiz, jint fd, jbyteArray buf,
                                    jint buf_len);

JNIEXPORT jbyteArray JNICALL
Java_com_fun_board_SerialPort_read(JNIEnv *env, jobject thiz, jint fd) ;

JNIEXPORT void JNICALL
Java_com_fun_board_SerialPort_close(JNIEnv *env, jobject thiz, jint fd);

#ifdef __cplusplus
}
#endif
#endif




