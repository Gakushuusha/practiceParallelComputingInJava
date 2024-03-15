package task1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class WordLengthCounterAction extends RecursiveTask<Map<Integer, Integer>> {
    private final Path path;
    private static final Object lock = new Object();

    public WordLengthCounterAction(Path path) {
        this.path = path;
    }

    @Override
    protected Map<Integer, Integer> compute() {
        Map<Integer, Integer> wordLengths = new ConcurrentHashMap<>();
        try {
            Files.lines(path).parallel().forEach(line -> {
                Arrays.stream(line.split("(\\s|\\p{Punct})+")).map(String::length).forEach(length -> {
                    synchronized (lock) {
                        wordLengths.merge(length, 1, Integer::sum);
                    }
                });
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return wordLengths;
    }

    public static Map<Integer, Integer> computeWordLengths(Path path) {
        ForkJoinPool pool = new ForkJoinPool();
        WordLengthCounterAction task = new WordLengthCounterAction(path);
        return pool.invoke(task);
    }
}