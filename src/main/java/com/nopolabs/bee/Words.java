package com.nopolabs.bee;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

class Words {

    private final ArrayList<Word> words = new ArrayList<>();

    void add(String word, boolean isPangram) {
        words.add(new Word(word, isPangram));
    }

    int size() {
        return words.size();
    }

    void forEach(Consumer<? super Word> action) {
        words.forEach(action);
    }

    int totalScore() {
        return words.stream()
                .map(Word::getScore)
                .reduce(0, Integer::sum);
    }

    List<Word> allWords() {
        return new ArrayList<>(words);
    }

    List<String> all() {
        return words.stream()
                .map(Word::getWord)
                .collect(Collectors.toList());
    }
}
