package com.epam.accounts.utils;

import org.apache.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class Secure {
    private static final Logger logger = Logger.getLogger(Secure.class.getName());
    private static final String ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 16;

    public static void main(String[] args) {
        try {
            String pass = "test";
            String salt = generateSalt();
            System.out.println(pass);
            System.out.println(salt);
            System.out.println(passwordToHash(salt, pass));
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
    }

    public static String passwordToHash(String salt, String password) throws ApplicationException {
        StringBuilder hashString = new StringBuilder();
        String saltAndPass = salt + password;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM);
            byte[] hash = messageDigest.digest(saltAndPass.getBytes(StandardCharsets.UTF_8));
            for (byte hashDigit : hash) {
                String hashDigitHex = Integer.toHexString(0xFF & hashDigit);
                hashDigitHex = (hashDigitHex.length() == 1) ? "0" + hashDigitHex : hashDigitHex;
                hashString.append(hashDigitHex);
            }
        } catch (NoSuchAlgorithmException e) {
            logger.error(e);
            throw new ApplicationException("Coding algorithm doesn't work.", e);
        }
        return hashString.toString().toUpperCase();
    }

    public static String generateSalt() throws ApplicationException {
        try {
            MessageDigest.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            logger.error(e);
            throw new ApplicationException("Coding algorithm doesn't work.", e);
        }
        byte[] randomArray = new byte[SALT_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(randomArray);
        return Integer.toHexString(Arrays.hashCode(randomArray));
    }
}
