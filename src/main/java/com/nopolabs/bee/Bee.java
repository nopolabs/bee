package com.nopolabs.bee;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Bee {

    public static void main(String[] args) throws Exception {
        final String letters = args[0];
        final String[] allLetters = letters.split("");
        final String centerLetter = allLetters[0];
        final Trie words = getWords("words_alpha.txt", centerLetter);
        final List<String> found = search(words.root, letters.toCharArray(), "", new ArrayList<>())
                .stream()
                .sorted()
                .collect(Collectors.toList());
        found.forEach(word -> System.out.printf("%s%s%n", word, contains(word, allLetters) ? " *" : ""));
        System.out.printf("found %d words%n", found.size());
    }

    private static boolean contains(String word, String[] allLetters) {
        for (String letter: allLetters) {
            if (!word.contains(letter)) {
                return false;
            }
        }
        return true;
    }

    private static List<String> search(Trie.Node parent, char[] letters, String prefix, List<String> found) {
        for (char ch : letters) {
            Trie.Node child = parent.children.get(ch);
            if (child != null) {
                String candidate = prefix + ch;
                if (child.isWord) {
                    found.add(candidate);
                }
                found = search(child, letters, candidate, found);
            }
        }
        return found;
    }
    
    private static Trie getWords(String name, String letter) throws Exception {
        Trie trie = new Trie();
        try (Stream<String> words = Files.lines(Paths.get(toURI(name)))) {
            words.filter(w -> w.length() >= 4)
                    .filter(w -> w.contains(letter))
                    .forEach(trie::insert);
        }
        return trie;
    }

    private static URI toURI(String name) throws Exception {
        return Objects.requireNonNull(Bee.class.getClassLoader().getResource(name)).toURI();
    }

    private static class Trie {

        private final Trie.Node root = new Trie.Node();

        public void insert(String word) {
            Trie.Node current = root;

            for (char l: word.toCharArray()) {
                current = current.children.computeIfAbsent(l, c -> new Trie.Node());
            }

            current.isWord = true;
        }

        private static class Node {
            private final Map<Character, Trie.Node> children = new HashMap<>();
            private boolean isWord;
        }
    }
}
