package fxbasics;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * ============================================================================
 *  PRZYKLAD 10: BorderPane - uklad "gora / dol / lewo / prawo / centrum".
 * ============================================================================
 *
 * BorderPane to layout z PIECIOMA "slotami", do ktorych wkladasz Node'y:
 *
 *      +---------------- TOP ----------------+
 *      |                                       |
 *      | LEFT |        CENTER         | RIGHT |
 *      |                                       |
 *      +---------------- BOTTOM --------------+
 *
 * Kazdy slot jest OPCJONALNY - jesli go nie ustawisz, po prostu nie istnieje
 * (nie zostaje pusta dziura, layout sam sie dopasowuje).
 *
 *   BorderPane root = new BorderPane();
 *   root.setTop(jakisNode);
 *   root.setBottom(jakisNode);
 *   root.setLeft(jakisNode);
 *   root.setRight(jakisNode);
 *   root.setCenter(jakisNode);
 *
 * KIEDY UZYWAC: gdy potrzebujesz klasycznego ukladu "aplikacji" - np. gorny
 * toolbar z przyciskami, lewy panel z lista/menu, srodek z glowna zawartoscia
 * (np. Canvas albo ListView), dolny status bar.
 *
 * CENTER zawsze "rozciaga sie", zeby wypelnic cala dostepna przestrzen -
 * to jest najwazniejsza rzecz do zapamietania o BorderPane.
 * ============================================================================
 */
public class Example10_BorderPane extends Application {

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        // TOP - pasek na gorze, np. tytul aplikacji
        Label topLabel = new Label("GORA (Top) - np. tytul lub menu");
        topLabel.setStyle("-fx-background-color: lightblue; -fx-padding: 10;");
        topLabel.setMaxWidth(Double.MAX_VALUE); // rozciagamy na cala szerokosc
        topLabel.setAlignment(Pos.CENTER);
        root.setTop(topLabel);

        // BOTTOM - status bar na dole
        Label bottomLabel = new Label("DOL (Bottom) - np. status bar");
        bottomLabel.setStyle("-fx-background-color: lightgray; -fx-padding: 10;");
        bottomLabel.setMaxWidth(Double.MAX_VALUE);
        bottomLabel.setAlignment(Pos.CENTER);
        root.setBottom(bottomLabel);

        // LEFT - panel boczny z przyciskami
        HBox leftBox = new HBox(new Button("Lewy 1"), new Button("Lewy 2"));
        leftBox.setStyle("-fx-background-color: lightyellow; -fx-padding: 10;");
        root.setLeft(leftBox);

        // RIGHT - drugi panel boczny
        Label rightLabel = new Label("PRAWO\n(Right)");
        rightLabel.setStyle("-fx-background-color: lightpink; -fx-padding: 10;");
        root.setRight(rightLabel);

        // CENTER - glowna zawartosc, ZAWSZE wypelnia cala reszte przestrzeni
        Label centerLabel = new Label("CENTRUM (Center)\nrozciaga sie automatycznie");
        centerLabel.setStyle("-fx-background-color: white;");
        centerLabel.setAlignment(Pos.CENTER);
        centerLabel.setMaxWidth(Double.MAX_VALUE);
        centerLabel.setMaxHeight(Double.MAX_VALUE);
        root.setCenter(centerLabel);

        Scene scene = new Scene(root, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Przyklad 10 - BorderPane");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
