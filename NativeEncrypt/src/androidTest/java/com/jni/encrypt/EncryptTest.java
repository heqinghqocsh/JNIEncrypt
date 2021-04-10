package com.jni.encrypt;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * JNI加解密测试
 */
@RunWith(AndroidJUnit4.class)
public class EncryptTest {
    private static final String TAG = "Encrypt";

    @Test
    public void testBase() {
        testEncryptDecrypt("123456");
        testEncryptDecrypt("Android安卓abcdefg");
        testEncryptDecrypt("中国上海123abcABC；；!!!？？？");
        testEncryptDecrypt("中国上海123abcABC；；!!!？？？加解密测试---------------啊啊啊123abcABC；；!!!？？？加解密测试");
    }

    private void testEncryptDecrypt(String plain) {
        String encrypt = NativeUtil.encrypt(plain);
        String decrypt = NativeUtil.decrypt(encrypt);
        Log.d(TAG, "明文：\n" + plain + "\n密文：\n" + encrypt + "\n解密：\n" + decrypt);
        assertEquals("加解密失败:\n" + plain, plain, decrypt);
    }

    @Test
    public void testPerformance() {
        final String s = "{type:1,name:\"bob;:&#$\\\",function:\"加解密对比\",url:\"https://www.baidu.com/\"}";
        final StringBuilder plainText = new StringBuilder(10000);
        int i;
        for (i = 0; i < 800; i++) {
            plainText.append(s);
        }
        for (i = 0; i < 100; i++) {
            plainText.append(s);
            final String str = plainText.toString();
            encrypt(str);
        }
    }

    private void encrypt(String plainText) {
        long startTime;
        long diffC;
        final int length = plainText.length();
        final StringBuilder log = new StringBuilder(100);
        //C加解密------------------------------------------------------
        startTime = System.currentTimeMillis();
        String cE = NativeUtil.encrypt(plainText);
        final String cD = NativeUtil.decrypt(cE);
        diffC = System.currentTimeMillis() - startTime;
        log.append("字符串长度：").append(length).append("      C加解密耗时(ms)：").append(diffC);
        Log.d(TAG, log.toString());
        final boolean success = plainText.equals(cD);
        if (!success) {
            Log.d(TAG, "加解密失败原文：\n" + plainText + "---" + plainText.length());
        }
        assertTrue("加解密失败", success);
    }
}
