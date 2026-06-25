package fxbasics;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * ============================================================================
 *  PRZYKLAD 4: TextField - pole do wpisywania tekstu przez uzytkownika.
 * ============================================================================
 *
 * TextField to "pole tekstowe" - user moze w nim pisac. Dwie metody, ktore
 * uzywasz najczesciej:
 *
 *   textField.getText()        -> ODCZYTUJE to, co user wpisal (String)
 *   textField.setText("...")    -> WSTAWIA tekst do pola (rzadziej potrzebne)
 *   textField.setPromptText("Wpisz cos...") -> szary tekst-podpowiedz,
 *                                                gdy pole jest puste
 *
 * W tym przykladzie: user wpisuje imie, klika przycisk, Label wita go
 * po imieniu. To jest klasyczny wzorzec "formularz + przycisk submit".
 *
 * WAZNE: TextField (tak jak Label w Przykladzie 3) jest POLEM KLASY,
 * bo musimy go odczytac z WNETRZA lambdy w setOnAction() - czyli
 * w innym momencie, niz kiedy go stworzylismy.
 * ============================================================================
 */
public class Example4_TextFieldAndButton extends Application {

    private TextField nameField;
    private Label greetingLabel;

    @Override
    public void start(Stage primaryStage) {
        nameField = new TextField();
        nameField.setPromptText("Wpisz swoje imie...");

        Button greetButton = new Button("Przywitaj sie");
        greetingLabel = new Label(""); // na razie pusty, wypelni sie po kliknieciu

        greetButton.setOnAction(event -> {
            String name = nameField.getText(); // ODCZYT tego, co user wpisal

            if (name.isEmpty()) {
                greetingLabel.setText("Najpierw wpisz imie!");
            } else {
                greetingLabel.setText("Witaj, " + name + "!");
            }
        });

        VBox root = new VBox(10, nameField, greetButton, greetingLabel);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Przyklad 4 - TextField");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
