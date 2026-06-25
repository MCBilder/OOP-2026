package client;

import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

/**
 * ============================================================================
 *  UNIWERSALNY NetworkClient — TCP, wzorzec "polacz + wysylaj + odbieraj".
 * ============================================================================
 * Uzycie:
 *   NetworkClient client = new NetworkClient("localhost", 5000);
 *   client.onMessage(line -> Platform.runLater(() -> { ... aktualizacja GUI ... }));
 *   client.connect();
 *   client.send("jakas wiadomosc");
 *
 * WAZNE: callback onMessage NIE jest na wątku JavaFX — kazda aktualizacja
 * kontrolek (Label, ListView, etc.) musi byc owiniete w Platform.runLater().
 * ============================================================================
 */
public class NetworkClient {

    private final String host;
    private final int port;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private Consumer<String> messageListener;
    private volatile boolean running = false;

    public NetworkClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /** Ustawia callback wywolywany dla kazdej linii przyslanej przez serwer. */
    public void onMessage(Consumer<String> listener) {
        this.messageListener = listener;
    }

    /** Laczy sie z serwerem i startuje wątek nasluchujacy na przychodzace wiadomosci. */
    public void connect() throws IOException {
        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
        running = true;

        Thread listenerThread = new Thread(this::listenLoop);
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    private void listenLoop() {
        try {
            String line;
            while (running && (line = in.readLine()) != null) {
                if (messageListener != null) {
                    messageListener.accept(line);
                }
            }
        } catch (IOException e) {
            if (running) {
                System.err.println("Polaczenie z serwerem przerwane: " + e.getMessage());
            }
        }
    }

    /** Wysyla linie tekstu do serwera (np. login, gest, kolor, odcinek). */
    public void send(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    /** Odczytuje JEDNA linie odpowiedzi synchronicznie — uzyteczne np. podczas logowania. */
    public String readLineBlocking() throws IOException {
        return in.readLine();
    }

    public void disconnect() {
        running = false;
        try {
            if (socket != null) socket.close();
        } catch (IOException ignored) {
        }
    }

    // ========================================================================
    // WYSYLANIE / ODBIERANIE PLIKU BINARNEGO (np. PNG) — symetrycznie do servera
    // Protokol: 8 bajtow dlugosci (long) + tresc pliku.
    // ========================================================================
    public void sendFile(File file) throws IOException {
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

    public void receiveFile(String savePath) throws IOException {
        DataInputStream dataIn = new DataInputStream(socket.getInputStream());
        long size = dataIn.readLong();
        try (FileOutputStream fos = new FileOutputStream(savePath)) {
            byte[] buffer = new byte[4096];
            long remaining = size;
            while (remaining > 0) {
                int read = dataIn.read(buffer, 0, (int) Math.min(buffer.length, remaining));
                if (read == -1) break;
                fos.write(buffer, 0, read);
                remaining -= read;
            }
        }
    }
}
