package task4;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

public class DocumentSearch {

    static class SearchResult {
        private String fileName;
        private Set<String> matchedWords;

        public SearchResult(String fileName, Set<String> matchedWords) {
            this.fileName = fileName;
            this.matchedWords = matchedWords;
        }

        @Override
        public String toString() {
            return String.format("File: %s, Matched Words: %s", fileName, matchedWords);
        }
    }

    static class FileSearchTask extends RecursiveTask<List<SearchResult>> {
        private File directory;
        private Set<String> keywords;

        public FileSearchTask(File directory, Set<String> keywords) {
            this.directory = directory;
            this.keywords = keywords;
        }

        @Override
        protected List<SearchResult> compute() {
            List<SearchResult> results = new ArrayList<>();
            List<FileSearchTask> tasks = new ArrayList<>();

            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        FileSearchTask task = new FileSearchTask(file, keywords);
                        task.fork();
                        tasks.add(task);
                    } else {
                        results.addAll(searchFile(file));
                    }
                }
            }

            for (FileSearchTask task : tasks) {
                results.addAll(task.join());
            }

            return results;
        }

        private List<SearchResult> searchFile(File file) {
            List<SearchResult> found = new ArrayList<>();
            try {
                Set<String> fileWords = Files.lines(Paths.get(file.getPath()))
                        .flatMap(line -> Arrays.stream(line.split("\\W+")))
                        .collect(Collectors.toSet());
                fileWords.retainAll(keywords);
                if (!fileWords.isEmpty()) {
                    found.add(new SearchResult(file.getName(), fileWords));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return found;
        }
    }

    public static void main(String[] args) {
        String pathToResources = "src/books";
        Set<String> searchedKeywords = new HashSet<>(Arrays.asList("algorithm", "Python", "Java", "C++"));

        ForkJoinPool pool = ForkJoinPool.commonPool();
        FileSearchTask task = new FileSearchTask(new File(pathToResources), searchedKeywords);
        List<SearchResult> results = pool.invoke(task);

        results.forEach(System.out::println);
    }
}