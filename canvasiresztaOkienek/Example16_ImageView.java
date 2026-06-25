package fxbasics;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * ============================================================================
 *  PRZYKLAD 16: ImageView - wyswietlanie obrazkow (zdjec, ikon, plikow PNG).
 * ============================================================================
 *
 * ImageView to kontrolka do WYSWIETLANIA gotowego obrazka - w przeciwienstwie
 * do Canvas (gdzie SAM rysujesz piksele), tutaj wczytujesz juz istniejacy
 * plik graficzny (albo, jak w tym przykladzie, obrazek wygenerowany w pamieci).
 *
 *   Image image = new Image("file:C:/sciezka/do/obrazka.png");
 *   ImageView imageView = new ImageView(image);
 *
 * Przydatne, jesli zadanie wymaga: wczytania zdjecia z dysku, wyswietlenia
 * "przed/po" przy box blur, podgladu PNG przyslanego przez klienta, itp.
 *
 * Mozesz skalowac obrazek do innego rozmiaru niz oryginalny:
 *   imageView.setFitWidth(200);
 *   imageView.setFitHeight(200);
 *   imageView.setPreserveRatio(true); // zachowaj proporcje przy skalowaniu
 *
 * W TYM PRZYKLADZIE: generujemy maly obrazek "programowo" (zamiast wczytywac
 * z dysku, zeby przyklad dzialal bez zewnetrznego pliku), uzywajac WritableImage
 * - to jest dokladnie to, co przyda Ci sie w zadaniu z box blur: stworzenie
 * obrazka wynikowego w pamieci i jego podglad.
 * ============================================================================
 */
public class Example16_ImageView extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Generujemy prosty obrazek "programowo" - szachownica 100x100
        Image generatedImage = createCheckerboardImage(100, 100, 10);

        ImageView imageView = new ImageView(generatedImage);

        // skalowanie - wyswietlamy 2x wiekszy niz oryginal
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);

        StackPane root = new StackPane(imageView);
        Scene scene = new Scene(root, 250, 250);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Przyklad 16 - ImageView");
        primaryStage.show();

        // JESLI MASZ PRAWDZIWY PLIK NA DYSKU - tak go wczytujesz:
        //   Image fileImage = new Image("file:C:/Users/Ty/Desktop/obrazek.png");
        //   imageView.setImage(fileImage);
    }

    /**
     * Tworzy prosty obrazek szachownicy w pamieci - to NIE jest typowy kod
     * z zadan, ale ladnie pokazuje, jak ImageView wyswietla COKOLWIEK,
     * co masz jako Image (czy to z dysku, czy wygenerowane).
     */
    private Image createCheckerboardImage(int width, int height, int squareSize) {
        // WritableImage to "obrazek, ktory mozesz wypelnic pikselami recznie"
        WritableImage image = new WritableImage(width, height);
        var writer = image.getPixelWriter();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                boolean isBlack = ((x / squareSize) + (y / squareSize)) % 2 == 0;
                writer.setColor(x, y, isBlack ? Color.BLACK : Color.WHITE);
            }
        }
        return image;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
