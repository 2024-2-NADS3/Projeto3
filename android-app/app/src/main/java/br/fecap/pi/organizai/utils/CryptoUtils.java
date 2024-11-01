package br.fecap.pi.organizai.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import android.util.Base64;

public class CryptoUtils {

    public static String encrypt(String data, String aesKey) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(aesKey.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encryptedData = cipher.doFinal(data.getBytes("UTF-8"));
        return Base64.encodeToString(encryptedData, Base64.DEFAULT);
    }

    public static String decrypt(String encryptedData, String aesKey) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(aesKey.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decodedData = Base64.decode(encryptedData, Base64.DEFAULT);
        byte[] decryptedData = cipher.doFinal(decodedData);
        return new String(decryptedData, "UTF-8");
    }
}
