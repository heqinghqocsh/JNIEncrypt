
#include <base/log.h>

int LogD(const char* fmt, ...) {
    if (!LOG_ENABLE){
        return -1;
    }
    va_list argList;
    va_start(argList, fmt);
    int result = __android_log_vprint(ANDROID_LOG_DEBUG, LOG_TAG, fmt, argList);
    va_end(argList);
    return result;
}

int LogE(const char* fmt, ...) {
    if (!LOG_ENABLE){
        return -1;
    }
    va_list argList;
    va_start(argList, fmt);
    int result = __android_log_vprint(ANDROID_LOG_ERROR, LOG_TAG, fmt, argList);
    va_end(argList);
    return result;
}

int LogI(const char* fmt, ...) {
    if (!LOG_ENABLE){
        return -1;
    }
    va_list argList;
    va_start(argList, fmt);
    int result = __android_log_vprint(ANDROID_LOG_INFO, LOG_TAG, fmt, argList);
    va_end(argList);
    return result;
}



