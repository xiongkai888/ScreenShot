package com.xson.common.utils.des;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * Created by xkai on 2018/2/4.
 */

public class Des {

    private static String DESKey = "LiJia888"; // 字节数必须是8的倍数
    private static byte[] iv1 = {(byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF};

    // 加解密统一使用的编码方式
    private final static String encoding = "utf-8";

    public static byte[] desEncrypt(byte[] plainText) throws Exception {

        IvParameterSpec iv = new IvParameterSpec(iv1);

        DESKeySpec dks = new DESKeySpec(DESKey.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte data[] = plainText;
        byte encryptedData[] = cipher.doFinal(data);
        return encryptedData;
    }


    /**
     * 3DES加密
     * @param input
     * @return
     */
    public static String encrypt(String input) {
        String result = "input";
        try {
            result = base64Encode(desEncrypt(input.getBytes()));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    public static String base64Encode(byte[] s) {
        if (s == null)
            return null;
        return Base64.encodeToString(s, Base64.DEFAULT);

    }

    /**
     * 3DES解密
     *
     * @param encryptText 加密文本
     * @return
     * @throws Exception
     */
    public static String decode(String encryptText) throws Exception {
        DESKeySpec spec = new DESKeySpec(DESKey.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DES");
        SecretKey deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(iv1);
        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);

        byte[] decryptData = cipher.doFinal(Base64.decode(encryptText,Base64.DEFAULT));

        return new String(decryptData, encoding);
    }
}
