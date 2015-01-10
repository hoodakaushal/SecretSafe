package com.sakhuja.ayush.secretsafe;

import com.tiemens.secretshare.main.cli.MainSplit;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Created by me on 1/7/2015.
 */


public class a {
    public static void main(String[] args){
        String s = "test message";
        String k = "1";
        String n = "2";
        String[] splitArgs = {"-n", n, "-k", k, "-sS", s};
        MainSplit.SplitInput splitInput = MainSplit.SplitInput.parse(splitArgs);
        MainSplit.SplitOutput splitOutput = splitInput.output();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        splitOutput.print(ps);
        String content = baos.toString(); // e.g. ISO-8859-1
        System.out.println(content);


    }


}
