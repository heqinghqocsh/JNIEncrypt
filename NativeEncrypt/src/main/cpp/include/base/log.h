
#include <android/log.h>

#ifndef LOG_TAG
#define LOG_TAG  "NativeTools-lib"
#endif

#ifndef LOG_ENABLE
#define LOG_ENABLE  true
#endif

int LogI(const char* fmt, ...);

int LogD(const char* fmt, ...);

int LogE(const char* fmt, ...);