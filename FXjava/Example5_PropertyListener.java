package fxbasics;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * ============================================================================
 *  PRZYKLAD 5: Property + Listener - reagowanie NA ZYWO, bez przycisku.
 * ============================================================================
 *
 * W Przykladzie 4 user musial KLIKNAC przycisk, zeby cos sie stalo.
 * A co, jesli chcesz, zeby Label zmienial sie SAM, w momencie gdy user
 * PISZE w TextField (bez klikania czegokolwiek)? Tu wchodzi Property.
 *
 * KLUCZOWA KONCEPCJA: kazda kontrolka ma swoje "Property"
 * -----------------------------------------------------------------------
 * TextField ma textProperty()   -> "obserwowalna" wersja jego tekstu
 * Slider ma valueProperty()     -> "obserwowalna" wersja jego wartosci
 * CheckBox ma selectedProperty() -> "obserwowalna" wersja "zaznaczone?"
 *
 * Mozesz do takiego Property "podpisac sie" (addListener), i Twoj kod
 * wykona sie AUTOMATYCZNIE, kazdy raz gdy ta wartosc sie zmieni - bez
 * Twojej proaktywnej akcji, bez przycisku.
 *
 *   textField.textProperty().addListener((obs, oldValue, newValue) -> {
 *       // wykonuje sie PRZY KAZDEJ zmianie tekstu - kazdy wpisany/usuniety znak
 *   });
 *
 * Trzy argumenty lambdy:
 *   obs       - samo property (rzadko uzywane)
 *   oldValue  - wartosc PRZED zmiana
 *   newValue  - wartosc PO zmianie (TO jest zazwyczaj to, czego potrzebujesz)
 *
 * TO JEST WZORZEC, KTORY UZYJESZ DO FILTROWANIA LISTY "NA BIEZACO"
 * (Twoje zadanie z lista slow z serwera korzysta z TEGO wlasnie).
 * ============================================================================
 */
public class Example5_PropertyListener extends Application {

    @Override
    public void start(Stage primaryStage) {
        TextField inputField = new TextField();
        inputField.setPromptText("Pisz tutaj...");

        Label mirrorLabel = new Label("Tu pojawi sie to, co piszesz");
        Label lengthLabel = new Label("Dlugosc: 0 znakow");

        // PODPISUJEMY SIE na zmiany tekstu w inputField.
        // Ten kod NIE wykonuje sie TERAZ (przy starcie) - wykona sie
        // KAZDY RAZ, gdy user wpisze/usunie jakikolwiek znak, W PRZYSZLOSCI.
        inputField.textProperty().addListener((obs, oldValue, newValue) -> {
            mirrorLabel.setText("Piszesz: " + newValue);
            lengthLabel.setText("Dlugosc: " + newValue.length() + " znakow");
        });

        VBox root = new VBox(10, inputField, mirrorLabel, lengthLabel);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 350, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Przyklad 5 - Property + Listener");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
