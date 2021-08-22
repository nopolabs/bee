package com.nopolabs.bee;

import java.io.IOException;
import java.net.URI;
import java.util.Objects;

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
        final Words words = search(dictionary.getRoot(), hive);
        words.forEach(System.out::println);
        System.out.printf("found %d words worth %d points%n", words.size(), words.totalScore());
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
