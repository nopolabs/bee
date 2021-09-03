package com.nopolabs.bee;

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
public class Bee {
    public static void main(String[] args) {
        final String letters = args[0];
        try (final Stream<String> wordSource = WordSource.get()) {

            final Search search = new Search(wordSource, letters);
            final Words words = search.run();

            words.forEach(System.out::println);
            System.out.printf("found %d words worth %d points%n", words.size(), words.totalScore());
            System.out.printf("Dictionary contains %d words%n", search.getWordCount());

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
