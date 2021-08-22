package com.nopolabs.bee;

import java.util.stream.Stream;

public class Search {

    private final Hive hive;
    private final Dictionary dictionary;

    Search(Stream<String> wordSource, String letters) {
        if (letters.length() != 7) {
            throw new IllegalArgumentException("game requires exactly 7 letters");
        }
        this.hive = new Hive(letters);
        this.dictionary = Dictionary.builder()
                .from(wordSource)
                .containing(hive.getCenterLetter())
                .build();
    }

    public Words run() {
        return search(dictionary.getRoot(), hive);
    }

    private Words search(Node root, Hive hive) {
        return search(root, hive, "", new Words());
    }

    private Words search(Node parent, Hive hive, String prefix, Words words) {
        for (char letter : hive.getChars()) {
            Node child = parent.get(letter);
            if (child != null) {
                String current = prefix + letter;
                if (child.isWord()) {
                    words.add(current, hive.isPangram(current));
                }
                words = search(child, hive, current, words);
            }
        }
        return words;
    }
}
