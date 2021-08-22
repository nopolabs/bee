package com.nopolabs.bee;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SearchTest {

    @Test
    void test_too_short() {
        Stream<String> wordSource = Stream.of("aaa");
        String letters = "aeioust";

        Words words = new Search(wordSource, letters).run();

        assertEquals(0, words.size());
        assertTrue(words.all().isEmpty());
        assertEquals(0, words.totalScore());
    }

    @Test
    void test_missing_center_letter() {
        Stream<String> wordSource = Stream.of("site");
        String letters = "aeioust";

        Words words = new Search(wordSource, letters).run();

        assertEquals(0, words.size());
        assertTrue(words.all().isEmpty());
        assertEquals(0, words.totalScore());
    }

    @Test
    void test_four_letter_word() {
        Stream<String> wordSource = Stream.of("auto");
        String letters = "aeioust";

        Words words = new Search(wordSource, letters).run();

        assertEquals(1, words.size());
        assertTrue(words.all().contains("auto"));
        assertTrue(words.allWords().stream()
                .anyMatch(w -> w.toString().equals("auto")));
        assertEquals(1, words.totalScore());
    }

    @Test
    void test_longer_than_four_letter_word() {
        Stream<String> wordSource = Stream.of("tautoousious");
        String letters = "aeioust";

        Words words = new Search(wordSource, letters).run();

        assertEquals(1, words.size());
        assertTrue(words.all().contains("tautoousious"));
        assertTrue(words.allWords().stream()
                .anyMatch(w -> w.toString().equals("tautoousious")));
        assertEquals(12, words.totalScore());
    }

    @Test
    void test_pangram() {
        Stream<String> wordSource = Stream.of("autosite");
        String letters = "aeioust";

        Words words = new Search(wordSource, letters).run();

        assertEquals(1, words.size());
        assertTrue(words.all().contains("autosite"));
        assertTrue(words.allWords().stream()
                .anyMatch(w -> w.toString().equals("autosite *")));
        assertEquals(15, words.totalScore());
    }

    @Test
    void test_multiple_words() {
        Stream<String> wordSource = Stream.of(
                "aaa", // too short
                "auto",
                "site", // no 'a'
                "autosite",
                "tautoousious"
        );
        String letters = "aeioust";

        Words words = new Search(wordSource, letters).run();

        assertEquals(3, words.size());
        assertTrue(words.all().containsAll(Arrays.asList(
                "auto",
                "autosite",
                "tautoousious"
        )));
        assertEquals(28, words.totalScore());
    }

    @Test
    void test_words_alpha() throws IOException, URISyntaxException {
        URI uri = Objects.requireNonNull(Bee.class.getClassLoader().getResource("words_alpha.txt")).toURI();
        Stream<String> wordSource = Files.lines(Paths.get(uri));
        String letters = "aeioust";

        Words words = new Search(wordSource, letters).run();

        assertEquals(257, words.size());
        assertTrue(words.all().containsAll(Arrays.asList(
                "auto",
                "autosite",
                "tautoousious"
        )));
        assertTrue(words.all().stream()
                .allMatch(w -> w.length() >= 4));
        assertTrue(words.allWords().stream()
                .map(Word::toString)
                .filter(w -> w.endsWith(" *"))
                .allMatch(w -> w.equals("autosite *")));
        assertEquals(1334, words.totalScore());
    }
}
