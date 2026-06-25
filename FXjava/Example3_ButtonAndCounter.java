package fxbasics;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * ============================================================================
 *  PRZYKLAD 3: Button + licznik klikniec.
 * ============================================================================
 *
 * To jest TWOJ PIERWSZY "zywy" przyklad - cos sie naprawde zmienia na ekranie
 * w odpowiedzi na akcje uzytkownika.
 *
 * KLUCZOWA KONCEPCJA #1: setOnAction(...)
 * -----------------------------------------
 * Kazdy Button ma metode setOnAction(), ktorej przekazujesz "co ma sie stac
 * po kliknieciu". To jest lambda - kawalek kodu, ktory JavaFX ZAPAMIETUJE
 * i wykona PÓŹNIEJ, kiedy user faktycznie kliknie (nie teraz, przy starcie!).
 *
 *   button.setOnAction(event -> {
 *       // ten kod wykona sie KAZDORAZOWO po kliknieciu, nie raz
 *   });
 *
 * KLUCZOWA KONCEPCJA #2: pola klasy (instance fields) vs zmienne lokalne
 * -----------------------------------------------------------------------
 * W Przykladzie 2, Label byl ZMIENNA LOKALNA wewnatrz start() - istnial
 * tylko "w czasie" wykonywania tej jednej metody.
 *
 * TERAZ potrzebujemy czegos innego: Label musi byc WIDOCZNY i ZMIENIALNY
 * z WNETRZA lambdy w setOnAction() - czyli z innego miejsca w kodzie,
 * w innym (pozniejszym) momencie czasu.
 *
 * Rozwiazanie: zrob z Label POLE KLASY (private Label counterLabel;
 * zadeklarowane NA GORZE klasy, NIE wewnatrz metody). Wtedy KAZDA metoda
 * w tej klasie (w tym lambda w setOnAction) ma do niego dostep.
 *
 * To jest WZORZEC, ktory bedziesz powtarzac przez WSZYSTKIE zadania z GUI:
 * "kontrolki, ktore musza sie zmieniac pozniej -> POLA KLASY".
 * "kontrolki, ktore tworzysz i ukladasz raz, bez potrzeby pozniejszego
 *  dostepu -> moga zostac zmiennymi lokalnymi w start()".
 * ============================================================================
 */
public class Example3_ButtonAndCounter extends Application {

    // POLE KLASY - bo lambda w setOnAction() (zdefiniowana w innym miejscu,
    // wykonywana w innym czasie) musi miec do niego dostep.
    private Label counterLabel;
    private int clickCount = 0; // normalna zmienna int, takze jako pole

    @Override
    public void start(Stage primaryStage) {
        counterLabel = new Label("Kliknieto: 0 razy");

        Button button = new Button("Kliknij mnie");

        // setOnAction przyjmuje lambde typu (ActionEvent event) -> { ... }
        // event zwykle nie jest potrzebny w prostych przypadkach, ale MUSI
        // byc w sygnaturze lambdy (nawet jesli go nie uzywasz wewnatrz).
        button.setOnAction(event -> {
            clickCount++;
            counterLabel.setText("Kliknieto: " + clickCount + " razy");
        });

        // VBox uklada dzieci JEDNO POD DRUGIM (w kolumnie)
        // pierwszy argument (10) to odstep w pikselach miedzy elementami
        VBox root = new VBox(10, counterLabel, button);

        // padding = margines wewnatrz layoutu (od krawedzi okna do kontrolek)
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Przyklad 3 - Button + licznik");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
