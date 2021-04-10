package com.jni.encrypt;

import android.util.Base64;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.nio.charset.Charset;

import static org.junit.Assert.assertFalse;

/**
 * Created on 2021/4/6.
 *
 * desc : Base64编解码测试
 */
@RunWith(AndroidJUnit4.class)
public class Base64Test {
    private static final String TAG = "base64";

    @Test
    public void testBase() {
        testBase64("");
        testBase64("123456");
        testBase64("啊哈哈嘿123456ABCDEF；");
    }

    @Test
    public void testPerformance() {
        final String s = "123456abcdef:\\{\"\"汉字http://.";
        final StringBuilder builder = new StringBuilder(1000);
        final int length = 3200;
        for (int i = 0; i < length; i++) {
            builder.append(s);
            if (i >= 3150) {
                testBase64(builder.toString());
            }
        }
        Log.d(TAG, "测试完成--" + length);
    }

    private void testBase64(String s) {
        final StringBuilder log = new StringBuilder(100);
        log.append("数据长度：").append(s.length());
        long start = System.currentTimeMillis();
        String encodeJ = javaBase64Encode(s);
        String decodeJ = javaBase64Decode(encodeJ);
        log.append("    java base64编码耗时（ms）：").append(System.currentTimeMillis() - start);
        start = System.currentTimeMillis();
        String encodeC = NativeUtil.base64Encode(s, true);
        String decodeC = NativeUtil.base64Decode(encodeC);
        log.append("---------------C base64编码耗时（ms）：").append(System.currentTimeMillis() - start);
        Log.d(TAG, log.toString());
        final boolean error = !encodeC.equals(encodeJ) || !decodeC.equals(decodeJ);
        if (error) {
            Log.d(TAG, "以下数据不同");
            Log.d(TAG, s + "----" + s.length());
            Log.d(TAG, encodeC);
            Log.d(TAG, encodeJ);
        }
        assertFalse(error);
    }

    private String javaBase64Encode(String s) {
        return Base64.encodeToString(s.getBytes(Charset.forName("UTF-8")), Base64.DEFAULT);
    }

    private String javaBase64Decode(String s) {
        return new String(Base64.decode(s, Base64.DEFAULT));
    }

}
