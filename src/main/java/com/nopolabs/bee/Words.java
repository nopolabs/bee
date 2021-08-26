package com.nopolabs.bee;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Words {

    private final ArrayList<Word> words = new ArrayList<>();

    public void addAll(Words other) {
        words.addAll(other.asList());
    }

    void add(String word, boolean isPangram) {
        words.add(new Word(word, isPangram));
    }

    int size() {
        return words.size();
    }

    int totalScore() {
        return words.stream()
                .map(Word::getScore)
                .reduce(0, Integer::sum);
    }

    Stream<Word> asStream() {
        return words.stream()
                .sorted();
    }

    void forEach(Consumer<? super Word> action) {
        asStream()
                .forEach(action);
    }

    List<Word> asList() {
        return asStream()
                .collect(Collectors.toList());
    }

    List<String> all() {
        return asStream()
                .map(Word::getWord)
                .collect(Collectors.toList());
    }
}
