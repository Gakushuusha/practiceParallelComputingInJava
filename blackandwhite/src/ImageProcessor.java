import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ImageProcessor {
    private static final String INPUT_FOLDER = "./resources/";
    private static final String OUTPUT_FOLDER = "./out/";
    private static final boolean isParallel = false;
    private static final int segments = 4;

    public static void main(String[] args) throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();

        try (var paths = Files.list(Paths.get(INPUT_FOLDER))) {
            List<File> images = paths.map(java.nio.file.Path::toFile)
                    .collect(Collectors.toList());

            int concurrentImages = 20;

            if (isParallel) {
                processImagesInParallel(images, concurrentImages, segments);
            } else {
                processImagesSequentially(images);
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Total processing time: " + (endTime - startTime) + " ms");
    }

    private static void processImagesInParallel(List<File> images, int maxParallel, int segments) throws InterruptedException {
        ImageConverter converter = (segments == 2) ? new ParallelImageConverter(2) : new ParallelImageConverter(4);
        List<Thread> imageProcessors = new ArrayList<>();
        for (int i = 0; i < images.size(); i += maxParallel) {
            int end = Math.min(i + maxParallel, images.size());
            List<File> batch = images.subList(i, end);
            Thread thread = new Thread(() -> batch.forEach(image -> processImage(image, converter)));
            imageProcessors.add(thread);
            thread.start();
        }

        for (Thread thread : imageProcessors) {
            thread.join();
        }
    }

    private static void processImagesSequentially(List<File> images) {
        images.forEach(image -> processImage(image, new SequentialImageConverter()));
    }

    private static void processImage(File imageFile, ImageConverter converter) {
        try {
            BufferedImage sourceImage = ImageIO.read(imageFile);
            BufferedImage grayedImage = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), BufferedImage.TYPE_INT_RGB);

            String outputFileName = OUTPUT_FOLDER + imageFile.getName();
            File outputFile = new File(outputFileName);

            converter.convert(sourceImage, grayedImage);
            ImageIO.write(grayedImage, "jpg", outputFile);
        } catch (IOException e) {
            System.err.println("Error processing image: " + imageFile.getName());
            e.printStackTrace();
        }
    }
}

interface ImageConverter {
    void convert(BufferedImage sourceImg, BufferedImage targetImg);
}

class SequentialImageConverter implements ImageConverter {
    public void convert(BufferedImage sourceImg, BufferedImage targetImg) {
        for (int y = 0; y < sourceImg.getHeight(); y++) {
            for (int x = 0; x < sourceImg.getWidth(); x++) {
                transformPixel(sourceImg, targetImg, x, y);
            }
        }
    }

    private void transformPixel(BufferedImage srcImg, BufferedImage destImg, int x, int y) {
        int pixelRGB = srcImg.getRGB(x, y);
        int red = (pixelRGB >> 16) & 0xFF;
        int green = (pixelRGB >> 8) & 0xFF;
        int blue = pixelRGB & 0xFF;

        int avgColor = (red + green + blue) / 3;
        int grayscaleRGB = (0xFF << 24) | (avgColor << 16) | (avgColor << 8) | avgColor;
        destImg.setRGB(x, y, grayscaleRGB);
    }
}

class ParallelImageConverter implements ImageConverter {
    private final int segments;

    public ParallelImageConverter(int segments) {
        this.segments = segments;
    }

    public void convert(BufferedImage sourceImg, BufferedImage targetImg) {
        List<Thread> threads = new ArrayList<>();
        int segmentHeight = sourceImg.getHeight() / this.segments;

        for (int i = 0; i < segments; i++) {
            final int segmentStartY = segmentHeight * i;
            int adjustedHeight = (i == segments - 1) ? sourceImg.getHeight() - segmentStartY : segmentHeight;

            Thread worker = new Thread(() -> convertToGrayscale(sourceImg, targetImg, 0, segmentStartY, sourceImg.getWidth(), adjustedHeight));
            threads.add(worker);
            worker.start();
        }

        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException ignored) {
            }
        });
    }

    private void convertToGrayscale(BufferedImage srcImage, BufferedImage destImage, int startX, int startY, int width, int height) {
        for (int x = startX; x < startX + width; x++) {
            for (int y = startY; y < startY + height; y++) {
                transformPixel(srcImage, destImage, x, y);
            }
        }
    }

    private void transformPixel(BufferedImage srcImg, BufferedImage destImg, int x, int y) {
        int pixelRGB = srcImg.getRGB(x, y);
        int red = (pixelRGB >> 16) & 0xFF;
        int green = (pixelRGB >> 8) & 0xFF;
        int blue = pixelRGB & 0xFF;

        int avgColor = (red + green + blue) / 3;
        int grayscaleRGB = (0xFF << 24) | (avgColor << 16) | (avgColor << 8) | avgColor;
        destImg.setRGB(x, y, grayscaleRGB);
    }
}