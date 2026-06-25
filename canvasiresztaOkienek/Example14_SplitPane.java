package fxbasics;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * ============================================================================
 *  PRZYKLAD 14: SplitPane - dwa (lub wiecej) panele z przesuwanym podzialem.
 * ============================================================================
 *
 * SplitPane to layout, w ktorym user moze RECZNIE przesuwac granice miedzy
 * sekcjami (np. jak w IntelliJ - panel z plikami po lewej, edytor po prawej,
 * i mozesz przesunac ten "uchwyt" miedzy nimi).
 *
 *   SplitPane splitPane = new SplitPane(lewyNode, prawyNode);
 *
 * Domyslnie dzieli przestrzen po rowno (50/50). Mozesz to zmienic:
 *   splitPane.setDividerPositions(0.3); // lewy panel = 30% szerokosci
 *
 * Orientacja (pionowy/horyzontalny podzial):
 *   splitPane.setOrientation(Orientation.VERTICAL); // gora/dol zamiast lewo/prawo
 *
 * KIEDY UZYWAC: gdy chcesz, zeby USER mial kontrolke nad proporcjami miedzy
 * dwoma sekcjami (np. lista po lewej + szczegoly po prawej, i user moze
 * sam zdecydowac, jak duzo miejsca dac kazdej z nich).
 * ============================================================================
 */
public class Example14_SplitPane extends Application {

    @Override
    public void start(Stage primaryStage) {
        // lewy panel - lista
        ListView<String> listView = new ListView<>();
        listView.getItems().addAll("Element A", "Element B", "Element C");

        // prawy panel - szczegoly (na razie statyczny tekst)
        VBox detailsPane = new VBox(10, new Label("Tutaj szczegoly\nwybranego elementu"));
        detailsPane.setPadding(new Insets(20));

        SplitPane splitPane = new SplitPane(listView, detailsPane);
        splitPane.setDividerPositions(0.35); // lewy panel = 35% szerokosci na start

        Scene scene = new Scene(splitPane, 450, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Przyklad 14 - SplitPane (przesun srodkowy uchwyt)");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
