package com.jni.encrypt;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private static final String TAG = "签名";

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        final PackageManager packageManager = appContext.getPackageManager();
        final String packageName = appContext.getPackageName();
        Log.d(TAG,"包名："+packageName);
        assertEquals("com.jni.encrypt.test", appContext.getPackageName());
        try {
            final PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            final Signature signature = packageInfo.signatures[0];
            Log.d(TAG, signature.toCharsString());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        boolean init = NativeUtil.init(appContext);
        Log.d(TAG,"签名验证结果："+init);
        assertTrue(init);
    }
}