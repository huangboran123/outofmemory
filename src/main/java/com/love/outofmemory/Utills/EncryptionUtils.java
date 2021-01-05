package com.love.outofmemory.Utills;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * @author huang
 */
public class EncryptionUtils {

    public static String encryptMD5(String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new BigInteger(1, md.digest()).toString(16);
    }

}
