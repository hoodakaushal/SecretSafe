package com.sakhuja.ayush.secretsafe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by hooda on 1/10/2015.
 */
public class XORSystem {

    public static void main(String[] args) throws Exception {
        XORSystem xorSystem = new XORSystem();
        String text = "Mr. and Mrs. Dursley, of number four Privet Drive, were proud to say that they were perfectly normal, thank you very much.";

        String[] pieces = xorSystem.encrypt(text);
        System.out.println(pieces[0]);
        System.out.println(pieces[1]);

        String test = xorSystem.decrypt(pieces[1], pieces[0]);
        System.out.println(test);
    }

    public String[] encrypt(String s) throws Exception {
        String bits = toBinary(s);
        String bits1 = keyGen(bits.length());
        String bits2 = doXOR(bits, bits1);
        String[] pieces = {bits1, bits2};
        return pieces;
    }

    public String decrypt(String a, String b) {
        String decrypted = doXOR(a, b);
        String cleartext = toString(decrypted);
        return cleartext;
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

    public String keyGen(int length) {
        String ret = "";
        Random rand = new Random();
        for (int i = 0; i < length; i++) {
            if (rand.nextBoolean()) {
                ret = ret.concat("1");
            } else {
                ret = ret.concat("0");
            }
        }
        return ret;
    }

    public String doXOR(String s1, String s2) throws IllegalArgumentException {

        String ret;
        if (s1.length() != s2.length()) {
            throw new IllegalArgumentException("The bit strings must have equal length!");
        } else {
            ret = "";
            for (int i = 0; i < s1.length(); i++) {
                if (s1.charAt(i) == s2.charAt(i)) {
                    ret = ret.concat("0");
                } else {
                    ret = ret.concat("1");
                }
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
