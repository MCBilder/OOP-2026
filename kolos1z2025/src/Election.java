import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Election {
    private ArrayList<Candidate> candidates;
    private ElectionTurn firstTurn;
    private ElectionTurn secondTurn;
    private Candidate winner;

    public ElectionTurn getFirstTurn() {
        return firstTurn;
    }
    public ElectionTurn getSecondTurn() {
        return secondTurn;
    }
    public Candidate getWinner() {
        return winner;
    }

    public ArrayList<Candidate> getCandidates() {
        return candidates;
    }

    public Election() {
        this.candidates = new ArrayList<>();
        this.firstTurn = new ElectionTurn(candidates);
        this.secondTurn = null;
    }

    public ArrayList<Candidate> candidatesCopy(){
        ArrayList<Candidate> candidatesCopy = new ArrayList<Candidate>(candidates);
        return candidatesCopy;
    }

    public void populateCandidates(String fileName) {
        try {
            File plik = new File(fileName);
            Scanner scanner = new Scanner(plik);
            while (scanner.hasNextLine()) {
                String linia = scanner.nextLine();
                Candidate candidate = new Candidate(linia);
                candidates.add(candidate);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Nie znaleziono pliku.");
            e.printStackTrace();
        }
    }

    public void populate(String fileName){
        populateCandidates(fileName);
        firstTurn.populate("1.csv");
        try{
            winner = firstTurn.winner();
        }catch (NoWinnerException e1){
            this.secondTurn = new ElectionTurn(firstTurn.runoffCandidates());
            secondTurn.populate("2.csv");
            try {
                winner = secondTurn.winner();
            }catch (NoWinnerException e2){
            }
        }
    }
}
