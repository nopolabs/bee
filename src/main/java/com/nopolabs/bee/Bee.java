package com.nopolabs.bee;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
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

    public static String WORDS_ALPHA_RESOURCE = "words_alpha.txt";
    public static String DICT_WORDS_FILE = "/usr/share/dict/words";

    public static void main(String[] args) {
        final String letters = args[0];
        try (final Stream<String> wordSource = getWordSource(WORDS_ALPHA_RESOURCE, DICT_WORDS_FILE)) {

            final Search search = new Search(wordSource, letters);
            final Words words = search.run();

            words.forEach(System.out::println);
            System.out.printf("found %d words worth %d points%n", words.size(), words.totalScore());
            System.out.printf("Dictionary contains %d words%n", search.getWordCount());

        } catch (URISyntaxException | IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private static Stream<String> getWordSource(String... names) throws URISyntaxException, IOException {
        return Arrays.stream(names)
                .map(name -> {
                    URL url = Bee.class.getClassLoader().getResource(name);
                    return (url != null) ? url : fileUrl(name);
                })
                .filter(Objects::nonNull)
                .map(Bee::toURI)
                .filter(Objects::nonNull)
                .map(Bee::lines)
                .filter(Objects::nonNull)
                .flatMap(Function.identity())
                .sorted()
                .distinct();
    }

    private static Stream<String> lines(URI uri) {
        try {
            return Files.lines(Paths.get(uri));
        } catch (IOException e) {
            return null;
        }
    }

    private static URI toURI(URL url) {
        try {
            return url.toURI();
        } catch (URISyntaxException e) {
            return null;
        }
    }

    private static URL fileUrl(String name) {
        try {
            return new URL("file://" + name);
        } catch (MalformedURLException e) {
            return null;
        }
    }
}
