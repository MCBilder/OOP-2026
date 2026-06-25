package fxbasics;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * ============================================================================
 *  PRZYKLAD 2: Label - najprostsza kontrolka, tylko wyswietla tekst.
 * ============================================================================
 *
 * Label to "tekst na ekranie". Nie da sie go kliknac/edytowac (od tego sa
 * inne kontrolki - Button, TextField). Label sluzy TYLKO do pokazywania info.
 *
 * Dwie kluczowe metody, ktore beda Ci towarzyszyc do konca zycia z JavaFX:
 *   label.setText("nowy tekst")  -> ZMIENIA to, co widac na ekranie
 *   label.getText()              -> ODCZYTUJE aktualny tekst
 *
 * WAZNE: zeby pozniej zmienic tekst Labela (np. po kliknieciu przycisku,
 * po odebraniu wiadomosci z sieci, itp.), musisz miec do niego DOSTEP z innej
 * metody. Dlatego Label CZESTO bywa polem klasy (private Label ...;),
 * a NIE zmienna lokalna wewnatrz start() - patrz Przyklad 3.
 * ============================================================================
 */
public class Example2_Label extends Application {

    @Override
    public void start(Stage primaryStage) {
        // tworzymy Label z poczatkowym tekstem
        Label label = new Label("Witaj w JavaFX!");

        // StackPane uklada swoje dzieci NA SRODKU, jedno na drugim
        // (dlatego "Stack" - stos)
        StackPane root = new StackPane(label);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Przyklad 2 - Label");
        primaryStage.show();

        // mozemy zmienic tekst PO pokazaniu okna - sprobuj, zadzialalo by:
        // label.setText("Zmieniony tekst");
        // ale TU (bezposrednio w start()) nie ma to wiekszego sensu,
        // bo i tak wykona sie zanim user zobaczy okno. Realna zmiana
        // dzieje sie zwykle w odpowiedzi na KLIKNIECIE - patrz Przyklad 3.
    }

    public static void main(String[] args) {
        launch(args);
    }
}
