package com.nopolabs.bee;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * How to Play Spelling Bee
 * - Create words using letters from the hive.
 * - Words must contain at least 4 letters.
 * - Words must include the center letter.
 * - Our word list does not include words that are obscure, hyphenated, or proper nouns.
 * - No cussing either, sorry.
 * - Letters can be used more than once.
 * Score points to increase your rating.
 * - 4-letter words are worth 1 point each.
 * - Longer words earn 1 point per letter.
 * - Each puzzle includes at least one “pangram” which uses every letter. These are worth 7 extra points!
 */
public class Bee implements Runnable {

    public static void main(String[] args) throws Exception {
        final String letters = args[0];
        final URI uri = Objects.requireNonNull(Bee.class.getClassLoader().getResource("words_alpha.txt")).toURI();
        final Bee bee = new Bee(letters, uri);

        bee.run();
    }

    private final Hive hive;
    private final Dictionary dictionary;

    private Bee(String letters, URI wordsSource) throws IOException {
        this.hive = new Hive(letters);
        this.dictionary = Dictionary.builder()
                .from(wordsSource)
                .containing(hive.getCenterLetter())
                .build();
    }

    @Override
    public void run() {
        final Words words = search(dictionary.root, hive.chars);
        words.forEach(System.out::println);
        System.out.printf("found %d words worth %d points%n", words.size(), words.totalScore());
    }

    private Words search(Node root, char[] chars) {
        return search(root, chars, "", new Words());
    }

    private Words search(Node parent, char[] chars, String prefix, Words words) {
        for (char letter : chars) {
            Node child = parent.get(letter);
            if (child != null) {
                String candidate = prefix + letter;
                if (child.isWord) {
                    words.add(candidate, isPangram(candidate, chars));
                }
                words = search(child, chars, candidate, words);
            }
        }
        return words;
    }

    private boolean isPangram(String word, char[] chars) {
        for (char letter: chars) {
            if (!word.contains(String.valueOf(letter))) {
                return false;
            }
        }
        return true;
    }

    private static class Word {

        private final String word;
        private final boolean isPangram;

        private Word(String word, boolean isPangram) {
            this.word = word;
            this.isPangram = isPangram;
        }

        private int getScore() {
            return (word.length() <= 4 ? 1 : word.length()) + (isPangram ? 7 : 0);
        }

        @Override
        public String toString() {
            if (isPangram) {
                return word + " *";
            }
            return  word;
        }
    }

    private static class Words {

        private final ArrayList<Word> words = new ArrayList<>();

        private void add(String word, boolean isPangram) {
            words.add(new Word(word, isPangram));
        }

        private int size() {
            return words.size();
        }

        private void forEach(Consumer<? super Word> action) {
            words.forEach(action);
        }

        public int totalScore() {
            return words.stream()
                    .map(Word::getScore)
                    .reduce(0, Integer::sum);
        }
    }

    private static class Dictionary {

        private Node root;

        private Dictionary(Node root) {
            this.root = root;
        }

        private static Builder builder() {
            return new Builder();
        }

        private static class Builder {

            private URI wordsSource;
            private String centerLetter;

            private Builder from(URI wordsSource) {
                this.wordsSource = wordsSource;
                return this;
            }

            private Builder containing(String centerLetter) {
                this.centerLetter = centerLetter;
                return this;
            }

            private Dictionary build() throws IOException {
                final Node root = new Node();

                try (Stream<String> words = Files.lines(Paths.get(wordsSource))) {
                    words.filter(word -> word.length() >= 4)
                            .filter(word -> word.contains(centerLetter))
                            .forEach(word -> {
                                Node current = root;
                                for (char letter: word.toCharArray()) {
                                    current = current.insert(letter);
                                }

                                current.isWord = true;
                            });
                }

                return new Dictionary(root);
            }
        }
    }

    private static class Hive {

        private final String[] letters;
        private final char[] chars;

        private Hive(String letters) {
            this.letters = letters.split("");
            this.chars = letters.toCharArray();
        }

        private String getCenterLetter() {
            return letters[0];
        }
    }

    private static class Node {
        private final Map<Character, Node> children = new HashMap<>();
        private boolean isWord;

        private Node insert(char letter) {
            return children.computeIfAbsent(letter, c -> new Node());
        }

        private Node get(char letter) {
            return children.get(letter);
        }
    }
}
