package fxbasics;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * ============================================================================
 *  PRZYKLAD 15: TableView - tabela z kolumnami, jak prosty Excel.
 * ============================================================================
 *
 * TableView jest NAJBARDZIEJ skomplikowana kontrolka z tej serii, ale
 * wzorzec jest zawsze taki sam, krok po kroku:
 *
 * KROK 1: potrzebujesz KLASY MODELU - jeden obiekt = jeden WIERSZ tabeli.
 * -----------------------------------------------------------------------
 * To jest jak Twoj rekord AuctionRecord z zadania serwerowego, ale tutaj
 * pola MUSZA byc "Property" (SimpleStringProperty, SimpleIntegerProperty),
 * NIE normalny String/int - to jest wymog TableView, zeby mogla "obserwowac"
 * zmiany i automatycznie odswiezac widok.
 *
 * KROK 2: TableColumn<TypWiersza, TypKolumny> - jedna kolumna = jedno pole
 * -----------------------------------------------------------------------
 * Kazda kolumna musi wiedziec, JAK wyciagnac swoja wartosc z obiektu wiersza.
 * Robi sie to przez setCellValueFactory + PropertyValueFactory("nazwaPola")
 * - "nazwaPola" odpowiada nazwie GETTERA bez "get" (np. "login" -> getLogin()).
 *
 * KROK 3: ObservableList<TypWiersza> jako dane, polaczone z TableView
 * -----------------------------------------------------------------------
 * Tak jak w ListView - ObservableList sprawia, ze tabela SAMA sie odswiezy
 * po dodaniu/usunieciu wiersza.
 * ============================================================================
 */
public class Example15_TableView extends Application {

    /**
     * KROK 1: model wiersza. Pola jako Property (nie zwykly String/int!),
     * zeby TableView mogla je obserwowac.
     */
    public static class Player {
        private final SimpleStringProperty login;
        private final SimpleIntegerProperty score;

        public Player(String login, int score) {
            this.login = new SimpleStringProperty(login);
            this.score = new SimpleIntegerProperty(score);
        }

        // gettery w STYLU JAVAFX (zwracajace Property, nie surowa wartosc) -
        // to jest wymagane, zeby PropertyValueFactory(...) je znalazlo
        public SimpleStringProperty loginProperty() { return login; }
        public SimpleIntegerProperty scoreProperty() { return score; }

        // normalne gettery - przydatne czasem do innych celow
        public String getLogin() { return login.get(); }
        public int getScore() { return score.get(); }
    }

    @Override
    public void start(Stage primaryStage) {
        // KROK 3: ObservableList z danymi (wierszami tabeli)
        ObservableList<Player> players = FXCollections.observableArrayList(
                new Player("alice", 150),
                new Player("bob", 90),
                new Player("mirek", 200)
        );

        TableView<Player> table = new TableView<>(players);

        // KROK 2: kolumny - kazda "podlaczona" do jednego pola modelu
        TableColumn<Player, String> loginColumn = new TableColumn<>("Login");
        loginColumn.setCellValueFactory(
                new javafx.scene.control.cell.PropertyValueFactory<>("login")
        );

        TableColumn<Player, Integer> scoreColumn = new TableColumn<>("Punkty");
        scoreColumn.setCellValueFactory(
                new javafx.scene.control.cell.PropertyValueFactory<>("score")
        );

        table.getColumns().addAll(loginColumn, scoreColumn);

        VBox root = new VBox(table);
        Scene scene = new Scene(root, 350, 250);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Przyklad 15 - TableView (ranking graczy)");
        primaryStage.show();

        // PRZYKLAD dodania nowego wiersza "na zywo" - tak jak items.add()
        // w ListView, tabela SAMA odswiezy widok:
        //   players.add(new Player("nowy_gracz", 50));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
