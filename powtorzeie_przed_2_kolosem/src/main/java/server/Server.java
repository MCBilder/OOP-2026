package server;

import auction.Auction;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Server {
    private final int port;
    private ServerSocket serverSocket;
    private final List<ClientHandler> clients = new CopyOnWriteArrayList<>();
    private final Database database = new Database();
    private Auction auction;

    private boolean auctionStatus;

    public Server(int port) {
        this.port = port;
        auctionStatus = false;
    }

    public void listen() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Serwer wystartowal na porcie " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(clientSocket, this);
                addClient(handler);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            System.err.println("Blad serwera: " + e.getMessage());
        }
    }

    public void addClient(ClientHandler client) {
        clients.add(client);
        System.out.println("Klient dolaczyl. Liczba klientow: " + clients.size());
    }

    public void removeClient(ClientHandler client) {
        clients.remove(client);
        System.out.println("Klient odlaczyl. Liczba klientow: " + clients.size());
    }

    public Database getDatabase() {
        return database;
    }

    public boolean isAuctionStatus() {
        return auctionStatus;
    }

    public Auction getAuction() {
        return auction;
    }

    public void sendToClient(ClientHandler client, String message) {
        client.send(message);
    }

    public void broadcast(String message) {
        for (ClientHandler c : clients) {
            c.send(message);
        }
    }

    private void startAuction(String itemName, double startingPrice){

        if(auctionStatus == false){
            auction = new Auction(itemName, startingPrice);
            auctionStatus = true;
            this.broadcast("Aukcja trwa \n-------------\n" + itemName + ", " + startingPrice);
            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.schedule(() -> {
                endAuction();
            }, 60, TimeUnit.SECONDS);
        }else {
            System.out.println("Aukcja trwa, zaczekaj az sie skonczy");
        }
    }

    private void endAuction(){
        this.broadcast("aukcje wygrywa: " + auction.getLogin());
        database.saveAuctionResult(auction.getName(), auction.getLogin(), auction.getMaxBid());

        List<Database.AuctionRecord> history = database.getAuctionHistory();
        for (Database.AuctionRecord record : history) {
            System.out.println(record);
        }

        auctionStatus = false;
        auction = null;
    }


    public static void main(String[] args) {
        Server server = new Server(5000);
        server.startAuction("Car", 1000);
        server.listen();
    }
}
