package task5;

import java.util.ArrayList;
import java.util.concurrent.*;

public class ParallelArraySum {

    private static final int ArraySize = 10000;
    private static final int TaskCount = 100;
    private static final int SegmentSize = ArraySize / TaskCount;

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        double[] numbers = new double[ArraySize];
        for (int i = 0; i < ArraySize; i++) {
            numbers[i] = Math.random();
        }

        ArrayList<Callable<Double>> tasks = new ArrayList<>();
        for (int i = 0; i < TaskCount; i++) {
            int StartIndex = i * SegmentSize;
            int EndIndex = (i + 1) * SegmentSize;
            tasks.add(() -> {
                double Sum = 0;
                for (int j = StartIndex; j < EndIndex; j++) {
                    Sum += numbers[j];
                }
                return Sum;
            });
        }

        double TotalSum = 0;
        try {
            for (Future<Double> result : executorService.invokeAll(tasks)) {
                TotalSum += result.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("Total sum: " + TotalSum);

        executorService.shutdown();
    }
}