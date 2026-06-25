package server;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ============================================================================
 *  UNIWERSALNA Database — SQLite, JDBC, bez ORM.
 * ============================================================================
 * Sterownik: org.xerial:sqlite-jdbc (dodany w pom.xml).
 * URL polaczenia do pliku: "jdbc:sqlite:nazwa_pliku.db"
 *
 * Zawiera trzy najczesciej powtarzajace sie operacje z kolokwiow:
 *   1) authenticate(login, password)      — sprawdzenie danych logowania
 *   2) updateLeaderboard(winner, loser)   — +1/-1 punkt
 *   3) getLeaderboard()                   — ranking malejaco
 *   4) insertLogRow(...)                  — wpis podsumowania (np. plik+rozmiar+czas)
 *
 * Na kolokwium: zmien nazwy tabel/kolumn na te z treści zadania, usun
 * niepotrzebne metody.
 * ============================================================================
 */
public class Database {

    private static final String DB_URL = "jdbc:sqlite:database.db";

    public Database() {
        createTablesIfNotExist();
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    /** Tworzy tabele, jezeli plik bazy/jej struktura jeszcze nie istnieje. */
    private void createTablesIfNotExist() {
        String usersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    login TEXT PRIMARY KEY,
                    password TEXT NOT NULL,
                    score INTEGER NOT NULL DEFAULT 0
                );
                """;

        // przykladowa tabela "logow" - np. dla zadania z PNG+blur (path/size/delay)
        String logTable = """
                CREATE TABLE IF NOT EXISTS conversions (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    path TEXT NOT NULL,
                    size INTEGER NOT NULL,
                    delay INTEGER NOT NULL
                );
                """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(usersTable);
            stmt.execute(logTable);
        } catch (SQLException e) {
            System.err.println("Blad tworzenia tabel: " + e.getMessage());
        }
    }

    // ========================================================================
    // AUTENTYKACJA
    // ========================================================================
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
                return false; // brak takiego uzytkownika
            }
        } catch (SQLException e) {
            System.err.println("Blad autentykacji: " + e.getMessage());
            return false;
        }
    }

    // ========================================================================
    // RANKING / LEADERBOARD
    // ========================================================================
    public void updateLeaderboard(String winnerLogin, String loserLogin) {
        String updateWinner = "UPDATE users SET score = score + 1 WHERE login = ?";
        String updateLoser = "UPDATE users SET score = score - 1 WHERE login = ?";

        try (Connection conn = connect()) {
            conn.setAutoCommit(false);
            try (PreparedStatement psWin = conn.prepareStatement(updateWinner);
                 PreparedStatement psLose = conn.prepareStatement(updateLoser)) {
                psWin.setString(1, winnerLogin);
                psWin.executeUpdate();

                psLose.setString(1, loserLogin);
                psLose.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            System.err.println("Blad aktualizacji rankingu: " + e.getMessage());
        }
    }

    /** Zwraca mape login -> punkty, posortowana malejaco po punktach. */
    public Map<String, Integer> getLeaderboard() {
        String sql = "SELECT login, score FROM users ORDER BY score DESC";
        Map<String, Integer> result = new LinkedHashMap<>(); // zachowuje porzadek wstawiania

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                result.put(rs.getString("login"), rs.getInt("score"));
            }
        } catch (SQLException e) {
            System.err.println("Blad odczytu rankingu: " + e.getMessage());
        }
        return result;
    }

    // ========================================================================
    // PRZYKLAD: zapis wpisu typu "log konwersji" (path/size/delay) — zadanie z blur
    // ========================================================================
    public void insertConversionLog(String path, int size, long delayMs) {
        String sql = "INSERT INTO conversions (path, size, delay) VALUES (?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, path);
            ps.setInt(2, size);
            ps.setLong(3, delayMs);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Blad zapisu logu: " + e.getMessage());
        }
    }
}
