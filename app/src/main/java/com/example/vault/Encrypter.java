package com.example.vault;

import java.util.HashMap;
import java.util.Map;

public class Encrypter {

    public int hashPin(String pin) {
        return sumOfChars(pin) * Integer.parseInt(pin) % 1000;
    }


    public String encryptPassword(String pin, String password) {
        Map<String, String> map = generateEncryptionMap(pin);
        return getStringUsingMap(map, password);
    }

    private Map<String, String> generateEncryptionMap(String pin) {
        Map<String, String> map = new HashMap<>();
        String lettersSeq = pinToLetters(pin);
        for (int i = 0; i < lettersSeq.length(); i++) {
            map.put(Integer.toString(i), String.valueOf(lettersSeq.charAt(i)));
        }
        for ( int i = lettersSeq.length(); i < 26; i++) {
            for (int j = 0; j < 26; j++) {
                if (!map.containsValue(Integer.toString(j))) {
                    map.put(Integer.toString(i), Integer.toString(j));
                    break;
                }
            }
        }
        return map;
    }

    private String getStringUsingMap(Map<String, String> map, String stringToEncrypt) {
        String result = "";
        for (int i = 0; i < stringToEncrypt.length() - 1; i++) {
            String charToAdd = map.get(String.valueOf(stringToEncrypt.charAt(i)));
            result += charToAdd;
        }
        return result;
    }

    private String pinToLetters(String pin) {
        int sum = sumOfChars(pin);
        String result = "";
        for (int i = 0; i < pin.length(); i++) {
            int alphabeticIndex = ( sum + Character.getNumericValue(pin.charAt(i)) % 25);
            String characterToAppend = Integer.toString(alphabeticIndex);
            result += characterToAppend;
        }
        return result;
    }



    private int sumOfChars(String pin) {
        int acc = 0;
        for (int i = 0; i < pin.length(); i++) {
            acc += Character.getNumericValue(pin.charAt(i));
        }
        return acc;
    }

    public String decryptPassword(String pin, String hashedPassword) {
        Map<String, String> map = generateEncryptionMap(pin);
        map = getInverseEncryptionMap(map);
        String decryptedPassword = "";
        for (int i = 0; i < hashedPassword.length(); i++) {
            String letter = String.valueOf(hashedPassword.charAt(i));
            String decryptedLetter = map.get(letter);
            decryptedPassword += decryptedLetter;
        }
        return decryptedPassword;
    }

    private Map<String, String> getInverseEncryptionMap(Map<String, String> map) {
        Map<String, String> inverseMap = new HashMap<>();
        for (Map.Entry<String, String> e : map.entrySet()){
            inverseMap.put(e.getValue(), e.getKey());
        }
        return inverseMap;
    }

    private String getLetterFromIndex(int index) {
        return Character.toString ((char) (index + 65));
    }


}
