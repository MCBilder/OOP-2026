package server;

import java.util.*;

/**
 * ============================================================================
 *  MAP - OD PODSTAW, W KONTEKSCIE ZADAN SERWEROWYCH (klient/serwer/baza).
 * ============================================================================
 *
 * Ten plik NIE jest czescia zadania - to czysta "ściągawka", zbior malych,
 * samodzielnych przykladow. Kazda metoda main_PrzykladX() mozesz uruchomic
 * niezaleznie, albo po prostu CZYTAC jako notatki.
 *
 * CZYM JEST Map? (w jednym zdaniu)
 * -----------------------------------------------------------------------
 * Map<K, V> to "slownik" - parowanie KLUCZA (Key) z WARTOSCIA (Value).
 * Podajesz klucz, dostajesz przypisana do niego wartosc. Jak slownik
 * polsko-angielski: podajesz polskie slowo (klucz), dostajesz angielskie
 * tlumaczenie (wartosc).
 *
 *   Map<String, Integer> punkty = new HashMap<>();
 *   punkty.put("alice", 150);   // klucz="alice", wartosc=150
 *   punkty.put("bob", 90);
 *
 *   int wynikAlice = punkty.get("alice");   // 150
 *
 * RÓŻNICA WZGLĘDEM List<>:
 * -----------------------------------------------------------------------
 *   List<String> - dostep przez INDEKS (numer pozycji): list.get(0)
 *   Map<K, V>     - dostep przez KLUCZ (cokolwiek): map.get("alice")
 *
 * Jesli Twoje dane naturalnie maja "nazwe/identyfikator", po ktorym chcesz
 * je wyszukiwac (login, nazwa przedmiotu, kod produktu) - to jest sygnal,
 * ze potrzebujesz Map, nie List.
 * ============================================================================
 */
public class MapCheatsheet {

    /**
     * ========================================================================
     * PRZYKLAD 1: Podstawowe operacje - put/get/containsKey/remove
     * ========================================================================
     */
    static void przyklad1_PodstawoweOperacje() {
        Map<String, Integer> leaderboard = new HashMap<>();

        // put(klucz, wartosc) - DODAJE nowy wpis albo NADPISUJE istniejacy
        leaderboard.put("alice", 150);
        leaderboard.put("bob", 90);
        leaderboard.put("alice", 200); // <- to NADPISUJE poprzednia wartosc alice (150 -> 200)!

        // get(klucz) - ODCZYTUJE wartosc. Jesli klucza NIE MA, zwraca null!
        Integer aliceScore = leaderboard.get("alice");      // 200
        Integer nieistniejacy = leaderboard.get("ktos_inny"); // null - UWAGA na to!

        // containsKey(klucz) - sprawdza, CZY klucz istnieje (bezpieczniej niz get+null check)
        boolean maAlice = leaderboard.containsKey("alice"); // true
        boolean maZenek = leaderboard.containsKey("zenek"); // false

        // remove(klucz) - usuwa wpis
        leaderboard.remove("bob");

        // size() - ile wpisow
        int iloscWpisow = leaderboard.size(); // 1 (zostala tylko alice)

        System.out.println("Alice ma: " + aliceScore + " punktow");
        System.out.println("Czy mamy Zenka: " + maZenek);
        System.out.println("Liczba wpisow: " + iloscWpisow);
    }

    /**
     * ========================================================================
     * PRZYKLAD 2: PUŁAPKA - get() na nieistniejacym kluczu zwraca null,
     * NIE wyjatek! To jest najczestszy blad/bug z Map w zadaniach.
     * ========================================================================
     */
    static void przyklad2_PulapkaNullPointerException() {
        Map<String, Integer> scores = new HashMap<>();
        scores.put("alice", 150);

        // ZLE - jesli "bob" nie istnieje, get() zwroci null,
        // a wywolanie metody NA null (np. .intValue(), czy autoboxing
        // do int w odejmowaniu) wybuchnie NullPointerException!
        //
        //   int bobScore = scores.get("bob");  // NullPointerException!
        //   (bo get() zwraca null, a "int bobScore = null" nie da sie zrobic
        //    bezposrednio - Java probuje unboxing i wywala wyjatek)

        // DOBRZE - opcja A: sprawdz containsKey PRZED uzyciem get()
        if (scores.containsKey("bob")) {
            int bobScore = scores.get("bob");
            System.out.println("Bob ma: " + bobScore);
        } else {
            System.out.println("Bob nie istnieje w mapie.");
        }

        // DOBRZE - opcja B: getOrDefault(klucz, domyslnaWartosc) - JEDNA LINIA,
        // czesto wygodniejsza niz if/containsKey
        int bobScore = scores.getOrDefault("bob", 0); // jesli "bob" nie istnieje -> 0
        System.out.println("Bob ma (z defaultem): " + bobScore);
    }

    /**
     * ========================================================================
     * PRZYKLAD 3: Iterowanie po Map - 3 sposoby
     * ========================================================================
     * To jest WAZNE dla Twojego zadania getLeaderboard()/wypisywania rankingu -
     * musisz przejsc po WSZYSTKICH wpisach mapy.
     */
    static void przyklad3_IterowaniePoMapie() {
        Map<String, Integer> scores = new HashMap<>();
        scores.put("alice", 150);
        scores.put("bob", 90);
        scores.put("mirek", 200);

        // SPOSOB A: entrySet() - dostajesz PARY (klucz + wartosc razem)
        // TO JEST NAJCZESTSZY, NAJWYGODNIEJSZY sposob - polecam ten.
        System.out.println("--- Sposob A: entrySet ---");
        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            String login = entry.getKey();
            int score = entry.getValue();
            System.out.println(login + " -> " + score);
        }

        // SPOSOB B: keySet() - dostajesz TYLKO klucze, potem get() dla kazdego
        // (mniej efektywne niz entrySet - dwa odczyty z mapy zamiast jednego)
        System.out.println("--- Sposob B: keySet ---");
        for (String login : scores.keySet()) {
            int score = scores.get(login); // <- dodatkowy odczyt
            System.out.println(login + " -> " + score);
        }

        // SPOSOB C: forEach z lambda - krotszy zapis tego samego co sposob A
        System.out.println("--- Sposob C: forEach + lambda ---");
        scores.forEach((login, score) -> {
            System.out.println(login + " -> " + score);
        });
    }

    /**
     * ========================================================================
     * PRZYKLAD 4: Sortowanie Map wedlug wartosci (DOKLADNIE Twoj getLeaderboard())
     * ========================================================================
     * Map NIE JEST domyslnie sortowana wedlug wartosci - HashMap nie ma
     * zadnej okreslonej kolejnosci. Zeby dostac "ranking od najwyzszego
     * wyniku", trzeba RECZNIE posortowac.
     */
    static Map<String, Integer> przyklad4_SortowanieWgWartosci(Map<String, Integer> scores) {
        // KROK 1: entrySet() mapy -> Stream -> sorted() -> z powrotem do Map
        // Comparator.comparing(Map.Entry::getValue) sortuje wg WARTOSCI
        // .reversed() odwraca, zeby najwyzszy wynik byl pierwszy

        Map<String, Integer> sorted = new LinkedHashMap<>(); // LinkedHashMap PAMIETA porzadek wstawiania!

        scores.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(entry -> sorted.put(entry.getKey(), entry.getValue()));

        return sorted;

        // UWAGA: zwykly HashMap NIE PAMIETA porzadku, w jakim wstawiasz wpisy!
        // Jesli zwrocisz wynik jako HashMap, kolejnosc moze sie "rozsypac"
        // przy iterowaniu, nawet jesli wlozyles je w dobrej kolejnosci.
        // LinkedHashMap NAPRAWIA to - zapamietuje kolejnosc wstawiania.
        // DLATEGO w Twoim Database.getLeaderboard() uzywalismy LinkedHashMap!
    }

    /**
     * ========================================================================
     * PRZYKLAD 5: Map jako "slownik klientow po loginie" - WZORZEC SERWEROWY
     * ========================================================================
     * To jest alternatywa dla podejscia z Twojego zadania (gdzie szukalismy
     * klienta przez PETLE po List<ClientHandler>, porownujac loginy):
     *
     *   public ClientHandler findClientByLogin(String login) {
     *       for (ClientHandler c : clients) {
     *           if (login.equals(c.getLogin())) return c;
     *       }
     *       return null;
     *   }
     *
     * Z Map<String, ClientHandler> ZAMIAST List<ClientHandler>, ta sama
     * operacja jest O(1) (natychmiastowa), nie O(n) (przeszukiwanie calej listy):
     *
     *   Map<String, ClientHandler> clientsByLogin = new ConcurrentHashMap<>();
     *
     *   // przy logowaniu:
     *   clientsByLogin.put(login, clientHandler);
     *
     *   // szukanie - JEDNA linia, BEZ petli:
     *   ClientHandler found = clientsByLogin.get(login); // null jesli nie istnieje
     *
     *   // przy rozlaczeniu:
     *   clientsByLogin.remove(login);
     *
     * CZEMU ConcurrentHashMap, NIE HashMap, w serwerze?
     * -----------------------------------------------------------------------
     * Tak jak uzywalismy CopyOnWriteArrayList (nie ArrayList) dla listy
     * klientow - bo wiele wątkow (kazdy klient = swoj wątek) moze
     * jednoczesnie czytac/modyfikowac te strukture. Zwykly HashMap NIE jest
     * bezpieczny przy wspoldzielonym dostepie z wielu wątkow - moze sie
     * "zepsuc" (np. ConcurrentModificationException albo gorzej, ciche
     * uszkodzenie danych). ConcurrentHashMap jest zaprojektowany do
     * bezpiecznego uzycia z wielu wątkow naraz.
     *
     * Czyli regula: "wiele wątkow + Map" -> ConcurrentHashMap, NIE HashMap.
     * ========================================================================
     */
    static void przyklad5_KomentarzWzorcaSerwerowego() {
        // Patrz komentarz powyzej - to jest czysto wyjasniajace, bez
        // samodzielnie wykonywalnego kodu (bo wymaga klasy ClientHandler
        // z Twojego prawdziwego projektu).
    }

    /**
     * ========================================================================
     * PRZYKLAD 6: computeIfAbsent / merge - skroty do typowych operacji
     * ========================================================================
     * Te metody sa SKROTAMI dla czestych wzorcow typu "jesli klucz nie
     * istnieje, stworz go z domyslna wartoscia" albo "zaktualizuj wartosc
     * na podstawie starej".
     */
    static void przyklad6_ComputeIfAbsentIMerge() {
        // PRZYPADEK: liczenie, ile razy kazde slowo przyszlo od klienta
        // (np. zadanie z lista slow z serwera - statystyka czestotliwosci)
        Map<String, Integer> wordCounts = new HashMap<>();

        String[] incomingWords = {"alfa", "beta", "alfa", "alfa", "beta", "gamma"};

        // SPOSOB "RECZNY" (dziala, ale rozwlekly):
        for (String word : incomingWords) {
            if (wordCounts.containsKey(word)) {
                wordCounts.put(word, wordCounts.get(word) + 1);
            } else {
                wordCounts.put(word, 1);
            }
        }
        System.out.println("Sposob reczny: " + wordCounts);

        // SPOSOB ZWIEZLY - merge(klucz, wartoscDoPolaczenia, funkcjaLaczenia):
        // jesli klucz NIE istnieje -> wstawia wartoscDoPolaczenia (tu: 1)
        // jesli klucz JUZ istnieje -> woła funkcje (staraWartosc, nowaWartosc) -> wynik
        Map<String, Integer> wordCounts2 = new HashMap<>();
        for (String word : incomingWords) {
            wordCounts2.merge(word, 1, Integer::sum);
        }
        System.out.println("Sposob merge: " + wordCounts2);
    }

    // ============================================================================
    // main() - odpal ten plik, zeby zobaczyc wszystkie przyklady na konsoli
    // ============================================================================
    public static void main(String[] args) {
        System.out.println("=== PRZYKLAD 1: podstawowe operacje ===");
        przyklad1_PodstawoweOperacje();

        System.out.println("\n=== PRZYKLAD 2: pulapka null ===");
        przyklad2_PulapkaNullPointerException();

        System.out.println("\n=== PRZYKLAD 3: iterowanie ===");
        przyklad3_IterowaniePoMapie();

        System.out.println("\n=== PRZYKLAD 4: sortowanie wg wartosci ===");
        Map<String, Integer> scores = new HashMap<>();
        scores.put("alice", 150);
        scores.put("bob", 90);
        scores.put("mirek", 200);
        Map<String, Integer> sorted = przyklad4_SortowanieWgWartosci(scores);
        System.out.println("Ranking (od najwyzszego): " + sorted);

        System.out.println("\n=== PRZYKLAD 6: computeIfAbsent/merge ===");
        przyklad6_ComputeIfAbsentIMerge();
    }
}
