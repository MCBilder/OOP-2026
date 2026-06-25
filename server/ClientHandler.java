package server;

import java.io.*;
import java.net.Socket;

/**
 * ============================================================================
 *  UNIWERSALNY ClientHandler — obsluga JEDNEGO klienta po stronie serwera.
 * ============================================================================
 * Implementuje Runnable, bo:
 *   - w wariancie wielu-klientow: odpalany jako `new Thread(handler).start()`
 *   - w wariancie jeden-klient-na-raz: odpalany blokujaco jako `handler.run()`
 *
 * Zawiera w komentarzach WSZYSTKIE typowe schematy parsowania wiadomosci,
 * jakie pojawialy sie w zadaniach:
 *   1. login/haslo (autentykacja)
 *   2. tekst wolny (np. wyzwanie na pojedynek po loginie)
 *   3. gest "r"/"p"/"s"
 *   4. kolor szesnastkowy "RRGGBB"
 *   5. cztery floaty "x1 y1 x2 y2" (odcinek)
 *   6. plik binarny (PNG) — odbior po rozmiarze / do EOF
 * ============================================================================
 */
public class ClientHandler implements Runnable {

    private final Socket socket;
    private final Server server;

    private BufferedReader in;   // do czytania linii tekstowych
    private PrintWriter out;     // do wysylania linii tekstowych

    private String login;        // zapamietany login po autentykacji
    private boolean authenticated = false;

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            //jezeli polskei zaki
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            //jezeli bez roznicy
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);

            // ----------------------------------------------------------------
            // BLOK 1: AUTENTYKACJA (odkomentuj gdy zadanie tego wymaga)
            // ----------------------------------------------------------------
            /*
            send("LOGIN?");
            String loginInput = in.readLine();
            send("PASSWORD?");
            String passwordInput = in.readLine();

            if (server.getDatabase().authenticate(loginInput, passwordInput)) {
                this.login = loginInput;
                this.authenticated = true;
                send("OK");
            } else {
                send("AUTH_FAILED");
                disconnect();
                return;
            }
            */

            // ----------------------------------------------------------------
            // BLOK 2: ODBIOR PLIKU BINARNEGO (np. PNG) — zrob to PRZED petla
            // tekstowa, bo po przejsciu na strumien binarny nie da sie
            // bezpiecznie wracac do BufferedReader na tym samym sockecie.
            // ----------------------------------------------------------------
            /*
            receiveFile("images/" + timestampName() + ".png");
            */

            // ----------------------------------------------------------------
            // BLOK 3: GLOWNA PETLA — odbior linii tekstowych od klienta
            // ----------------------------------------------------------------
            String line;
            while ((line = in.readLine()) != null) {
                handleMessage(line);
            }

        } catch (IOException e) {
            System.err.println("Blad obslugi klienta: " + e.getMessage());
        } finally {
            disconnect();
        }
    }

    // ============================================================================
    // PARSOWANIE WIADOMOSCI — wybierz / zlacz wzorce wedlug treci zadania
    // ============================================================================
    private void handleMessage(String message) {
        message = message.trim();
        if (message.isEmpty()) return;

        // --- Wzorzec: kolor szesnastkowy "RRGGBB" (6 cyfr hex) ---
        if (message.matches("[0-9a-fA-F]{6}")) {
            // np. javafx.scene.paint.Color.web("#" + message)
            // zapamietaj kolor przypisany do TEGO klienta (pole w klasie)
            return;
        }

        // --- Wzorzec: cztery floaty "x1 y1 x2 y2" (odcinek) ---
        String[] parts = message.split("\\s+");
        if (parts.length == 4 && message.matches("[-\\d.\\s]+")) {
            try {
                double x1 = Double.parseDouble(parts[0]);
                double y1 = Double.parseDouble(parts[1]);
                double x2 = Double.parseDouble(parts[2]);
                double y2 = Double.parseDouble(parts[3]);
                // np. Platform.runLater(() -> canvasController.drawLine(x1, y1, x2, y2, myColor));
            } catch (NumberFormatException ignored) {
            }
            return;
        }

        // --- Wzorzec: pojedynczy gest "r"/"p"/"s" (gra w PKN) ---
        if (message.equals("r") || message.equals("p") || message.equals("s")) {
            // Gesture gesture = Gesture.fromString(message);
            // makeGesture(gesture); // metoda odziedziczona z Player
            return;
        }

        // --- Wzorzec: tekst wolny, np. login przeciwnika do wyzwania ---
        // server.challengeToDuel(this, message);
    }

    // ============================================================================
    // WYSYLANIE WIADOMOSCI DO TEGO KLIENTA
    // ============================================================================
    public void send(String message) {
        if (out != null) {
            out.println(message); // println dopisuje znak konca linii
        }
    }

    // ============================================================================
    // ODBIOR PLIKU BINARNEGO (np. PNG) PO LICZBIE BAJTOW Z NAGLOWKA
    // Najprostszy, niezawodny protokol: klient najpierw wysyla 8-bajtowy
    // naglowek z dlugoscia pliku (long), potem same bajty.
    // ============================================================================
    private void receiveFile(String path) throws IOException {
        File dir = new File("images");
        if (!dir.exists()) dir.mkdirs();

        DataInputStream dataIn = new DataInputStream(socket.getInputStream());
        long fileSize = dataIn.readLong();

        try (FileOutputStream fos = new FileOutputStream(path)) {
            byte[] buffer = new byte[4096];
            long remaining = fileSize;
            while (remaining > 0) {
                int read = dataIn.read(buffer, 0, (int) Math.min(buffer.length, remaining));
                if (read == -1) break;
                fos.write(buffer, 0, read);
                remaining -= read;
            }
        }
    }

    /** Wysylka pliku binarnego z powrotem do klienta, tym samym protokolem (dlugosc + bajty) */
    private void sendFile(String path) throws IOException {
        File file = new File(path);
        DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());
        dataOut.writeLong(file.length());
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[4096];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                dataOut.write(buffer, 0, read);
            }
        }
        dataOut.flush();
    }

    private String timestampName() {
        return new java.text.SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new java.util.Date());
    }

    // ============================================================================
    // ROZLACZENIE KLIENTA
    // ============================================================================
    private void disconnect() {
        server.removeClient(this);
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException ignored) {
        }
    }

    // ============================================================================
    // GETTERY / SETTERY
    // ============================================================================
    public String getLogin() {
        return login;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }
}
