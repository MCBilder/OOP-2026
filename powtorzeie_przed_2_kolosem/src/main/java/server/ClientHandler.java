package server;

import auction.Auction;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final Server server;

    private BufferedReader in;
    private PrintWriter out;

    private String login;
    private boolean authenticated = false;

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    public void send(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    private void disconnect() {
        server.removeClient(this);
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException ignored) {
        }
    }

    public String getLogin() {
        return login;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    private void handleMessage(String message) {
        message = message.trim();
        if (message.isEmpty()) return;
        if (message.matches("[0-9]+(\\.[0-9]+)?")) {
            double parsemsg = Double.parseDouble(message);
            if(server.isAuctionStatus() == true){
                Auction auction = server.getAuction();
                if(login.equals(auction.getLogin())){
                    this.send("nie mozesz przebic wlasnej oferty");
                    return;
                    }
                boolean result = auction.placeBid(this.getLogin(), parsemsg);
                if(result == false){
                    this.send("zbyt maly bet");
                }else{
                    server.broadcast(this.getLogin() + "bet " + parsemsg);
                }
            }else{
                this.send("brak aktywnych aukcji");
            }
        }
    }

    @Override
    public void run() {
        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

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
        }catch (IOException e) {
        System.err.println("Blad obslugi klienta: " + e.getMessage());
        }finally{
            disconnect();
        }
    }
}
