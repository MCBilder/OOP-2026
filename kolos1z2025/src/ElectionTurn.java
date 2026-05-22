import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ElectionTurn {
    private ArrayList<Candidate> candidates;
    private ArrayList<Vote> votes;

    //towrzy liste obiektow jako pole
    public ElectionTurn(ArrayList<Candidate> candidates) {
        this.candidates = candidates;
        //w konstruktorze tylko inicjalizuje liste
        this.votes = new ArrayList<>();
    }

    public void populate(String fileName) {
        try {
            File plik = new File(fileName);
            Scanner scanner = new Scanner(plik);
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                String linia = scanner.nextLine();
                votes.add(Vote.fromCsvLine(linia, this.candidates));
            }
            scanner.close();
        } catch (
                FileNotFoundException e) {
            System.out.println("Nie znaleziono pliku.");
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return Vote.summarize(votes, null).toString();
    }

    public Candidate winner() throws NoWinnerException {
        Vote wynik = Vote.summarize(votes, null);
        for(Candidate c : candidates){
            if(wynik.percentage(c) > 50){
                return c;
            }
        }
        throw new NoWinnerException("nie ma zwyciezcy");
    }

    public ArrayList<Candidate> runoffCandidates(){
        //null bo nie dajemy lokalizacji
        Vote wynik = Vote.summarize(votes, null);
        candidates.sort((a,b)-> wynik.votes(b) - wynik.votes(a));
        ArrayList<Candidate> result = new ArrayList<>();
        result.add(candidates.get(0));
        result.add(candidates.get(1));
        return result;
    }

    public Vote summarize() {
        return Vote.summarize(votes, null);
    }

    public Vote summarize(ArrayList<String> location) {
        return Vote.summarize(votes, location);
    }

}
