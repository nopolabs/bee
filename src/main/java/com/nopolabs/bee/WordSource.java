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

public class WordSource {

    public static String WORDS_ALPHA_RESOURCE = "words_alpha.txt";
    public static String DICT_WORDS_FILE = "/usr/share/dict/words";

    public static Stream<String> get() {
        return get(WORDS_ALPHA_RESOURCE, DICT_WORDS_FILE);
    }

    public static Stream<String> get(String... names) {
        return getWordSource(names);
    }

    private static Stream<String> getWordSource(String... names) {
        return Arrays.stream(names)
                .map(name -> {
                    URL url = WordSource.class.getClassLoader().getResource(name);
                    return (url != null) ? url : fileUrl(name);
                })
                .filter(Objects::nonNull)
                .map(WordSource::toURI)
                .filter(Objects::nonNull)
                .map(WordSource::lines)
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
