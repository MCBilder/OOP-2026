# JavaFX od podstaw — 9 przykładów, jeden wzorzec na raz

## Jak odpalić

```
mvn javafx:run -Djavafx.mainClass=fxbasics.Example1_EmptyWindow
mvn javafx:run -Djavafx.mainClass=fxbasics.Example2_Label
mvn javafx:run -Djavafx.mainClass=fxbasics.Example3_ButtonAndCounter
... itd, zmieniaj nazwe klasy po Example
```

Albo prościej: otwórz dowolny `ExampleX_*.java` w IDE (IntelliJ/Eclipse) i kliknij "Run" na jego `main()` — IDE z wtyczką JavaFX ogarnie to bez Mavena w linii komend.

## Kolejność — ćwicz po kolei, każdy przykład buduje na poprzednim

| # | Plik | Co się uczysz |
|---|---|---|
| 1 | `Example1_EmptyWindow` | Stage / Scene / root, szkielet każdej aplikacji |
| 2 | `Example2_Label` | pierwsza kontrolka, `setText()`/`getText()` |
| 3 | `Example3_ButtonAndCounter` | `setOnAction`, **pola klasy vs zmienne lokalne** |
| 4 | `Example4_TextFieldAndButton` | odczyt tekstu wpisanego przez użytkownika |
| 5 | `Example5_PropertyListener` | reagowanie NA ŻYWO, bez przycisku (`textProperty().addListener`) |
| 6 | `Example6_ObservableListAndListView` | lista, która sama się odświeża |
| 7 | `Example7_PlatformRunLater` | **najważniejszy** — GUI + osobny wątek (sieć/timer) |
| 8 | `Example8_CanvasDrawing` | rysowanie 2D, obsługa klawiatury |
| 9 | `Example9_FullPattern` | WSZYSTKO razem — dokładny wzorzec zadania "lista słów z serwera" |

## Jak ćwiczyć (proponowany sposób)

1. **Odpal przykład**, pobaw się nim (klikaj przyciski, pisz w polach) — zobacz na żywo co robi.
2. **Przeczytaj komentarz na górze pliku** — wyjaśnia *dlaczego* kod jest napisany tak, a nie inaczej.
3. **Zmodyfikuj coś drobnego** — np. w Przykładzie 3 zmień co się dzieje po kliknięciu (np. zamiast liczyć, wypisuj losowy tekst). Sprawdź, czy nadal działa.
4. Przejdź do następnego numeru.

Po przejściu wszystkich 9 — wróć do zadania "klient odbierający słowa z serwera", które dostałeś wcześniej. Powinno być znacznie bardziej zrozumiałe, bo każdy element tego zadania (Label, ListView, TextField+filtr, Platform.runLater) miał już swój osobny, mały przykład tutaj.

## Najważniejsze rzeczy do zapamiętania (skrót)

1. **Pole klasy vs zmienna lokalna**: jeśli kontrolka musi być dostępna z innej metody/lambdy (np. z `setOnAction`, z callbacku sieciowego) — musi być polem klasy (`private Label x;` na górze klasy), nie zmienną lokalną w `start()`.

2. **Platform.runLater()**: każda aktualizacja GUI z wątku, który NIE jest wątkiem FX (sieć, timer, plik) — musi być owinięta w `Platform.runLater(() -> { ... })`.

3. **ObservableList**, nie `ArrayList`, jeśli chcesz, żeby `ListView` sam się odświeżał.

4. **Dwie listy przy filtrowaniu**: jedna ("źródło prawdy", wszystko) + jedna ("widok", tylko to co przechodzi filtr).

5. **Canvas nie pamięta nic** — sam musisz trzymać listę narysowanych elementów i przy zmianie rysować wszystko od nowa.

6. **`setFocusTraversable(true)` + `requestFocus()`** — bez tego klawiatura nie działa na Canvas.
