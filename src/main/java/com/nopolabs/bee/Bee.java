package com.nopolabs.bee;

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
public class Bee {
    public static void main(String[] args) throws Exception {
        final String letters = args[0];
        final URI uri = Objects.requireNonNull(Bee.class.getClassLoader().getResource("words_alpha.txt")).toURI();
        final Hive hive = new Hive(letters);
        final Dictionary dictionary = Dictionary.builder()
                .from(uri)
                .containing(hive.getCenterLetter())
                .build();;
        final Search search = new Search(dictionary, hive);

        final Words words = search.run();

        words.forEach(System.out::println);
        System.out.printf("found %d words worth %d points%n", words.size(), words.totalScore());
    }
}
