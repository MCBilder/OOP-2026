package fxbasics;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * ============================================================================
 *  PRZYKLAD 12: ScrollPane - przewijanie, gdy zawartosc jest wieksza niz okno.
 * ============================================================================
 *
 * ScrollPane to "ramka z paskami przewijania" - owijasz nia zawartosc, ktora
 * MOZE byc wieksza niz widoczny obszar. Pojawiaja sie paski (scrollbary),
 * user moze przewijac.
 *
 *   ScrollPane scrollPane = new ScrollPane(jakasZawartosc);
 *
 * To dziala jak "ramka na jeden Node" - mozesz wlozyc w nia TYLKO JEDEN
 * element (ale to czesto VBox/GridPane z WIELOMA elementami w środku,
 * wiec praktycznie nie jest to ograniczenie).
 *
 * KIEDY UZYWAC: dluga lista, duzy formularz, wielka tabela, Canvas wiekszy
 * niz okno - wszedzie, gdzie zawartosc moze "nie zmiescic sie" na ekranie.
 *
 * Przydatne opcje:
 *   scrollPane.setFitToWidth(true);   // zawartosc rozciaga sie na szerokosc
 *                                      // okna (przewijanie tylko w pionie)
 * ============================================================================
 */
public class Example12_ScrollPane extends Application {

    @Override
    public void start(Stage primaryStage) {
        // tworzymy DUZO etykiet - wiecej niz zmiesci sie w oknie na raz
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        for (int i = 1; i <= 50; i++) {
            content.getChildren().add(new Label("Element numer " + i));
        }

        // owijamy ta duza zawartosc w ScrollPane
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true); // zawartosc rozciaga sie na szerokosc okna

        Scene scene = new Scene(scrollPane, 300, 300); // okno mniejsze niz 50 elementow!
        primaryStage.setScene(scene);
        primaryStage.setTitle("Przyklad 12 - ScrollPane (przewin w dol)");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
