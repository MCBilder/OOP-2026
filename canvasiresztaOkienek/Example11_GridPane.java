package fxbasics;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * ============================================================================
 *  PRZYKLAD 11: GridPane - uklad w wierszach/kolumnach, jak tabela/Excel.
 * ============================================================================
 *
 * GridPane to "siatka" - kazdy Node ma swoja pozycje (kolumna, wiersz):
 *
 *   gridPane.add(node, kolumna, wiersz);
 *
 * Indeksy zaczynaja sie od 0 (0,0) = lewy gorny rog.
 *
 * KIEDY UZYWAC: formularze! Login+TextField w jednym wierszu, Haslo+Pole
 * w drugim, Przycisk w trzecim - to jest KLASYCZNY przypadek dla GridPane.
 * (Mogłbyś to zrobic VBoxem z HBoxami w środku, ale GridPane jest czystszy
 * gdy kolumny musza sie wyrownac - np. wszystkie etykiety "Login:"/"Haslo:"
 * w jednej kolumnie, wszystkie pola w drugiej, idealnie wyrownane).
 *
 *   GridPane grid = new GridPane();
 *   grid.setHgap(10);  // odstep w pikselach miedzy KOLUMNAMI
 *   grid.setVgap(10);  // odstep w pikselach miedzy WIERSZAMI
 *
 *   grid.add(new Label("Login:"), 0, 0);   // kolumna 0, wiersz 0
 *   grid.add(new TextField(),     1, 0);   // kolumna 1, wiersz 0 (ten sam wiersz!)
 *   grid.add(new Label("Haslo:"), 0, 1);   // kolumna 0, wiersz 1
 *   grid.add(new PasswordField(), 1, 1);   // kolumna 1, wiersz 1
 * ============================================================================
 */
public class Example11_GridPane extends Application {

    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setHgap(10);  // odstep miedzy kolumnami
        grid.setVgap(10);  // odstep miedzy wierszami
        grid.setPadding(new Insets(20));

        // wiersz 0: Login
        grid.add(new Label("Login:"), 0, 0);
        TextField loginField = new TextField();
        grid.add(loginField, 1, 0);

        // wiersz 1: Haslo
        grid.add(new Label("Haslo:"), 0, 1);
        PasswordField passwordField = new PasswordField();
        grid.add(passwordField, 1, 1);

        // wiersz 2: przycisk - GridPane.setColumnSpan(...) rozciaga go na 2 kolumny
        Button loginButton = new Button("Zaloguj");
        grid.add(loginButton, 1, 2);

        Label statusLabel = new Label("");
        grid.add(statusLabel, 1, 3);

        loginButton.setOnAction(event -> {
            statusLabel.setText("Login: " + loginField.getText() + ", haslo dlugosc: " + passwordField.getText().length());
        });

        Scene scene = new Scene(grid, 350, 250);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Przyklad 11 - GridPane (formularz logowania)");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
