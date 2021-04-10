package com.jni.encrypt;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * java加解密和C语言加解密性能测试
 */
@RunWith(AndroidJUnit4.class)
public class EncryptPerformanceTest {
    private static final String TAG = "EncryptPerformance";
    private static final Map<Integer, Long> table = new LinkedHashMap<>(100);

    @Test
    public void testPerformance() {
        table.clear();
        final String s = "{type:1,name:\"bob;:&#$\\\",function:\"加解密对比\",url:\"https://www.baidu.com/\"}";
        final StringBuilder plainText = new StringBuilder(100000);
        int i;
        for (i = 0; i < 3000; i++) {
            plainText.append(s);
        }
        for (i = 0; i < 50; i++) {
            plainText.append(s);
            final String str = plainText.toString();
            encrypt(str);
        }
        final StringBuilder builder = new StringBuilder(1000);
        Set<Map.Entry<Integer, Long>> entrySet = table.entrySet();
        builder.append("加解密字符串长度以及耗时差（J-C），单位 ms\n");
        for (Map.Entry<Integer, Long> entry : entrySet) {
            builder.append("长度：").append(entry.getKey())
                    .append("    耗时差：").append(entry.getValue())
                    .append("\n");
        }
        Log.d(TAG, "       \n\n" + builder.toString());
    }

    private void encrypt(String plainText) {
        long startTime;
        long diffJ, diffC;
        final int length = plainText.length();
        final StringBuilder log = new StringBuilder(100);

        String javaE;
        String javaD;
        try {
            //java加解密------------------------------------------------------
            startTime = System.currentTimeMillis();
            javaE = AESEncrypt.encodeReplace(plainText);
            javaD = AESEncrypt.decodeReplace(javaE);
            diffJ = System.currentTimeMillis() - startTime;
            log.append("字符串长度：").append(length).append("      java耗时：").append(diffJ);

            //C加解密------------------------------------------------------
            startTime = System.currentTimeMillis();
            String cE = NativeUtil.encrypt(plainText);
            final String cD = NativeUtil.decrypt(cE);
            diffC = System.currentTimeMillis() - startTime;
            log.append("      C耗时：").append(diffC);
            table.put(length, diffJ - diffC);
            Log.d(TAG, log.toString());
            final boolean success = javaE.equals(cE) && cD.equals(javaD);
            if (!success) {
                Log.d(TAG, "\n加解密失败：\n");
                Log.d(TAG, "失败原文：\n" + plainText + "---" + plainText.length());
                Log.d(TAG, "\n明文：\n" + plainText);
                Log.d(TAG, "\nJ密文：\n" + cE);
                Log.d(TAG, "\nC密文：\n" + cE);
                Log.d(TAG, "\nC解密：\n" + cD);
            }
            Assert.assertTrue(success);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
