package fxbasics;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * ============================================================================
 *  PRZYKLAD 8: Canvas - rysowanie 2D + obsluga klawiatury.
 * ============================================================================
 *
 * Canvas to "platno" - prostokat, na ktorym mozesz rysowac linie, ksztalty,
 * tekst. Aby rysowac, potrzebujesz GraphicsContext (skrot: "gc") - to jest
 * Twoj "pedzel".
 *
 *   Canvas canvas = new Canvas(500, 500);          // platno 500x500px
 *   GraphicsContext gc = canvas.getGraphicsContext2D();  // "pedzel"
 *
 * Podstawowe operacje rysowania:
 *   gc.setFill(Color.WHITE); gc.fillRect(x, y, w, h);   // wypelniony prostokat
 *   gc.setStroke(Color.BLACK); gc.strokeLine(x1,y1,x2,y2); // linia
 *   gc.fillOval(x, y, w, h);                              // wypelnione kolo/owal
 *   gc.fillText("tekst", x, y);                           // tekst NA platnie
 *
 * WAZNA PUŁAPKA #1: Canvas NIE PAMIETA co narysowales!
 * -----------------------------------------------------------------------
 * To tylko piksele. Jesli chcesz np. przesunac caly rysunek (jak w zadaniu
 * z odcinkami i strzalkami), MUSISZ:
 *   1. SAM trzymac liste tego, co narysowales (np. List<double[]> segments)
 *   2. Przy kazdej zmianie: WYCZYSCIC platno (fillRect na cale) i narysowac
 *      WSZYSTKO OD NOWA z tej listy (z nowymi wspolrzednymi/offsetem)
 *
 * WAZNA PUŁAPKA #2: Canvas domyslnie NIE PRZYJMUJE focusu klawiatury!
 * -----------------------------------------------------------------------
 * Jesli klawisze nie dzialaja, sprawdz:
 *   canvas.setFocusTraversable(true);   // pozwala canvasowi miec focus
 *   canvas.requestFocus();               // od razu po starcie, "daj mi focus"
 *
 * Obsluga klawiatury jest na Scene (nie na Canvas):
 *   scene.setOnKeyPressed(event -> { KeyCode code = event.getCode(); ... });
 * ============================================================================
 */
public class Example8_CanvasDrawing extends Application {

    private static final double SIZE = 400;

    private Canvas canvas;
    private GraphicsContext gc;

    // "logiczna" pozycja kolka - to MY pamietamy, Canvas tego nie robi
    private double circleX = 200;
    private double circleY = 200;

    @Override
    public void start(Stage primaryStage) {
        canvas = new Canvas(SIZE, SIZE);
        gc = canvas.getGraphicsContext2D();

        drawEverything(); // pierwsze rysowanie

        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, SIZE, SIZE);

        // obsluga klawiatury - strzalki przesuwaja kolko
        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            switch (code) {
                case UP    -> circleY -= 10;
                case DOWN  -> circleY += 10;
                case LEFT  -> circleX -= 10;
                case RIGHT -> circleX += 10;
                default -> { return; } // inny klawisz - nic nie robimy
            }
            drawEverything(); // PO zmianie pozycji - narysuj WSZYSTKO OD NOWA
        });

        primaryStage.setScene(scene);
        primaryStage.setTitle("Przyklad 8 - Canvas (strzalki przesuwaja kolko)");
        primaryStage.show();

        // BEZ TYCH DWOCH LINII STRZALKI NIE ZADZIALAJA:
        canvas.setFocusTraversable(true);
        canvas.requestFocus();
    }

    /** Czysci CALE platno i rysuje wszystko od nowa - to jest standardowy wzorzec. */
    private void drawEverything() {
        // 1. wyczysc tlo (wypelnij na bialo)
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, SIZE, SIZE);

        // 2. narysuj kolko w aktualnej (zapamietanej) pozycji
        gc.setFill(Color.CORNFLOWERBLUE);
        gc.fillOval(circleX - 20, circleY - 20, 40, 40);

        // 3. tekst informacyjny - aktualna pozycja
        gc.setFill(Color.BLACK);
        gc.fillText(String.format("Pozycja: (%.0f, %.0f)", circleX, circleY), 10, 20);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
