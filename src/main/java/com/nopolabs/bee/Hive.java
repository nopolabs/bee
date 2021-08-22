package com.nopolabs.bee;

class Hive {

    private final String centerLetter;
    private final char[] chars;

    Hive(String letters) {
        this.chars = letters.toCharArray();
        this.centerLetter = String.valueOf(chars[0]);
    }

    String getCenterLetter() {
        return centerLetter;
    }

    char[] getChars() {
        return chars;
    }

    boolean isPangram(String word) {
        for (char letter: chars) {
            if (!word.contains(String.valueOf(letter))) {
                return false;
            }
        }
        return true;
    }
}
