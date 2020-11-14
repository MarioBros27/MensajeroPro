/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mensajeropro;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 *
 * @author alex1
 */
public class Ciphero {

    public static byte[] decipher(BigInteger key, byte[] packet) throws InvalidKeyException, InvalidKeySpecException, IllegalBlockSizeException, NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException, InvalidAlgorithmParameterException {
        byte[] de = new byte[256];

        byte[] realKey = key.toByteArray();
        byte[] finalKey = new byte[8];

        System.out.println("Size of key in bits: " + key.bitLength());
        System.out.println("Size of key array: " + realKey.length);
        int c = 0;
        for (int i = 8 - realKey.length; i < 8; i++) {
            finalKey[i] = realKey[c];
            c++;
        }
        System.out.println("Key Real Key: " + Arrays.toString(realKey));
        System.out.println("Key FinalKey: " + Arrays.toString(finalKey));
        DESKeySpec dks = new DESKeySpec(finalKey);

        System.out.println("Key DESKeySpec: " + Arrays.toString(dks.getKey()));

        System.out.println("Size of DESkey array: " + dks.getKey().length);

        SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
        Key secretKey = factory.generateSecret(dks);
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("DES/ECB/NoPadding");
        cipher.init(javax.crypto.Cipher.DECRYPT_MODE, secretKey);
        de = cipher.doFinal(packet);
        System.out.println("packet cnrypted-> Size "+packet.length+" array: "+Arrays.toString(packet));
        System.out.println("Packet decrypted-> Size "+de.length + " array: " +Arrays.toString(de));
        return de;
    }

    public static byte[] encipher(BigInteger key, byte[] packet) throws InvalidKeyException, InvalidKeySpecException, IllegalBlockSizeException, NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException, InvalidAlgorithmParameterException {
        byte[] de = new byte[256];

        byte[] realKey = key.toByteArray();
        byte[] finalKey = new byte[8];

        System.out.println("Size of key in bits: " + key.bitLength());
        System.out.println("Size of key array: " + realKey.length);
        int c = 0;
        for (int i = 8 - realKey.length; i < 8; i++) {
            finalKey[i] = realKey[c];
            c++;
        }
        System.out.println("Key Real Key: " + Arrays.toString(realKey));
        System.out.println("Key FinalKey: " + Arrays.toString(finalKey));
        DESKeySpec dks = new DESKeySpec(finalKey);

        System.out.println("Key DESKeySpec: " + Arrays.toString(dks.getKey()));

        System.out.println("Size of DESkey array: " + dks.getKey().length);

        SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
        Key secretKey = factory.generateSecret(dks);
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("DES/ECB/NoPadding");
        cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, secretKey);
        de = cipher.doFinal(packet);
        System.out.println("packet not ecnrypted-> Size "+packet.length+" array: "+Arrays.toString(packet));
        System.out.println("Packet encrypted-> Size "+de.length + " array: " +Arrays.toString(de));
        return de;
    }

    public static List<BigInteger> gimmemyXY(BigInteger q, BigInteger a) {
        Random r = new Random();
        List<BigInteger> list = new ArrayList<>();
        int qBitLength = q.bitLength();
        BigInteger x = new BigInteger(qBitLength, r);
        if (x.compareTo(q) >= 0) {
            x = x.mod(q);
        }
        System.out.println("Q: " + q + "X: " + x);
        BigInteger y = a.modPow(x, q);
        list.add(x);
        list.add(y);
        return list;
    }

    public static List<BigInteger> gimmemyKeys(BigInteger q, BigInteger a, BigInteger y) {
        List<BigInteger> list = new ArrayList<>();
        Random r = new Random();
        int qBitLength = q.bitLength();
        BigInteger x = new BigInteger(qBitLength, r);
        if (x.compareTo(q) >= 0) {
            x = x.mod(q);
        }
        System.out.println("Q: " + q + "X: " + x);
        BigInteger myY = a.modPow(x, q);
        BigInteger myKey = y.modPow(x, q);
        list.add(myY);
        list.add(myKey);
        return list;
    }

    //Codigo para convertir bajado de https://www.baeldung.com/java-byte-arrays-hex-strings
    public static byte[] decodeHexString(String hexString) {
        if (hexString.length() % 2 == 1) {
            throw new IllegalArgumentException(
                    "Invalid hexadecimal String supplied.");
        }

        byte[] bytes = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i += 2) {
            bytes[i / 2] = hexToByte(hexString.substring(i, i + 2));
        }
        return bytes;
    }

    public static byte hexToByte(String hexString) {
        int firstDigit = toDigit(hexString.charAt(0));
        int secondDigit = toDigit(hexString.charAt(1));
        return (byte) ((firstDigit << 4) + secondDigit);
    }

    private static int toDigit(char hexChar) {
        int digit = Character.digit(hexChar, 16);
        if (digit == -1) {
            throw new IllegalArgumentException(
                    "Invalid Hexadecimal Character: " + hexChar);
        }
        return digit;
    }
}
