package com.nopolabs.bee;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ToBee {
    public static void main(String[] args) throws IOException {

        List<String> wordList = WordSource.get(WordSource.DICT_WORDS_FILE).collect(Collectors.toList());

        AtomicInteger count = new AtomicInteger();

        Stream<String> stream = pangrams(wordList.stream())
                .flatMap(ToBee::permute)
                .distinct()
                .map(pangram -> {
                    System.out.println(count.incrementAndGet());
                    final Search search = new Search(wordList.stream(), pangram);
                    final Words words = search.run();
                    Info info = new Info(pangram, words.size(), words.totalScore());
                    return String.format("%s %5d %5d%n", info.pangram, info.words, info.points);
                });

        Files.write(Paths.get("/tmp/pangrams.txt"), (Iterable<String>)stream::iterator);
    }

    static class Info {
        final String pangram;
        final int words;
        final int points;

        Info(String pangram, int words, int points) {
            this.pangram = pangram;
            this.words = words;
            this.points = points;
        }
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

    private static Stream<String> pangrams(Stream<String> words) {
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
