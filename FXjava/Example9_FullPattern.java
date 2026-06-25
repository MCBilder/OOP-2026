package fxbasics;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * ============================================================================
 *  PRZYKLAD 9: WSZYSTKO NARAZ - dokladny wzorzec zadania "lista slow z serwera".
 * ============================================================================
 *
 * To laczy WSZYSTKIE poprzednie przyklady w jedna, kompletna aplikacje:
 *   - Platform.runLater()       (Przyklad 7) - bo "siec" dziala w innym wątku
 *   - ObservableList + ListView (Przyklad 6) - lista odswieza sie sama
 *   - Property + Listener       (Przyklad 5) - filtrowanie na biezaco
 *   - Label                     (Przyklad 2) - licznik
 *
 * KLUCZOWA KONCEPCJA TEGO PRZYKLADU: DWIE LISTY, NIE JEDNA
 * -----------------------------------------------------------------------
 * To jest najwazniejsza rzecz do zrozumienia w zadaniach z filtrowaniem:
 *
 *   allWords      (normalna List<String>)     -> WSZYSTKO, co kiedykolwiek
 *                                                  przyszlo, NIGDY nie czyszczone
 *                                                  przez zmiane filtra
 *
 *   visibleWords  (ObservableList<String>)    -> TYLKO to, co ma byc WIDOCZNE
 *                                                  na ListView TERAZ, zgodnie
 *                                                  z aktualnym filtrem
 *
 * Czemu nie jedna lista? Bo gdybys CZYSCIL/FILTROWAL bezposrednio te liste,
 * ktora pokazujesz - ZGUBIŁBYŚ dane! Np. user wpisuje "a" w filtrze (widzi
 * tylko slowa na "a"), potem usuwa filtr - skad maja sie wziac z powrotem
 * WSZYSTKIE slowa, jesli nigdzie ich nie zachowales?
 *
 * Wzorzec dzialania:
 *   1. Nowe slowo przychodzi -> ZAWSZE dodaj do allWords
 *   2. Nowe slowo przychodzi -> dodaj do visibleWords TYLKO jesli pasuje
 *      do aktualnego filtra
 *   3. Filtr sie zmienia -> PRZEBUDUJ visibleWords od zera z allWords,
 *      stosujac NOWY filtr
 * ============================================================================
 */
public class Example9_FullPattern extends Application {

    // (1) ZRODLO PRAWDY - wszystko, co przyszlo, nigdy nie czyszczone filtrem
    private final List<String> allWords = new ArrayList<>();

    // (2) TO, CO WIDAC na ekranie - ObservableList, bo polaczone z ListView
    private final ObservableList<String> visibleWords = FXCollections.observableArrayList();

    private Label countLabel;
    private TextField filterField;

    @Override
    public void start(Stage primaryStage) {
        countLabel = new Label("Otrzymano: 0 slow");

        filterField = new TextField();
        filterField.setPromptText("Filtruj po prefiksie...");

        // (3) PROPERTY + LISTENER - filtrowanie NA BIEZACO, bez przycisku
        // (identyczny wzorzec jak Przyklad 5)
        filterField.textProperty().addListener((obs, oldVal, newVal) -> {
            rebuildVisibleList();
        });

        ListView<String> listView = new ListView<>(visibleWords);

        VBox root = new VBox(10, countLabel, filterField, listView);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 350, 450);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Przyklad 9 - pelny wzorzec (siec + filtr + lista)");
        primaryStage.show();

        startFakeNetwork();
    }

    /** Symuluje siec - co 3 sekundy "przychodzi" nowe losowe slowo. */
    private void startFakeNetwork() {
        String[] possibleWords = {"alfa", "ananas", "beta", "banan", "gamma", "agrest"};

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            String word = possibleWords[(int) (Math.random() * possibleWords.length)];

            // TO DZIEJE SIE W OSOBNYM WATKU -> musimy uzyc Platform.runLater,
            // zanim dotkniemy czegokolwiek zwiazanego z GUI
            Platform.runLater(() -> onWordReceived(word));

        }, 3, 3, TimeUnit.SECONDS);
    }

    /**
     * Wywolywane (juz bezpiecznie, na wątku FX, dzieki Platform.runLater
     * w startFakeNetwork) dla KAZDEGO nowego slowa.
     */
    private void onWordReceived(String word) {
        // KROK 1: ZAWSZE zapamietaj w "zrodle prawdy"
        allWords.add(word);
        countLabel.setText("Otrzymano: " + allWords.size() + " slow");

        // KROK 2: dodaj do WIDOKU tylko jesli pasuje do AKTUALNEGO filtra
        if (matchesFilter(word, filterField.getText())) {
            visibleWords.add(word);
        }
    }

    /** Wywolywane przy KAZDEJ zmianie filtra - przebudowuje caly widok od zera. */
    private void rebuildVisibleList() {
        String filter = filterField.getText();

        visibleWords.clear(); // czyscimy WIDOK (NIE allWords!)

        for (String word : allWords) {
            if (matchesFilter(word, filter)) {
                visibleWords.add(word);
            }
        }
    }

    private boolean matchesFilter(String word, String filter) {
        if (filter == null || filter.isEmpty()) {
            return true; // brak filtra = wszystko przechodzi
        }
        return word.startsWith(filter);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
