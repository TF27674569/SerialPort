#include <jni.h>

#ifndef _Included_com_fun_broad_Serial
#define _Included_com_fun_broad_Serial
#ifdef __cplusplus
extern "C" {
#endif

int serial_open(const char *dev_path, int baud_rate);
int serial_write(int fd, uint8_t  buf[], int buf_len);
int serial_read(int fd, uint8_t  buf[], int buf_len);
void serial_close(int fd);

#ifdef __cplusplus
}
#endif
#endif




