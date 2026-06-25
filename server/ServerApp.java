package server;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 *  PRZYKLADOWA APLIKACJA OKIENKOWA SERWERA (JavaFX, bez FXML).
 * ============================================================================
 * Wzorzec na zadanie typu "rysowanie odcinkow":
 *   - serwer dziala w tle (osobny wątek demon)
 *   - GUI rysuje odcinki przyslane przez klientow
 *   - obsluga strzalek przesuwajacych uklad wspolrzednych
 *   - WAZNE: aktualizacje Canvas z wątku sieciowego TYLKO przez Platform.runLater()
 *
 * Jezeli zadanie nie wymaga okna — zignoruj ten plik, uzyj samego Server.main().
 * ============================================================================
 */
public class ServerApp extends Application {

    private static final double WIDTH = 500;
    private static final double HEIGHT = 500;

    private Canvas canvas;
    private GraphicsContext gc;

    // przesuniecie ukladu wspolrzednych (klawiaturą), w pikselach
    private double offsetX = 0;
    private double offsetY = 0;

    // lista narysowanych odcinkow, do ponownego rysowania po przesunieciu
    // kazdy element: {x1, y1, x2, y2, r, g, b} — kolor zapisany na trwale przy odcinku
    private final List<double[]> segments = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();
        clearCanvas();

        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        // --- obsluga strzalek: przesuwa uklad o 10px, w odpowiednim kierunku ---
        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            switch (code) {
                case UP -> offsetY -= 10;
                case DOWN -> offsetY += 10;
                case LEFT -> offsetX -= 10;
                case RIGHT -> offsetX += 10;
                default -> { return; }
            }
            redrawAll();
            updateTitle(primaryStage);
        });

        primaryStage.setTitle("Rysowanie odcinkow — przesuniecie: (0, 0)");
        primaryStage.setScene(scene);
        canvas.setFocusTraversable(true);
        primaryStage.show();
        canvas.requestFocus();

        // --- start serwera sieciowego w tle, NIE blokuje wątku JavaFX ---
        Server server = new Server(5000);
        Thread serverThread = new Thread(() -> {
            // tutaj normalnie server.listen(), ale przeciazamy je tak,
            // by po kazdej otrzymanej wiadomosci wolac Platform.runLater(...)
            // -> patrz: integracja w ClientHandler.handleMessage(), ktora
            //    powinna wywolywac np. serverApp.drawSegment(...) zamiast
            //    bezposrednio rysowac (bo to inny wątek!)
            server.listen();
        });
        serverThread.setDaemon(true);
        serverThread.start();
    }

    /** Wywolywane z wątku sieciowego — MUSI przejsc przez Platform.runLater. */
    public void drawSegmentFromNetwork(double x1, double y1, double x2, double y2, Color color) {
        Platform.runLater(() -> {
            segments.add(new double[]{x1, y1, x2, y2, color.getRed(), color.getGreen(), color.getBlue()});
            drawSingleSegment(x1, y1, x2, y2, color);
        });
    }

    private void drawSingleSegment(double x1, double y1, double x2, double y2, Color color) {
        gc.setStroke(color);
        gc.setLineWidth(2);
        // przeliczenie ukladu logicznego na piksele canvasu, z uwzglednieniem przesuniecia
        gc.strokeLine(x1 + offsetX, y1 + offsetY, x2 + offsetX, y2 + offsetY);
    }

    private void redrawAll() {
        clearCanvas();
        for (double[] seg : segments) {
            Color color = new Color(seg[4], seg[5], seg[6], 1.0);
            drawSingleSegment(seg[0], seg[1], seg[2], seg[3], color);
        }
    }

    private void clearCanvas() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, WIDTH, HEIGHT);
    }

    private void updateTitle(Stage stage) {
        stage.setTitle(String.format("Rysowanie odcinkow — przesuniecie: (%.0f, %.0f)", offsetX, offsetY));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
