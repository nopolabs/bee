package com.nopolabs.bee;

class Word {

    private final String word;
    private final boolean isPangram;

    Word(String word, boolean isPangram) {
        this.word = word;
        this.isPangram = isPangram;
    }

    int getScore() {
        return lengthPoints() + bonusPoints();
    }

    String getWord() {
        return word;
    }

    private int lengthPoints() {
        return word.length() <= 4 ? 1 : word.length();
    }

    private int bonusPoints() {
        return isPangram ? 7 : 0;
    }

    @Override
    public String toString() {
        if (isPangram) {
            return word + " *";
        }
        return word;
    }
}
