package com.nopolabs.bee;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
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

        private URI wordsSource;
        private String centerLetter;

        Builder from(URI wordsSource) {
            this.wordsSource = wordsSource;
            return this;
        }

        Builder containing(String centerLetter) {
            this.centerLetter = centerLetter;
            return this;
        }

        Dictionary build() throws IOException {
            final Node root = new Node();

            try (Stream<String> words = Files.lines(Paths.get(wordsSource))) {
                words.filter(word -> word.length() >= 4)
                        .filter(word -> word.contains(centerLetter))
                        .forEach(word -> {
                            Node current = root;
                            for (char letter : word.toCharArray()) {
                                current = current.insert(letter);
                            }

                            current.setWord(true);
                        });
            }

            return new Dictionary(root);
        }
    }
}
