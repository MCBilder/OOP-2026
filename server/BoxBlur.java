package server;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ============================================================================
 *  UNIWERSALNY BoxBlur — wielowątkowe rozmycie obrazu (box blur, rownoleglosc
 *  na liczbe rdzeni procesora). Wzorzec na zadania typu "serwer PNG + blur".
 * ============================================================================
 * Algorytm: kazdy piksel = srednia arytmetyczna sasiadow w kwadracie o boku
 * `radius*2+1` (lub po prostu `size`, jezeli zadanie podaje rozmiar jadra
 * a nie promien — patrz parametr `kernelSize`).
 *
 * Rownoleglosc: obraz dzielony na rownej wielkosci pasy WIERSZY, kazdy wątek
 * liczy swoj fragment do WSPOLNEJ tablicy wynikowej (rozne indeksy, brak
 * potrzeby synchronizacji zapisu).
 * ============================================================================
 */
public class BoxBlur {

    /**
     * @param input      obraz wejsciowy
     * @param kernelSize rozmiar jadra (NIEPARZYSTY, np. 1, 3, 5 ... 15)
     * @return nowy, rozmyty obraz
     */
    public static BufferedImage apply(BufferedImage input, int kernelSize) throws InterruptedException {
        int width = input.getWidth();
        int height = input.getHeight();
        int radius = kernelSize / 2; // np. kernelSize=3 -> radius=1

        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        int threadCount = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        int rowsPerThread = (int) Math.ceil((double) height / threadCount);
        List<Runnable> tasks = new ArrayList<>();

        for (int t = 0; t < threadCount; t++) {
            final int startRow = t * rowsPerThread;
            final int endRow = Math.min(startRow + rowsPerThread, height);
            if (startRow >= endRow) continue;

            tasks.add(() -> blurRows(input, output, startRow, endRow, radius));
        }

        List<Thread> threads = new ArrayList<>();
        for (Runnable task : tasks) {
            Thread thread = new Thread(task);
            threads.add(thread);
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join(); // czekamy na zakonczenie WSZYSTKICH wątkow
        }

        executor.shutdown();
        return output;
    }

    /** Liczy box blur dla wierszy [startRow, endRow) — wywolywane w jednym wątku. */
    private static void blurRows(BufferedImage input, BufferedImage output,
                                  int startRow, int endRow, int radius) {
        int width = input.getWidth();
        int height = input.getHeight();

        for (int y = startRow; y < endRow; y++) {
            for (int x = 0; x < width; x++) {
                long sumR = 0, sumG = 0, sumB = 0, sumA = 0;
                int count = 0;

                for (int dy = -radius; dy <= radius; dy++) {
                    for (int dx = -radius; dx <= radius; dx++) {
                        int nx = x + dx;
                        int ny = y + dy;
                        // piksele poza obrazem - po prostu ignorowane (dowolne podejscie)
                        if (nx < 0 || nx >= width || ny < 0 || ny >= height) continue;

                        int rgb = input.getRGB(nx, ny);
                        int a = (rgb >> 24) & 0xFF;
                        int r = (rgb >> 16) & 0xFF;
                        int g = (rgb >> 8) & 0xFF;
                        int b = rgb & 0xFF;

                        sumA += a;
                        sumR += r;
                        sumG += g;
                        sumB += b;
                        count++;
                    }
                }

                int avgA = (int) (sumA / count);
                int avgR = (int) (sumR / count);
                int avgG = (int) (sumG / count);
                int avgB = (int) (sumB / count);

                int newRgb = (avgA << 24) | (avgR << 16) | (avgG << 8) | avgB;
                output.setRGB(x, y, newRgb);
            }
        }
    }

    // ========================================================================
    // Pomocnicze: odczyt / zapis PNG (java.awt.image, dziala bez JavaFX)
    // ========================================================================
    public static BufferedImage readImage(String path) throws IOException {
        return ImageIO.read(new File(path));
    }

    public static void writeImage(BufferedImage image, String path) throws IOException {
        ImageIO.write(image, "png", new File(path));
    }

    // ========================================================================
    // Przykladowe uzycie w kontekscie ClientHandlera serwera PNG+blur:
    //
    //   long start = System.currentTimeMillis();
    //   BufferedImage original = BoxBlur.readImage(savedPath);
    //   BufferedImage blurred = BoxBlur.apply(original, kernelSizeFromSlider);
    //   long delay = System.currentTimeMillis() - start;
    //   BoxBlur.writeImage(blurred, outputPath);
    //   database.insertConversionLog(savedPath, kernelSizeFromSlider, delay);
    //   sendFile(outputPath); // odeslanie do klienta
    // ========================================================================
}
