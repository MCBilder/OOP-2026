package client;

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

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * ============================================================================
 *  PRZYKLADOWA APLIKACJA KLIENTA (JavaFX, bez FXML) — wzorzec "lista + filtr".
 * ============================================================================
 * Realizuje kazdy punkt z zadania "klient wyswietlajacy slowa z serwera":
 *   - polaczenie z serwerem przy starcie
 *   - zapamietywanie WSZYSTKICH otrzymanych slow (niezaleznie od filtra)
 *   - etykieta z liczba wszystkich otrzymanych slow
 *   - lista przefiltrowana "na biezaco" po prefiksie z filterField
 *   - sortowanie alfabetyczne wedlug ASCII, ignorujac polskie znaki
 *   - format wpisu: "HH:mm:ss slowo"
 *
 * UWAGA: kazda aktualizacja kontrolek z wątku sieciowego idzie przez
 * Platform.runLater() — patrz NetworkClient.onMessage().
 * ============================================================================
 */
public class ClientApp extends Application {

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    // Model: kazdy wpis pamieta czas + slowo, niezaleznie od tego czy jest widoczny
    private record Entry(LocalTime time, String word) {
        String display() {
            return TIME_FORMAT.format(time) + " " + word;
        }
    }

    // WSZYSTKIE otrzymane wpisy — zrodlo prawdy, nie czyszczone przy zmianie filtra
    private final List<Entry> allEntries = new ArrayList<>();

    // Lista widoczna w ListView — odtwarzana z allEntries po kazdej zmianie/filtrze
    private final ObservableList<String> visibleEntries = FXCollections.observableArrayList();

    private Label wordCountLabel;
    private TextField filterField;
    private ListView<String> wordList;

    @Override
    public void start(Stage primaryStage) {
        wordCountLabel = new Label("Liczba slow: 0");
        filterField = new TextField();
        filterField.setPromptText("Filtruj po prefiksie...");
        wordList = new ListView<>(visibleEntries);

        // filtrowanie na biezaco przy kazdej zmianie tekstu w polu
        filterField.textProperty().addListener((obs, oldVal, newVal) -> refreshVisibleList());

        VBox root = new VBox(10, wordCountLabel, filterField, wordList);
        root.setPadding(new Insets(10));

        primaryStage.setScene(new Scene(root, 400, 500));
        primaryStage.setTitle("Klient — lista slow");
        primaryStage.show();

        connectToServer();
    }

    private void connectToServer() {
        NetworkClient client = new NetworkClient("localhost", 5000);

        // KAZDA linia od serwera = jedno nowe slowo
        client.onMessage(word -> Platform.runLater(() -> onWordReceived(word)));

        try {
            client.connect();
        } catch (Exception e) {
            System.err.println("Nie udalo sie polaczyc z serwerem: " + e.getMessage());
        }
    }

    /** Wywolywane (na wątku JavaFX) po otrzymaniu kazdego nowego slowa. */
    private void onWordReceived(String word) {
        Entry entry = new Entry(LocalTime.now(), word);
        allEntries.add(entry);
        wordCountLabel.setText("Liczba slow: " + allEntries.size());

        // dodajemy do widoku TYLKO jesli przechodzi aktualny filtr —
        // ale zapamietujemy zawsze (allEntries), zgodnie z wymaganiem zadania
        if (matchesFilter(entry.word(), filterField.getText())) {
            insertSorted(entry);
        }
    }

    /** Pelne przebudowanie widocznej listy — wywolywane przy zmianie filtra. */
    private void refreshVisibleList() {
        String filter = filterField.getText();
        List<Entry> filtered = allEntries.stream()
                .filter(e -> matchesFilter(e.word(), filter))
                .sorted((e1, e2) -> compareAscii(e1.word(), e2.word()))
                .toList();

        visibleEntries.clear();
        for (Entry e : filtered) {
            visibleEntries.add(e.display());
        }
    }

    /** Wstawia jeden wpis w odpowiednie (sortowane) miejsce, bez przebudowy calej listy. */
    private void insertSorted(Entry entry) {
        int index = 0;
        while (index < visibleEntries.size()) {
            String existingWord = extractWord(visibleEntries.get(index));
            if (compareAscii(entry.word(), existingWord) < 0) {
                break;
            }
            index++;
        }
        visibleEntries.add(index, entry.display());
    }

    private String extractWord(String displayLine) {
        // "HH:mm:ss slowo" -> "slowo"
        int firstSpace = displayLine.indexOf(' ');
        return displayLine.substring(firstSpace + 1);
    }

    private boolean matchesFilter(String word, String filter) {
        if (filter == null || filter.isEmpty()) {
            return true;
        }
        return word.startsWith(filter);
    }

    /**
     * Porownanie "ASCII, ignorujac polskie znaki" — najprostsza, bezpieczna
     * interpretacja: usuwamy znaki diakrytyczne (Normalizer) i porownujemy
     * standardowym compareTo (porzadek ASCII/Unicode).
     */
    private int compareAscii(String a, String b) {
        return stripPolish(a).compareTo(stripPolish(b));
    }

    private String stripPolish(String s) {
        String normalized = java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFD);
        return normalized.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
                .toLowerCase(Locale.ROOT);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
