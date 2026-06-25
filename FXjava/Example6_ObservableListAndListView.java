package fxbasics;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * ============================================================================
 *  PRZYKLAD 6: ObservableList + ListView - lista, ktora sama sie odswieza.
 * ============================================================================
 *
 * Chcesz wyswietlic LISTE elementow (np. liste slow z serwera)? Potrzebujesz
 * dwoch rzeczy razem:
 *
 *   1) ObservableList<String> - "specjalna" lista (jak ArrayList, ale
 *      "obserwowalna" - powiadamia kontrolki o zmianach)
 *   2) ListView<String> - kontrolka, ktora WYSWIETLA elementy tej listy
 *
 * KLUCZOWA RZECZ: jak juz POLACZYSZ ListView z ObservableList (przekazujac
 * ja w konstruktorze), to KAZDA zmiana w tej liscie (add/remove/clear)
 * AUTOMATYCZNIE pojawia sie na ekranie. Nie musisz nigdy mowic
 * "listView.refresh()" czy podobnie - dzieje sie samo.
 *
 *   ObservableList<String> items = FXCollections.observableArrayList();
 *   ListView<String> listView = new ListView<>(items);
 *
 *   items.add("nowy element");     // <- listView SAM pokaze ten element
 *   items.remove("stary element");  // <- listView SAM go usunie z widoku
 *
 * Roznica wzgledem normalnej ArrayList<String>: gdybys uzyl zwyklej
 * ArrayList i robil list.add(...), ListView NIC by nie zauwazyl - trzeba
 * by recznie wywolywac listView.setItems(...) na nowo po kazdej zmianie.
 * ObservableList robi to za Ciebie.
 * ============================================================================
 */
public class Example6_ObservableListAndListView extends Application {

    // ObservableList jako POLE - bo dodajemy do niej z wnetrza lambdy
    // (setOnAction przycisku), czyli z innego miejsca/czasu niz start()
    private final ObservableList<String> items = FXCollections.observableArrayList();
    private TextField inputField;

    @Override
    public void start(Stage primaryStage) {
        inputField = new TextField();
        inputField.setPromptText("Wpisz element listy...");

        Button addButton = new Button("Dodaj do listy");

        // KONSTRUKTOR ListView<>(observableList) - to jest to "polaczenie"
        ListView<String> listView = new ListView<>(items);

        addButton.setOnAction(event -> {
            String text = inputField.getText();
            if (!text.isEmpty()) {
                items.add(text);          // <- listView SAM odswiezy widok
                inputField.clear();        // czyscimy pole po dodaniu
            }
        });

        VBox root = new VBox(10, inputField, addButton, listView);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 350, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Przyklad 6 - ObservableList + ListView");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
