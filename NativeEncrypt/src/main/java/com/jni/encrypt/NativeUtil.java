package com.jni.encrypt;

import android.content.Context;

public class NativeUtil {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("NativeTools-lib");
    }

    public static native boolean init(Context appContext);

    public static native String encrypt(String plainText);

    public static native String decrypt(String cipherText);

    public static native String base64Encode(String cipherText,boolean doNewLine);

    public static native String base64Decode(String cipherText);

}
