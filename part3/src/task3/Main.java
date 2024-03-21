package task3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.time.Instant;
import java.time.Duration;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Instant start = Instant.now();

        Path[] filePaths = {
                Path.of("src/books/about_parrot.txt"),
                Path.of("src/books/about_dog.txt"),
                Path.of("src/books/about_cat.txt")
        };

        ForkJoinPool pool = ForkJoinPool.commonPool();

        Set<String> commonWords = null;
        for (Path path : filePaths) {
            FileWordFinder task = new FileWordFinder(path);
            Set<String> words = pool.invoke(task);
            if (commonWords == null) {
                commonWords = new HashSet<>(words);
            } else {
                commonWords.retainAll(words);
            }
        }

        if (commonWords != null) {
            commonWords.forEach(System.out::println);
        }

        System.out.println("Execution Time: " + Duration.between(start, Instant.now()).toMillis() + " ms");
    }
}

class FileWordFinder extends RecursiveTask<Set<String>> {
    private final Path file;

    public FileWordFinder(Path file) {
        this.file = file;
    }

    @Override
    protected Set<String> compute() {
        Set<String> words = new HashSet<>();
        try {
            List<String> lines = Files.readAllLines(file);
            for (String line : lines) {
                for (String word : line.split("\\W+")) {
                    if (!word.isEmpty()) {
                        words.add(word);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to process file: " + file, e);
        }
        return words;
    }
}
