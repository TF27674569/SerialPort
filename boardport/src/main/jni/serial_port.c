#include <jni.h>
#include <malloc.h>
#include <string.h>

#include "android/log.h"
#include "serial.h"

static const char *TAG = "serial";
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, TAG, fmt, ##args)
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##args)

#define BUF_LEN 256

// #define DEBUG

uint8_t *read_buf = NULL;

JNIEXPORT jint JNICALL
Java_com_fun_board_SerialPort_open(JNIEnv *env, jobject thiz, jstring dev, jint baud_rate) {
    // 创建读取的buff
    read_buf = (uint8_t *) malloc(BUF_LEN);
    if (read_buf == NULL) {
        LOGD("open malloc buffer err!");
        return -1;
    }

    // 获取Java字符串的UTF-8字符数组
    jboolean is_copy;
    const char *path_utf = (*env)->GetStringUTFChars(env, dev, &is_copy);
    if (path_utf == NULL) {
        LOGD("open jstring to cstring err!");
        return -1;
    }
    int res = serial_open(path_utf, baud_rate);
    // 释放资源
    (*env)->ReleaseStringChars(env, dev, (const jchar *) path_utf);
    return res;
}

JNIEXPORT jint JNICALL
Java_com_fun_board_SerialPort_write(JNIEnv *env, jobject thiz, jint fd, jbyteArray buf,
                                    jint buf_len) {
    jboolean is_copy;
    jbyte *buff_arr = (*env)->GetByteArrayElements(env, buf, &is_copy);
    if (buff_arr == NULL) {
        // 处理异常
        return -1;
    }
    uint8_t *char_buf = (uint8_t *) buff_arr;
    int res = serial_write(fd, char_buf, buf_len);

    return res;
}

JNIEXPORT jbyteArray JNICALL
Java_com_fun_board_SerialPort_read(JNIEnv *env, jobject thiz, jint fd) {
    // 获取指向字节数组的指针
    memset(read_buf, 0, BUF_LEN);
    int len = serial_read(fd, read_buf, BUF_LEN);

    if (len <= 0) {
#ifdef DEBUG
        LOGD("read err, read len is 0! ");
#endif
        return NULL;
    }

#ifdef DEBUG
    for (size_t i = 0; i < len; i++) {
        LOGD("data[%d] = %02X ", i, read_buf[i]); // 使用%02X格式以16进制形式打印每个元素，并确保至少有两位（前面补0）
    }
#endif

    jbyteArray response = (*env)->NewByteArray(env,len);
    jbyte *buf = (*env)->GetByteArrayElements(env, response, NULL);
    memcpy(buf, read_buf, len);
    (*env)->ReleaseByteArrayElements(env, response, buf, JNI_COMMIT);
    return response;

}

JNIEXPORT void JNICALL
Java_com_fun_board_SerialPort_close(JNIEnv *env, jobject thiz, jint fd) {
    serial_close(fd);
}