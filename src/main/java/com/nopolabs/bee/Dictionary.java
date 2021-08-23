package com.nopolabs.bee;

import java.util.stream.Stream;

class Dictionary {

    private final Node root;

    Dictionary(Node root) {
        this.root = root;
    }

    static Builder builder() {
        return new Builder();
    }

    Node getRoot() {
        return root;
    }

    static class Builder {

        private Stream<String> wordsSource;
        private String centerLetter;

        Builder from(Stream<String> wordsSource) {
            this.wordsSource = wordsSource;
            return this;
        }

        Builder containing(String centerLetter) {
            this.centerLetter = centerLetter;
            return this;
        }

        Dictionary build() {
            final Node root = new Node();

            wordsSource.filter(word -> word.length() >= 4)
                    .map(String::toLowerCase)
                    .filter(word -> word.contains(centerLetter))
                    .forEach(root::add);

            return new Dictionary(root);
        }
    }
}
