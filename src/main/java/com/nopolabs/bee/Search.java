package com.nopolabs.bee;

public class Search {

    private final Hive hive;
    private final Dictionary dictionary;

    Search(Dictionary dictionary, Hive hive) {
        this.hive = hive;
        this.dictionary = dictionary;
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
