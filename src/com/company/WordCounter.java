package com.company;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

public final class WordCounter {
    public static void main(String[] args) {
//        if (args.length != 2) {
//            System.out.println("Usage: WordCounter [path] [word]");
//            return;
//        }
//        Path start = Path.of(args[0]);
        Path start = Path.of("/Users/ayush/concurrency/src/files");
//        String word = args[1];
        String word = "dapibus";

        Instant before = Instant.now();

        long count = countWords(start, word);
//        ForkJoinPool pool = new ForkJoinPool();
//        long count = pool.invoke(new CountWordsTask(start, word));

        Duration elapsed = Duration.between(before, Instant.now());
        System.out.println(count + " (" + elapsed.toSeconds() + " seconds)");
    }

    public static long countWordInFile(Path file, String word) {
        try {
            return Files.readAllLines(file, StandardCharsets.UTF_8)
                    .stream()
                    .flatMap(l -> Arrays.stream(l.split(" ")))
                    .filter(word::equalsIgnoreCase)
                    .count();
        } catch (IOException e) {
            return 0;
        }
    }

    private static long countWords(Path path, String word) {
        if (!Files.isDirectory(path)) {
            return countWordInFile(path, word);
        }
        try {
            return Files.list(path)
                    .mapToLong(p -> countWords(p, word))
                    .sum();
        } catch (IOException e) {
            return 0;
        }
    }
}
