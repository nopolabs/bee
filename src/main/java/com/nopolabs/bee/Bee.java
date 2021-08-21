package com.nopolabs.bee;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class Bee {

    public static void main(String[] args) throws Exception {
        Trie words = getWords("words_alpha.txt");
        String letters = args[0];

        List<String> found = search(words.root, letters.toCharArray(), new ArrayList<>());

        found.stream()
                .filter(w -> w.contains(letters.substring(0, 1)))
                .forEach(System.out::println);
    }

    private static List<String> search(Trie.Node parent, char[] letters, List<String> found) {
        for (char ch : letters) {
            Trie.Node child = parent.children.get(ch);
            if (child != null) {
                if (child.word != null) {
                    found.add(child.word);
                }
                found = search(child, letters, found);
            }
        }
        return found;
    }
    
    private static Trie getWords(String name) throws Exception {
        Trie trie = new Trie();
        try (Stream<String> words = Files.lines(Paths.get(toURI(name)))) {
            words.filter(w -> w.length() >= 4)
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

            current.word = word;
        }

        public boolean find(String word) {
            Trie.Node current = root;

            for (int i = 0; i < word.length(); i++) {
                char ch = word.charAt(i);
                Trie.Node node = current.children.get(ch);
                if (node == null) {
                    return false;
                }
                current = node;
            }

            return current.word != null;
        }

        private static class Node {
            private final Map<Character, Trie.Node> children = new HashMap<>();
            private String word;
        }
    }
}
