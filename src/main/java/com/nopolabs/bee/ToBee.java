package com.nopolabs.bee;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ToBee {
    public static void main(String[] args) throws URISyntaxException, IOException {

        List<String> wordList = WordSource.get(WordSource.DICT_WORDS_FILE).collect(Collectors.toList());

        List<String> pangrams = pangrams(wordList.stream()).collect(Collectors.toList());

        pangrams.stream()
                .flatMap(ToBee::permute)
                .forEach(pangram -> {
                    final Search search = new Search(wordList.stream(), pangram);
                    final Words words = search.run();
                    System.out.printf("%s found %d words worth %d points%n", pangram, words.size(), words.totalScore());
                });
    }

    private static Stream<String> permute(String p) {
        return Stream.of(
             p,
             p.substring(1, 7) + p.charAt(0),
             p.substring(2, 7) + p.substring(0, 2),
             p.substring(3, 7) + p.substring(0, 3),
             p.substring(4, 7) + p.substring(0, 4),
             p.substring(5, 7) + p.substring(0, 5),
             p.charAt(6) + p.substring(0, 6)
        );
    }

    private static Stream<String> pangrams(Stream<String> words) throws URISyntaxException, IOException {
        return words
                .filter(w -> w.length() >= 7)
                .map(String::toLowerCase)
                .map(w -> w.chars()
                        .mapToObj(Character::toString)
                        .collect(Collectors.toCollection(TreeSet::new)))
                .filter(s -> s.size() == 7)
                .map(s -> String.join("", s))
                .sorted()
                .distinct();
    }
}
