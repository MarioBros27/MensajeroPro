/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mensajeropro;

import java.awt.Color;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;

/**
 *
 * @author andres
 */
public class Util {

    public static String toHexString(byte[] hash) {
        // Convert byte array into signum representation  
        BigInteger number = new BigInteger(1, hash);

        // Convert message digest into hex value  
        StringBuilder hexString = new StringBuilder(number.toString(16));

        // Pad with leading zeros 
        while (hexString.length() < 32) {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }

    public static String translate(byte[] messageIn) {
        int off = 20;
        String message = "";
        int size = messageIn[9] & 255;
        for (int c = off; c < off + size && c < 235; c++) {
            message = message + (char) messageIn[c];
        }

        if (messageIn[11] == 1) {
            byte[] data = new byte[236];
            byte[] macToCompare = new byte[20];
            byte[] macInMessage = new byte[20];

            for (int c = 0; c < 236; c++) {
                data[c] = messageIn[c];
            }

            for (int c = 236; c < 256; c++) {
                macInMessage[c - 236] = messageIn[c];
            }

            try {
                macToCompare = SHA1(data);
            } catch (NoSuchAlgorithmException e) {
                System.out.println("Exception thrown for incorrect algorithm: " + e);
            }
            if (!Arrays.equals(macInMessage, macToCompare) && macInMessage != null) {
                return "";
            }

        }

        return message;
    }

    public static String getHellman(String message, char value) {
        String returnS = "";
        char[] arr = message.toCharArray();
        int index = 0;
        for (int c = 0; c < message.length(); c++) {
            if (arr[c] == value && arr[c + 1] == '=') {
                index = c + 2;
                break;
            }

        }
        for (int c = index; c < arr.length; c++) {
            if (arr[c] == ',') {
                break;
            }
            returnS = returnS + arr[c];
            System.out.println(c);
        }
        System.out.println("@getHellman->" + value + ": " + returnS);
        return returnS;

    }

    public static byte[] SHA1(byte[] arr) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        return md.digest(arr);
    }

    public static byte[] getByteArray(String message, byte function, boolean incorrectMAC) {
        char[] msg = message.toCharArray();
        byte[] arr = new byte[256];
        //ASCP
        arr[0] = 65;
        arr[1] = 83;
        arr[2] = 67;
        arr[3] = 80;
        //Version
        arr[4] = 0;
        arr[5] = 0;
        arr[6] = 0;
        arr[7] = 0;
        arr[8] = 1;
        //Size
        arr[9] = (byte) msg.length;
        //Function
        arr[10] = 0;
        arr[11] = function;
        //State
        arr[12] = 0;
        arr[13] = 0;
        arr[14] = 0;
        arr[15] = 0;
        //id_session
        arr[16] = 0;
        arr[17] = 0;
        arr[18] = 0;
        arr[19] = 0;
        //DATA
        for (int c = 20; c < 20 + msg.length; c++) {
            arr[c] = (byte) msg[c - 20];
        }
        //MAC
        if (function == 1 && !incorrectMAC) {
            byte[] data = new byte[236];
            byte[] mc = new byte[20];

            for (int c = 0; c < 236; c++) {
                data[c] = arr[c];
            }

            try {
                mc = SHA1(data);
                for (int c = 236; c < 256; c++) {
                    arr[c] = mc[c - 236];
                }
            } catch (NoSuchAlgorithmException e) {
                System.out.println("Exception thrown for incorrect algorithm: " + e);
            }
        }

        return arr;
    }

    public static String getMyIP() throws SocketException {
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netint : Collections.list(nets)) {
            Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
            for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                String add = inetAddress.toString();
                char[] arr = add.toCharArray();
                if (arr[1] == '1' && arr[2] == '7' && arr[3] == '2') {
                    return add;
                }

            }
        }

        return "didn't find a valid ip for tec's vpn";
    }
}
