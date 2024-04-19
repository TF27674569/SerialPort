#include <termios.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <string.h>
#include <jni.h>

#include "android/log.h"

static const char *TAG = "serial";
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, TAG, fmt, ##args)
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##args)

/**
 * 获取比特率
 */
static speed_t get_baud_rate(int baud_rate) {
    switch (baud_rate) {
        case 0:
            return B0;
        case 50:
            return B50;
        case 75:
            return B75;
        case 110:
            return B110;
        case 134:
            return B134;
        case 150:
            return B150;
        case 200:
            return B200;
        case 300:
            return B300;
        case 600:
            return B600;
        case 1200:
            return B1200;
        case 1800:
            return B1800;
        case 2400:
            return B2400;
        case 4800:
            return B4800;
        case 9600:
            return B9600;
        case 19200:
            return B19200;
        case 38400:
            return B38400;
        case 57600:
            return B57600;
        case 115200:
            return B115200;
        case 230400:
            return B230400;
        case 460800:
            return B460800;
        case 500000:
            return B500000;
        case 576000:
            return B576000;
        case 921600:
            return B921600;
        case 1000000:
            return B1000000;
        case 1152000:
            return B1152000;
        case 1500000:
            return B1500000;
        case 2000000:
            return B2000000;
        case 2500000:
            return B2500000;
        case 3000000:
            return B3000000;
        case 3500000:
            return B3500000;
        case 4000000:
            return B4000000;
        default:
            return -1;
    }
}

int serial_open(const char *dev_path, int baud_rate) {
    LOGD("serial_open");
    speed_t speed = get_baud_rate(baud_rate);
    if (speed == -1) {
        LOGE("Invalid baud_rate");
        return -1;
    }

    int fd = open(dev_path, O_RDWR | O_SYNC);

    LOGD("serial_open() fd = %d", fd);
    if (fd == -1) {
        LOGE("Cannot open port");
        return -1;
    }
    // 设置非阻塞模式
    if (fcntl(fd, F_SETFL, O_NONBLOCK) == -1) {
        LOGD("fcntl error!");
        close(fd);
        return -1;
    }


    struct termios cfg;
    LOGD("Configuring serial port");
    if (tcgetattr(fd, &cfg)) {
        LOGE("tcgetattr() failed");
        close(fd);
        return -1;
    }

    cfmakeraw(&cfg);
    cfsetispeed(&cfg, speed);
    cfsetospeed(&cfg, speed);

    if (tcsetattr(fd, TCSANOW, &cfg)) {
        LOGE("tcsetattr() failed");
        close(fd);
        return -1;
    }
//    // 读取时不阻塞
//    cfg.c_cc[VMIN] = 0;
//    // 100分之一秒的超时
//    cfg.c_cc[VTIME] = 100;
    LOGD("serial_open success");
    return fd;
}

int serial_write(int fd, uint8_t buf[], int buf_len) {
    return write(fd, buf, buf_len);
}

int serial_read(int fd, uint8_t buf[], int buf_len) {
    return read(fd, buf, buf_len);
}


void serial_close(int fd) {
    LOGD("serial_close() fd = %d", fd);
    close(fd);
}
