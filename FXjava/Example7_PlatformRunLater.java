package fxbasics;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * ============================================================================
 *  PRZYKLAD 7: Platform.runLater() - NAJWAZNIEJSZY przyklad w tej serii.
 * ============================================================================
 *
 * To jest TEN problem, na ktory trafisz w KAZDYM zadaniu laczacym GUI
 * z siecia (klient odbierajacy wiadomosci) albo z timerem (np. serwer
 * wysylajacy co 5 sekund cos).
 *
 * ZASADA (zapamietaj to na zawsze):
 * -----------------------------------------------------------------------
 *   Tylko JEDEN, specjalny wątek - "FX Application Thread" - moze
 *   dotykac kontrolek GUI (Label.setText, lista.add, etc).
 *
 *   Jesli sprobujesz zmienic GUI z INNEGO wątku (np. wątku timera,
 *   wątku czytajacego z sieci) - dostaniesz wyjatek:
 *   "IllegalStateException: Not on FX application thread"
 *
 * ROZWIAZANIE: Platform.runLater(() -> { ... })
 * -----------------------------------------------------------------------
 *   To jest jak wyslanie liscika do wątku FX: "hej, jak będziesz mial
 *   chwile, wykonaj ten kod". Wątek FX odbiera te "liściki" i wykonuje
 *   je pomiędzy odswiezeniami ekranu.
 *
 *   Wewnatrz Platform.runLater(...) JUZ MOZESZ bezpiecznie dotykac GUI.
 *
 * W tym przykladzie: ScheduledExecutorService (poznany w zadaniach
 * serwerowych!) co 2 sekundy "udaje", ze cos przyszlo z sieci, i probuje
 * zaktualizowac liste - ale robi to przez Platform.runLater(), bo dzieje
 * sie to w OSOBNYM WATKU (nie wątku FX).
 * ============================================================================
 */
public class Example7_PlatformRunLater extends Application {

    private final ObservableList<String> items = FXCollections.observableArrayList();
    private Label counterLabel;
    private int counter = 0;

    @Override
    public void start(Stage primaryStage) {
        counterLabel = new Label("Otrzymano: 0 wiadomosci");
        ListView<String> listView = new ListView<>(items);

        VBox root = new VBox(10, counterLabel, listView);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 350, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Przyklad 7 - Platform.runLater");
        primaryStage.show();

        // startujemy "symulacje sieci" - to jest OSOBNY WATEK, nie wątek FX!
        startFakeNetworkSimulation();
    }

    /**
     * Symuluje to, co robi NetworkClient w prawdziwym zadaniu: w tle,
     * w OSOBNYM WATKU, co jakis czas "przychodzi" nowa wiadomosc.
     */
    private void startFakeNetworkSimulation() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(() -> {
            // TEN KOD WYKONUJE SIE W OSOBNYM WATKU (scheduler), NIE wątku FX!
            counter++;
            String fakeMessage = "wiadomosc-" + counter;

            // ZLA WERSJA (NIE ROB TEGO) - wywola wyjatek "Not on FX thread":
            //   counterLabel.setText("Otrzymano: " + counter);
            //   items.add(fakeMessage);

            // DOBRA WERSJA - przez Platform.runLater:
            Platform.runLater(() -> {
                // TU, W SRODKU runLater, jestesmy bezpiecznie na wątku FX
                counterLabel.setText("Otrzymano: " + counter + " wiadomosci");
                items.add(fakeMessage);
            });

        }, 2, 2, TimeUnit.SECONDS); // pierwsza po 2s, potem co 2s
    }

    public static void main(String[] args) {
        launch(args);
    }
}
