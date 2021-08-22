package com.nopolabs.bee;

import java.util.HashMap;
import java.util.Map;

class Node {
    private final Map<Character, Node> children = new HashMap<>();

    private boolean isWord;

    Node insert(char letter) {
        return children.computeIfAbsent(letter, c -> new Node());
    }

    Node get(char letter) {
        return children.get(letter);
    }

    void setWord(boolean word) {
        isWord = word;
    }

    boolean isWord() {
        return isWord;
    }
}
