package com.example.vault;

import java.util.HashMap;
import java.util.Map;

public class Encrypter {

    private static final int PIN_LIMIT = 4;
    private static final int ASCII_TABLE_SIZE = 128;

    public static int encryptPIN(String pin) {
        return (sumOfChars(pin) * Integer.parseInt(pin)) % 1000;
    }

    private static int sumOfChars(String pin) {
        int sum = 0;
        for (int i = 0; i < PIN_LIMIT; i++) {
            sum += Character.getNumericValue(pin.charAt(i));
        }
        return sum;
    }

    public static String encryptPassword(String pin, String password) {
        Map<Integer, Integer> encryptionMap = generateEncryptionMap(pin);
        return encryptStringWithMap(password, encryptionMap);
    }

    private static Map<Integer, Integer> generateEncryptionMap(String pin) {
        Map<Integer, Integer> map = new HashMap<>();
        int[] encryptedPIN = generatePinEncryptedAscii(pin);
        for (int i = 0; i < PIN_LIMIT; i++) {
            int ascii = encryptedPIN[i];      // assign ascii of char to int
            map.put(i, ascii);
        }
        for (int i = PIN_LIMIT; i < ASCII_TABLE_SIZE; i++) {
            for (int j = 0; j < ASCII_TABLE_SIZE; j++) {
                if (!map.containsValue(j)) {
                    map.put(i, j);
                    break;
                }
            }
        }
        return map;
    }

    private static int[] generatePinEncryptedAscii(String pin) {
        int[] encryptedAscii = new int[PIN_LIMIT];
        int pinSum = sumOfChars(pin);
        for (int i = 0; i < PIN_LIMIT; i++) {
            int pinDigit = Character.getNumericValue(pin.charAt(i));
            int asciiCode = (pinSum + pinDigit % ASCII_TABLE_SIZE);
            encryptedAscii[i] = asciiCode;
        }
        return encryptedAscii;
    }

    private static String encryptStringWithMap(String password, Map<Integer, Integer> encryptionMap) {
        StringBuilder encryptedPassword = new StringBuilder();
        for (int i = 0; i < password.length(); i++) {
            int ascii = password.charAt(i);                         // get ascii of password char
            int encryptedCharAscii = (encryptionMap.get(ascii));    // use map to get value for ascii
            char encryptedCharacter = (char) encryptedCharAscii;    // get char of ascii value returned
            encryptedPassword.append(encryptedCharacter);           // append the encrypted char
        }
        return encryptedPassword.toString();
    }

    public static String decryptPassword(String pin, String encryptedPassword) {
        StringBuilder decryptedPassword = new StringBuilder();
        Map<Integer, Integer> decryptionMap = invertMap(generateEncryptionMap(pin));
        for (int i = 0; i < encryptedPassword.length(); i++) {
            int ascii = encryptedPassword.charAt(i);                // get ascii of encrypted char
            int decryptedCharAscii = (decryptionMap.get(ascii));    // use map to get ascii of decrypted char
            char decryptedCharacter = (char) decryptedCharAscii;    // get char of decrypted ascii
            decryptedPassword.append(decryptedCharacter);           // append the decrypted char
        }
        return decryptedPassword.toString();
    }

    private static Map<Integer, Integer> invertMap(Map<Integer, Integer> map) {
        Map<Integer, Integer> invertedMap = new HashMap<>();
        for (Map.Entry<Integer, Integer> e : map.entrySet()) {
            invertedMap.put(e.getValue(), e.getKey());
        }
        return invertedMap;
    }

}
