# Uniwersalny szkielet serwer + klient (Java + JavaFX + SQLite)

## Jak odpalić

```
mvn clean javafx:run                                    # odpala server.ServerApp (domyslne)
mvn javafx:run -Djavafx.mainClass=client.ClientApp       # odpala klienta
mvn javafx:run -Djavafx.mainClass=server.BlurControlApp  # odpala panel sliderem
```

Jeśli zadanie NIE wymaga okna (czysty serwer konsolowy):
```
mvn compile exec:java -Dexec.mainClass=server.Server
```
(albo po prostu uruchom `Server.main()` z IDE — działa bez JavaFX).

## Struktura plików

```
server/
  Server.java          <- glowna klasa serwera: listen() / listenSingleClient(),
                          lista klientow, broadcast, zadanie cykliczne, main()
  ClientHandler.java   <- obsluga 1 klienta: login/haslo, parsowanie wiadomosci,
                          wysylka/odbior plikow binarnych
  Database.java        <- SQLite: authenticate, leaderboard, insert logow
  BoxBlur.java          <- wielowatkowy box blur (niezalezny od reszty)
  ServerApp.java        <- GUI: Canvas 500x500, rysowanie odcinkow, strzalki
  BlurControlApp.java   <- GUI: slider promienia blur (1-15, nieparzyste)

client/
  NetworkClient.java    <- polaczenie TCP, wysylanie/odbior linii i plikow
  ClientApp.java         <- GUI: lista + filtrowanie + sortowanie (wzorzec "slowa")
```

## Mapa: typ zadania -> co odkomentować / zmienić

### "Serwer dla wielu klientow, logowanie, gra/pojedynki" (np. PKN)
- `Server.listen()` — zostaw jak jest (wariant A, wielu klientow)
- `ClientHandler.run()` — odkomentuj **BLOK 1: AUTENTYKACJA**
- `Database.authenticate()` — już gotowe (SELECT po loginie)
- Dopisz własne klasy domenowe (np. `Gesture`, `Duel`, `Player`) w pakiecie `game`
- `ClientHandler` -> rozważ żeby dziedziczyła/implementowała `Player` (jak w treści)
- `Server.challengeToDuel()` — szablon już jest, dopisz `startDuel()`

### "Rysowanie odcinkow na podstawie danych sieciowych"
- Użyj `ServerApp.java` jako bazy (Canvas 500x500, białe tło, obsługa strzałek)
- W `ClientHandler.handleMessage()` — odkomentuj/dopracuj wzorce:
  - kolor hex `[0-9a-fA-F]{6}` → zapamiętaj jako pole `currentColor` w `ClientHandler`
  - 4 floaty `x1 y1 x2 y2` → wywołaj `serverApp.drawSegmentFromNetwork(...)`
- **WAŻNE:** musisz przekazać referencję do `ServerApp` do `Server`/`ClientHandler`
  (np. przez konstruktor), żeby `Platform.runLater()` mógł zaktualizować Canvas
- Dla "serwer co 5s wysyła słowo" → `Server.startPeriodicBroadcast()` już gotowe

### "Klient wyświetlający słowa z serwera + filtrowanie"
- Użyj `ClientApp.java` jako bazy — już implementuje WSZYSTKIE punkty:
  zapamiętywanie, licznik, filtr na bieżąco, sortowanie bez polskich znaków
- Jeśli macie `view.fxml` z konkretnymi `fx:id` — podmień ręczne tworzenie
  kontrolek na wstrzykiwanie przez `@FXML` + `Controller` (wzorzec MVC FXML)

### "Serwer: jeden klient na raz, plik PNG, blur, SQLite"
- `Server.listenSingleClient()` — użyj tego wariantu (B) w `main()`
- `ClientHandler.run()` — odkomentuj **BLOK 2: ODBIÓR PLIKU**, dopisz wywołanie
  `BoxBlur.apply(...)` między odebraniem i odesłaniem pliku
- `BlurControlApp.java` — slider 1-15 nieparzyste, `currentRadius` jako pole statyczne
  (na kolokwium: lepiej wstrzyknąć referencję, ale dla szybkości statyczne pole działa)
- `Database.insertConversionLog(path, size, delayMs)` — już gotowe

## Najczęstsze pułapki (z doświadczenia kolokwiów)
1. **JavaFX + sieć w jednym wątku = zawiesza GUI.** Serwer ZAWSZE w osobnym
   wątku (`Thread`, `setDaemon(true)`), aktualizacje UI ZAWSZE przez `Platform.runLater()`.
2. **BufferedReader + surowe bajty na tym samym sockecie nie miksują się dobrze.**
   Jeśli musisz odebrać plik binarny, zrób to PRZED założeniem `BufferedReader`
   (albo użyj jednego, konsekwentnego strumienia przez cały czas).
3. **CopyOnWriteArrayList**, nie `ArrayList`, dla listy klientów — wielu wątków
   czyta/modyfikuje ją równocześnie.
4. **Promień/rozmiar jądra blur musi być nieparzysty** — pamiętaj o walidacji
   przy każdej zmianie slidera, nie tylko raz.
5. Pamiętaj o **`PrintWriter(..., true)`** (autoflush) albo wywołuj `flush()` —
   inaczej klient nie zobaczy wiadomości.
