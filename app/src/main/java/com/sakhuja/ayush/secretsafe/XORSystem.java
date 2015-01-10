package com.sakhuja.ayush.secretsafe;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hooda on 1/10/2015.
 */
public class XORSystem {

    public static void main(String[] args) throws Exception {
        XORSystem xorSystem = new XORSystem();

        String text = "Mr. and Mrs. Dursley, of number four Privet Drive, were proud to say that they were perfectly normal, thank you very much.";
        String[] pieces = xorSystem.encrypt(text, 5);
        System.out.println(pieces[0]);
        System.out.println(pieces[1]);
        System.out.println(pieces[2]);
        System.out.println(pieces[3]);
        System.out.println(pieces[4]);
        String test = xorSystem.decrypt(pieces);
        System.out.println(test);

    }

    public String[] encrypt(String s, int n) throws Exception {
        int length = s.length();
        String[] pieces = new String[n];

        for (int i = 0; i < n - 1; i++) {
            pieces[i] = keyGen(length);
        }

        String nth = s;
        for (int i = 0; i < n - 1; i++) {
            nth = doXOR(pieces[i], nth);
        }

        pieces[n - 1] = nth;
        return pieces;
    }

    public String decrypt(String[] pieces) {

        String decrypted = new String(new char[pieces[0].length()]).replace("\0", Character.toString((char) 0));
        for (String piece : pieces) {
            decrypted = doXOR(piece, decrypted);
        }
//        String cleartext = toString(decrypted);
        return decrypted;
    }

    public String toBinary(String s) throws Exception {
        byte[] bytes = s.getBytes("ASCII");
        StringBuilder binary = new StringBuilder();
        for (byte b : bytes) {
            int val = b;
            for (int i = 0; i < 8; i++) {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
        }
        return binary.toString();
    }

    private String toString(String bits) {
        List<String> pieces = splitEqually(bits, 8);
        String ret = "";

        for (String piece : pieces) {
            int charCode = Integer.parseInt(piece, 2);
            String str = new Character((char) charCode).toString();
            ret = ret.concat(str);
        }
        return ret;
    }

    public String keyGen(int length) throws UnsupportedEncodingException {
        SecureRandom srand = new SecureRandom();
        byte[] bytes = new byte[length];
        srand.nextBytes(bytes);
        return new String(bytes, "ASCII");
    }

    public String doXOR(String s1, String s2) throws IllegalArgumentException {

        String ret;
        if (s1.length() != s2.length()) {
            throw new IllegalArgumentException("The bit strings must have equal length!");
        } else {
            ret = "";
            for (int i = 0; i < s1.length(); i++) {
                char c1 = s1.charAt(i);
                char c2 = s2.charAt(i);
                int i1 = (int) c1;
                int i2 = (int) c2;
                int i3 = i1 ^ i2;
                char c3 = (char) i3;
                ret += c3;
            }
        }
        return ret;
    }

    public List<String> splitEqually(String text, int size) {
        List<String> ret = new ArrayList<String>((text.length() + size - 1) / size);

        for (int start = 0; start < text.length(); start += size) {
            ret.add(text.substring(start, Math.min(text.length(), start + size)));
        }
        return ret;
    }
}
