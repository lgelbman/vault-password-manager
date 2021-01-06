package com.example.vault.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Encrypter2 {

    private final int PIN_LIMIT = 4;
    private final int ASCII_TABLE_SIZE = 128;

    public int hashPin(String pin) {
        return sumOfChars(pin) * Integer.parseInt(pin) % 1000;
    }

    private int sumOfChars(String pin) {
        int acc = 0;
        for (int i = 0; i < pin.length(); i++) {
            acc += Character.getNumericValue(pin.charAt(i));
        }
        return acc;
    }

    public String encryptPassword(String pin, String password) {
        Map<Integer, Integer> map = generateEncryptionMap(pin);
        return encryptStringUsingMap(password, map);
    }


    private String encryptStringUsingMap(String password, Map<Integer, Integer> map) {
        String result = "";
        for (int i = 0; i < password.length(); i++) {
            int numericValueOfChar = Character.getNumericValue(password.charAt(i));
            int encryptedChar = map.get(numericValueOfChar);
            String encryptedCharAsString = Character.toString( (char) encryptedChar);
            result += encryptedCharAsString;
        }
        return result;
    }

    public String decrypt(String pin, String password){
        Map<Integer, Integer> map = generateEncryptionMap(pin);

    }

    private Map<Integer, Integer> generateEncryptionMap(String pin) {
        Map<Integer, Integer> map = new HashMap<>();
        int[] asciiPin = asciiPin(pin);
        for (int i = 0; i < asciiPin.length; i++) {
            int asciiCode = asciiPin[i];
            map.put(i, asciiCode);
        }
        for (int i = asciiPin.length; i < ASCII_TABLE_SIZE; i++) {
            for (int j = 0; j < ASCII_TABLE_SIZE; j++){
                if (!map.containsValue(j)) {
                    map.put(i, j);
                    break;
                }
            }
        }
        return map;
    }

    private Map<Integer, Integer> invertMap(Map<Integer, Integer> map) {
        Map<Integer, Integer> result = new HashMap<>();
        for (Map.Entry<Integer, Integer> e : map.entrySet()) {
            result.put(e.getValue(), e.getKey());
        }
        return result;
    }

    private int[] asciiPin(String pin) {
        int[] asciiArray = new int[PIN_LIMIT];
        int sum = sumOfChars(pin);
        for (int i = 0; i < PIN_LIMIT; i++) {
            int asciiValue = ( sum + Character.getNumericValue(pin.charAt(i)) % ASCII_TABLE_SIZE);
            asciiArray[i] = asciiValue;
        }
        return asciiArray;
    }


}
