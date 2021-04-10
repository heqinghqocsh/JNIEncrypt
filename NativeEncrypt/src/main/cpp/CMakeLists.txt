# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.10.2)

message("工程源目录：${PROJECT_SOURCE_DIR}")
message("当前CMake路径：${CMAKE_SOURCE_DIR}")
# 包含include文件
include_directories(${CMAKE_SOURCE_DIR}/include/)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

set(AES_DIR crypto/aes/)
add_library( # Sets the name of the library.
        NativeTools-lib

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        NativeUtil.cpp encode/base64.cpp base/log.cpp
        ${AES_DIR}aes_cbc.c ${AES_DIR}aes_misc.c ${AES_DIR}aes_cfb.c ${AES_DIR}aes_core.c
        ${AES_DIR}aes_local.h ${AES_DIR}aes_ecb.c ${AES_DIR}aes_ige.c ${AES_DIR}aes_ofb.c
        crypto/modes/cbc128.c crypto/modes/cfb128.c crypto/modes/modes_local.h crypto/modes/ofb128.c)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
        NativeTools-lib

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})