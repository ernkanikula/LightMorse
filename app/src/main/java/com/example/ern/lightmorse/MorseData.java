package com.example.ern.lightmorse;

import java.util.HashMap;


/**
 * Created by ern on 02.03.16.
 */
public class MorseData {

    public static HashMap getHashMap() {
        HashMap<String, String> codeMap = new HashMap<String, String>();
        codeMap.put("A",".-");
        codeMap.put("B","-");
        codeMap.put("C","-.-.");
        codeMap.put("D","-..");
        codeMap.put("E",".");
        codeMap.put("F","..-.");
        codeMap.put("G","--.");
        codeMap.put("H","....");
        codeMap.put("I","..");
        codeMap.put("J",".---");
        codeMap.put("K","-.-");
        codeMap.put("L",".-..");
        codeMap.put("M","--");
        codeMap.put("N","-.");
        codeMap.put("O","---");
        codeMap.put("P",".--.");
        codeMap.put("Q","--.-");
        codeMap.put("R",".-.");
        codeMap.put("S","...");
        codeMap.put("T","-");
        codeMap.put("U","..");
        codeMap.put("V","...-");
        codeMap.put("W",".--");
        codeMap.put("X","-..-");
        codeMap.put("Y","-.--");
        codeMap.put("Z","--..");

        codeMap.put("ą",".-.-");
        codeMap.put("ć","-.-..");
        codeMap.put("ę","..-..");
        codeMap.put("ł",".-..-");
        codeMap.put("ń","--.--");
        codeMap.put("ó","---.");
        codeMap.put("ś","...-...");
        codeMap.put("ż","--..-.");
        codeMap.put("ź","--..- ");

        codeMap.put("1",".----");
        codeMap.put("2","..---");
        codeMap.put("3","...--");
        codeMap.put("4","....-");
        codeMap.put("5",".....");
        codeMap.put("6","-....");
        codeMap.put("7","--...");
        codeMap.put("8","---..");
        codeMap.put("9","----.");
        codeMap.put("0","-----");

        codeMap.put(".",".-.-.-");
        codeMap.put(",","--..--");
        codeMap.put("'",".----.");
        codeMap.put("_","..--.-");
        codeMap.put(":","---...");
        codeMap.put("?","..--..");
        codeMap.put("-","-....-");
        codeMap.put("/","-..-.");
        codeMap.put("(","-.--.");
        codeMap.put(")","-.--.-");
        codeMap.put("@",".--.-.");

        return codeMap;
    }


}
