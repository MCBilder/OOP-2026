import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class DeathCauseStatisticList {
    public ArrayList<DeathCauseStatistic> lista = new ArrayList<>();
    public void repopulate(String fileName){
        lista.clear();
        try {
            // plik
            File plik = new File(fileName);

            // scanner
            Scanner scanner = new Scanner(plik);

            //robie tak bo 1 linia to nie dane a 2 to wyniki ogolem
            scanner.nextLine();
            scanner.nextLine();
            // odczyt pliku
            while(scanner.hasNextLine()) {
                String linia = scanner.nextLine();
                DeathCauseStatistic obiekt = DeathCauseStatistic.fromCsvLine(linia);
                if(obiekt != null){
                    lista.add(obiekt);
                }
            }
            scanner.close();

        } catch(Exception e) {

            e.printStackTrace();
        }
    }

    public ArrayList<DeathCauseStatistic> mostDeadlyDiseases(int age, int n){
        int idx = age / 5;
        lista.sort((a,b) -> b.getDeadRate(idx) - a.getDeadRate(idx));
        ArrayList<DeathCauseStatistic> wynik = new ArrayList<>();
        for(int i = 0; i < n; i++) {
            //get pobiera element z listy
            wynik.add(lista.get(i));
        }
        return wynik;
    }
}
