package com.sakhuja.ayush.secretsafe;

import java.io.BufferedReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import com.tiemens.secretshare.main.cli.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ayush on 1/10/2015.
 */
public class SplitCombine {

    public static ArrayList<String> shamirSplit(String in, int n, int k) {
        ArrayList<String> parts = new ArrayList<>();
        String[] splitArgs = {"-n", Integer.toString(n), "-k", Integer.toString(k), "-sS", in};
        MainSplit.SplitInput splitInput = MainSplit.SplitInput.parse(splitArgs);
        MainSplit.SplitOutput splitOutput = splitInput.output();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        splitOutput.print(ps);
        String content = baos.toString(); // e.g. ISO-8859-1
        BufferedReader reader = new BufferedReader(new StringReader(content));
        String line;
        int i=0;
        try{
            while((line = reader.readLine()) != null && i<n){
                if (line.startsWith("Share (x")){
                    i++;
                    parts.add((line.split("=")[1].trim()));
                }
            }
        }
        catch (Exception e){
            //TODO Catch
        }
        return parts;
    }

    public static String shamirCombine (ArrayList<String> parts, ArrayList<String> flags, int k){
        ArrayList<String> args = new ArrayList<>();
        args.add("-k");
        args.add(Integer.toString(k));
        for (int i=0;i<flags.size();i++){
            args.add(flags.get(i));
            args.add(parts.get(i));
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        String[] combineArgs = args.toArray(new String[args.size()]);
        MainCombine.CombineInput combineInput = MainCombine.CombineInput.parse(combineArgs,null,ps);
        MainCombine.CombineOutput combineOutput = combineInput.output();
        combineOutput.print(ps);
        String content = baos.toString(); // e.g. ISO-8859-1
        Pattern pattern = Pattern.compile("secret.string = '");
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()){
            return (content.substring(matcher.end(), content.length()-2));
        }
        else{
            return "";
        }
    }

    public static ArrayList<String> split(String s, int n) {
        int length = s.length();
        String[] pieces = new String[n];

        for (int i = 0; i < n - 1; i++) {
            try {
                pieces[i] = keyGen(length);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        String nth = s;
        for (int i = 0; i < n - 1; i++) {
            nth = doXOR(pieces[i], nth);
        }

        pieces[n - 1] = nth;
        return new ArrayList(Arrays.asList(pieces));
    }

    public static String combine(ArrayList<String> parts) {
        String[] pieces = parts.toArray(new String[parts.size()]);
        String decrypted = new String(new char[pieces[0].length()]).replace("\0", Character.toString((char) 0));
        for (String piece : pieces) {
            decrypted = doXOR(piece, decrypted);
        }
        return decrypted;
    }



    public static String keyGen(int length) throws UnsupportedEncodingException {
        Random rand = new Random();
        byte[] bytes = new byte[length];
        rand.nextBytes(bytes);
        return new String(bytes, "ASCII");
    }

    public static String doXOR(String s1, String s2) throws IllegalArgumentException {

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
}
