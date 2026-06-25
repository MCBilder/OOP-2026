package fxbasics;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * ============================================================================
 *  PRZYKLAD 13: TabPane - zakladki, jak karty w przegladarce.
 * ============================================================================
 *
 * TabPane sklada sie z wielu Tab - kazdy Tab ma:
 *   - tytul (to, co widac na "uszku" zakladki)
 *   - zawartosc (jakikolwiek Node - VBox, Canvas, ListView, cokolwiek)
 *
 *   TabPane tabPane = new TabPane();
 *
 *   Tab tab1 = new Tab("Nazwa zakladki");
 *   tab1.setContent(jakisNode);
 *   tabPane.getTabs().add(tab1);
 *
 * KIEDY UZYWAC: gdy masz kilka "trybow" / "widokow" tej samej aplikacji,
 * np. "Lista uczestnikow" / "Historia gier" / "Ustawienia" - kazdy w
 * swojej zakladce, zamiast upychac wszystko na jednym ekranie.
 *
 * Domyslnie zakladki mozna ZAMYKAC (krzyzyk na zakladce) - jesli tego
 * NIE chcesz (np. stale 3 zakladki aplikacji), ustaw:
 *   tab.setClosable(false);
 * ============================================================================
 */
public class Example13_TabPane extends Application {

    @Override
    public void start(Stage primaryStage) {
        TabPane tabPane = new TabPane();

        // ZAKLADKA 1
        VBox tab1Content = new VBox(10, new Label("To jest zawartosc zakladki 1."));
        tab1Content.setPadding(new Insets(20));
        Tab tab1 = new Tab("Lista", tab1Content);
        tab1.setClosable(false); // user nie moze zamknac tej zakladki

        // ZAKLADKA 2
        VBox tab2Content = new VBox(10, new Label("To jest zawartosc zakladki 2 - inny widok."));
        tab2Content.setPadding(new Insets(20));
        Tab tab2 = new Tab("Historia", tab2Content);
        tab2.setClosable(false);

        // ZAKLADKA 3
        VBox tab3Content = new VBox(10, new Label("Ustawienia tutaj."));
        tab3Content.setPadding(new Insets(20));
        Tab tab3 = new Tab("Ustawienia", tab3Content);
        tab3.setClosable(false);

        tabPane.getTabs().addAll(tab1, tab2, tab3);

        Scene scene = new Scene(tabPane, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Przyklad 13 - TabPane");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
