package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * ============================================================================
 *  UNIWERSALNY SZKIELET SERWERA TCP — do adaptacji na kolokwium
 * ============================================================================
 *
 * Ten plik zawiera WSZYSTKIE wzorce, które pojawiały się w zadaniach typu:
 *   - "papier-kamien-nozyce" (logowanie, ClientHandler, lista klientow, gra)
 *   - "rysowanie odcinkow"   (broadcast, parsing wiadomosci, zadanie cykliczne)
 *   - "klient + lista slow"  (serwer wysyla co X sekund losowe slowo)
 *   - "serwer PNG + blur"    (jeden klient na raz, odbior/wysylka plikow binarnych)
 *
 * NA KOLOKWIUM: skopiuj ten plik, usun co niepotrzebne, dopisz logike specyficzna
 * dla zadania. Nie musisz uzywac wszystkiego na raz.
 *
 * WYBOR WARIANTU (linia w main / w listen()):
 *   A) WIELU klientow naraz       -> listenMultiClient()   [domyslne, najczesciej trzeba]
 *   B) JEDEN klient na raz        -> listenSingleClient()  [np. zadanie z PNG+blur]
 * ============================================================================
 */
public class Server {

    private final int port;
    private ServerSocket serverSocket;

    // --- WARIANT A: wielu klientow jednoczesnie ---
    // CopyOnWriteArrayList = bezpieczna lista przy wspoldzielonym dostepie z wielu wątkow
    // (kazdy ClientHandler dziala w swoim wlasnym wątku)
    private final List<ClientHandler> clients = new CopyOnWriteArrayList<>();

    // Wspolny dostep do bazy danych (patrz klasa Database)
    private final Database database = new Database();

    // Do zadan cyklicznych typu "co 5 sekund wyslij wszystkim losowe slowo"
    private ScheduledExecutorService scheduler;

    public Server(int port) {
        this.port = port;
    }

    // ========================================================================
    // WARIANT A: SERWER OBSLUGUJACY WIELU KLIENTOW NARAZ
    // (uzyj gdy zadanie mowi: "serwer powinien obslugiwac wielu klientow")
    // ========================================================================
    public void listen() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Serwer wystartowal na porcie " + port);

            // Przykladowe zadanie cykliczne — odkomentuj gdy potrzebne
            // startPeriodicBroadcast();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(clientSocket, this);
                addClient(handler);
                // Kazdy klient w osobnym wątku — serwer od razu wraca do accept()
                new Thread(handler).start();
            }
        } catch (IOException e) {
            System.err.println("Blad serwera: " + e.getMessage());
        }
    }

    // ========================================================================
    // WARIANT B: SERWER OBSLUGUJACY JEDNEGO KLIENTA NA RAZ
    // (uzyj gdy zadanie mowi: "serwer akceptuje polaczenie tylko od jednego
    //  klienta naraz, po obsluzeniu jednego moze obslugiwac kolejnego")
    // ========================================================================
    public void listenSingleClient() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Serwer (single-client) wystartowal na porcie " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Polaczono klienta: " + clientSocket.getInetAddress());

                // UWAGA: brak new Thread() — obsluga BLOKUJACA, w glownym wątku.
                // Dzieki temu accept() na kolejnego klienta wykona sie
                // dopiero PO zakonczeniu obslugi biezacego.
                ClientHandler handler = new ClientHandler(clientSocket, this);
                handler.run(); // run(), NIE start() na nowym wątku — chcemy blokujaco!

                System.out.println("Zakonczono obsluge klienta, czekam na kolejnego...");
            }
        } catch (IOException e) {
            System.err.println("Blad serwera: " + e.getMessage());
        }
    }

    // ========================================================================
    // ZARZADZANIE LISTA KLIENTOW (Etap 1, Krok 3 z zadania PKN)
    // ========================================================================
    public void addClient(ClientHandler client) {
        clients.add(client);
        System.out.println("Klient dolaczyl. Liczba klientow: " + clients.size());
    }

    public void removeClient(ClientHandler client) {
        clients.remove(client);
        System.out.println("Klient odlaczyl. Liczba klientow: " + clients.size());
    }

    public List<ClientHandler> getClients() {
        return clients;
    }

    /** Szukanie klienta po loginie/nicku — przydatne np. do "challengeToDuel" */
    public ClientHandler findClientByLogin(String login) {
        for (ClientHandler c : clients) {
            if (login.equals(c.getLogin())) {
                return c;
            }
        }
        return null;
    }

    // ========================================================================
    // WYSYLANIE WIADOMOSCI (jeden klient / broadcast do wszystkich)
    // ========================================================================
    public void sendToClient(ClientHandler client, String message) {
        client.send(message);
    }

    /** Broadcast — np. wynik gry, nowy odcinek do narysowania u wszystkich, etc. */
    public void broadcast(String message) {
        for (ClientHandler c : clients) {
            c.send(message);
        }
    }

    /** Broadcast z wykluczeniem nadawcy — np. "X narysowal odcinek" do innych */
    public void broadcastExcept(String message, ClientHandler excluded) {
        for (ClientHandler c : clients) {
            if (c != excluded) {
                c.send(message);
            }
        }
    }

    // ========================================================================
    // ZADANIE CYKLICZNE — np. "serwer co 5 sekund losuje i wysyla slowo"
    // ========================================================================
    private static final String[] WORDS = {"alfa", "beta", "gamma", "delta", "epsilon", "zeta"};

    public void startPeriodicBroadcast() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            String word = WORDS[(int) (Math.random() * WORDS.length)];
            broadcast(word); // ClientHandler.send() dopisuje \n — patrz ClientHandler
        }, 5, 5, TimeUnit.SECONDS);
    }

    public void stopPeriodicBroadcast() {
        if (scheduler != null) {
            scheduler.shutdown();
        }
    }

    // ========================================================================
    // DOSTEP DO BAZY DANYCH
    // ========================================================================
    public Database getDatabase() {
        return database;
    }

    // ========================================================================
    // PRZYKLADOWE METODY DOMENOWE DO NADPISANIA / ROZWINIECIA
    // (zostaw jako szablon — dopisz wlasna logike biznesowa zadania)
    // ========================================================================

    /** np. wyzwanie na pojedynek w grze PKN */
    public void challengeToDuel(ClientHandler challenger, String challengeeLogin) {
        if (challengeeLogin.equals(challenger.getLogin())) {
            challenger.send("Nie mozesz wyzwac samego siebie.");
            return;
        }
        ClientHandler challengee = findClientByLogin(challengeeLogin);
        if (challengee == null) {
            challenger.send("Nie znaleziono uzytkownika: " + challengeeLogin);
            return;
        }
        // startDuel(challenger, challengee); // odkomentuj gdy masz klase Duel
    }

    // ========================================================================
    // PUNKT WEJSCIA — main() ODPALAJACY SERWER W TLE + (OPCJONALNIE) OKNO GUI
    // ========================================================================
    public static void main(String[] args) {
        Server server = new Server(5000);

        // Serwer NIE moze blokowac wątku JavaFX — zawsze w osobnym wątku!
        Thread serverThread = new Thread(server::listen);
        serverThread.setDaemon(true); // zamkniecie okna => koniec wątku serwera
        serverThread.start();

        // Jezeli zadanie wymaga okna (np. rysowanie odcinkow) — odpalamy GUI:
        // javafx.application.Application.launch(DrawingApp.class, args);

        // Jezeli zadanie NIE wymaga okna (czysty serwer konsolowy) — wystarczy:
        try {
            serverThread.join();
        } catch (InterruptedException ignored) {
        }
    }
}
