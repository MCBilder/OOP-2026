package server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private static final String DB_URL = "jdbc:sqlite:database.db";

    public Database() {
        createTablesIfNotExist();
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    private void createTablesIfNotExist() {
        String usersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    login TEXT PRIMARY KEY,
                    password TEXT NOT NULL,
                    score INTEGER NOT NULL DEFAULT 0
                );
                """;

        String logTable = """
                CREATE TABLE IF NOT EXISTS conversions (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    path TEXT NOT NULL,
                    size INTEGER NOT NULL,
                    delay INTEGER NOT NULL
                );
                """;

        String auctionsTable = """
            CREATE TABLE IF NOT EXISTS auctions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                item_name TEXT NOT NULL,
                winner_login TEXT,
                final_price REAL NOT NULL
            );
            """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(usersTable);
            stmt.execute(logTable);
            stmt.execute(auctionsTable);
        } catch (SQLException e) {
            System.err.println("Blad tworzenia tabel: " + e.getMessage());
        }
    }

   public void saveAuctionResult(String itemName, String winnerLogin, double finalPrice){
        String sql = "INSERT INTO auctions (item_name, winner_login, final_price) VALUES (?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, itemName);
            ps.setString(2, winnerLogin);   // moze byc null - JDBC to obsluzy poprawnie
            ps.setDouble(3, finalPrice);
            ps.executeUpdate();              // <- executeUpdate, bo NIC nie czytasz z powrotem
        } catch (SQLException e) {
            System.err.println("Blad zapisu aukcji: " + e.getMessage());
        }
    }

    public record AuctionRecord(String itemName, String winnerLogin, double finalPrice){};

    public List<AuctionRecord> getAuctionHistory(){
        String sql = "SELECT item_name, winner_login, final_price FROM auctions ORDER BY id DESC";
        List<AuctionRecord> history = new ArrayList<>();
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String item = rs.getString("item_name");
                String winner = rs.getString("winner_login");
                double price = rs.getDouble("final_price");
                AuctionRecord record = new AuctionRecord(item, winner, price);
                history.add(record);
            }
        } catch (SQLException e) {
            System.err.println("Blad odczytu historii: " + e.getMessage());
        }

        return history;
    }

    public boolean authenticate(String login, String password) {
        String sql = "SELECT password FROM users WHERE login = ?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, login);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    return storedPassword.equals(password);
                }
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Blad autentykacji: " + e.getMessage());
            return false;
        }
    }
}
