package server;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * ============================================================================
 *  PRZYKLADOWY interfejs uzytkownika serwera: slider promienia + etykieta.
 * ============================================================================
 * Wzorzec na punkt zadania: "Serwer powinien posiadac UI z suwakiem
 * pozwalajacym modyfikowac promien filtra. Promien przyjmuje wylacznie
 * wartosci nieparzyste z zakresu 1-15."
 *
 * Trik: Slider sam w sobie generuje wartosci ciagle/calkowite — wiec
 * zaokraglamy do najblizszej NIEPARZYSTEJ liczby przy kazdej zmianie.
 * ============================================================================
 */
public class BlurControlApp extends Application {

    // pole statyczne, zeby ClientHandler / Server mogl je odczytac w kazdej chwili
    // (w prawdziwym zadaniu lepiej wstrzyknac przez konstruktor/referencje,
    //  ale dla prostoty kolokwialnej wystarczy volatile static)
    public static volatile int currentRadius = 1;

    @Override
    public void start(Stage primaryStage) {
        Label radiusLabel = new Label("Promien filtra: 1");

        Slider slider = new Slider(1, 15, 1);
        slider.setMajorTickUnit(2);
        slider.setMinorTickCount(0);
        slider.setSnapToTicks(false); // sami wymuszamy nieparzystosc w listenerze
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);

        slider.valueProperty().addListener((obs, oldVal, newVal) -> {
            int rounded = Math.round(newVal.floatValue());
            if (rounded % 2 == 0) {
                // wymuszenie nieparzystosci — zaokraglamy w gore
                rounded = Math.min(15, rounded + 1);
            }
            currentRadius = rounded;
            radiusLabel.setText("Promien filtra: " + rounded);
        });

        VBox root = new VBox(15, radiusLabel, slider);
        root.setPadding(new Insets(20));

        primaryStage.setScene(new Scene(root, 350, 150));
        primaryStage.setTitle("Ustawienia filtra blur");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
