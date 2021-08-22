package com.nopolabs.bee;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
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
        try (final Stream<String> wordSource = getWordSource("words_alpha.txt")) {

            final Search search = new Search(wordSource, letters);
            final Words words = search.run();

            words.forEach(System.out::println);
            System.out.printf("found %d words worth %d points%n", words.size(), words.totalScore());

        } catch (URISyntaxException | IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private static Stream<String> getWordSource(String name) throws URISyntaxException, IOException {
        final URI uri = Objects.requireNonNull(Bee.class.getClassLoader().getResource(name)).toURI();
        return Files.lines(Paths.get(uri));
    }
}
