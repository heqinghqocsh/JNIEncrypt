package com.jni.encrypt;

import android.text.TextUtils;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESEncrypt {

    private static final String AES_KEY = "ynadcc@p$abw0rdcdbttye$38949!@$@";
    private static final String AES_IV = "=o&g%m,*o?dL~def";
    private static final String AES_ALGORITHM = "AES/CFB/PKCS7Padding";
    private static final String CHARSET = "UTF-8";

    @Nullable
    private static Cipher encodeCipher = null;
    @Nullable
    private static Cipher decodeCipher = null;

    private static synchronized Cipher getEncodeCipher() throws InvalidKeyException,
            NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, UnsupportedEncodingException {

        if (encodeCipher == null) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(AES_KEY.getBytes(CHARSET), "AES");
            encodeCipher = Cipher.getInstance(AES_ALGORITHM);
            encodeCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(AES_IV.getBytes(CHARSET)));
        }
        return encodeCipher;
    }

    private static synchronized Cipher getDecodeCipher() throws InvalidKeyException,
            NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, UnsupportedEncodingException {

        if (decodeCipher == null) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(AES_KEY.getBytes(CHARSET), "AES");
            decodeCipher = Cipher.getInstance(AES_ALGORITHM);
            decodeCipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(AES_IV.getBytes(CHARSET)));
        }
        return decodeCipher;

    }

    @Nullable
    public static synchronized byte[] encode(@NonNull byte[] data) {
        try {
            return getEncodeCipher().doFinal(data);
        } catch (Exception e) {
            return null;
        }
    }

    public static String encodeReplace(@NonNull String data) throws UnsupportedEncodingException {
        if (TextUtils.isEmpty(data)) {
            return "";
        }
        byte[] bytes = encode(data.getBytes(CHARSET));
        if (bytes == null) {
            return "";
        }
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    @Nullable
    private static synchronized byte[] decode(byte[] data) {
        try {
            return getDecodeCipher().doFinal(data);
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    public static String decodeReplace(@NonNull String data) {
        if (TextUtils.isEmpty(data)) {
            return "";
        }
        try {
            byte[] datas = decode(Base64.decode(data, Base64.NO_WRAP));
            return new String(datas, CHARSET);
        } catch (Exception e) {
            return "";
        }
    }
}
