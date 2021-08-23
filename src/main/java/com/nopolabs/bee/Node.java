package com.nopolabs.bee;

import java.util.HashMap;
import java.util.Map;

class Node {
    private final Map<Character, Node> children = new HashMap<>();

    private boolean isWord;

    Node getChild(char letter) {
        return children.get(letter);
    }

    void setWord(boolean word) {
        isWord = word;
    }

    boolean isWord() {
        return isWord;
    }

    void add(String word) {
        Node node = this;
        for (char letter : word.toCharArray()) {
            node = node.insert(letter);
        }
        node.setWord(true);
    }

    private Node insert(char letter) {
        return children.computeIfAbsent(letter, c -> new Node());
    }
}
