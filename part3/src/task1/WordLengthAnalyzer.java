package task1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;


public class WordLengthAnalyzer {

    public static void analyzeTextFile(File directory) throws IOException {

        long startTime = System.currentTimeMillis();
        File concatenatedFile = concatenateTextFiles(directory, "output.txt");

        Map<Integer, Integer> wordLengths = WordLengthCounterAction.computeWordLengths(concatenatedFile.toPath());
        Statistics stats = calculateStatistics(wordLengths);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.printf("Average length: %s%nVariance: %s%nStandard deviation: %s%nTime taken: %d ms%nThe variety of the words' length: %d%n",
                stats.mean, stats.variance, stats.standardDeviation, duration, wordLengths.size());
    }

    private static File concatenateTextFiles(File directory, String outputFileName) throws IOException {
        File outputFile = new File(directory, outputFileName);
        int filesProcessed = 0;
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        try {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".txt")) {
                        BufferedReader reader = new BufferedReader(new FileReader(file));
                        try {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                writer.write(line);
                                writer.newLine();
                            }
                        } finally {
                            reader.close();
                        }
                        filesProcessed++;
                    }
                }
            }
        } finally {
            writer.close();
        }
        System.out.println("Files concatenated: " + filesProcessed);
        return outputFile;
    }

    private static Statistics calculateStatistics(Map<Integer, Integer> wordLengths) {
        double sum = 0.0;
        int totalWords = 0;
        for (Map.Entry<Integer, Integer> entry : wordLengths.entrySet()) {
            int length = entry.getKey();
            int count =  entry.getValue();
            sum += length * count;
            totalWords += count;
        }
        double mean = sum / totalWords;

        double squaredSum = 0.0;
        for (Map.Entry<Integer, Integer> entry : wordLengths.entrySet()) {
            int length = entry.getKey();
            int count = entry.getValue();
            squaredSum += Math.pow(length - mean, 2) * count;
        }
        double variance = squaredSum / totalWords;
        double standardDeviation = Math.sqrt(variance);

        return new Statistics(mean, variance, standardDeviation);
    }

    private static class Statistics {
        final double mean;
        final double variance;
        final double standardDeviation;

        Statistics(double mean, double variance, double standardDeviation) {
            this.mean = mean;
            this.variance = variance;
            this.standardDeviation = standardDeviation;
        }
    }

    public static void main(String[] args) {
        try {
            String pathToDirectory = "src/books";
            analyzeTextFile(new File(pathToDirectory));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}