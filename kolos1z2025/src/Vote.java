import javax.sound.sampled.FloatControl;
import java.util.ArrayList;
import java.util.HashMap;

public class Vote {
    private HashMap<Candidate, Integer> votesForCandidate;
    private ArrayList<String> location;
    private int totalVotes;

    public Vote() {
        //twore mape i tablice w obiekcie
        this.location = new ArrayList<>();
        this.votesForCandidate = new HashMap<>();
        totalVotes = 0;
    }

    public ArrayList<String> getLocation() {
        return location;
    }

    public HashMap<Candidate, Integer> getVotesForCandidate() {
        return votesForCandidate;
    }

    public int getTotalVotes() {
        return totalVotes;
    }

    public static Vote fromCsvLine(String linia, ArrayList<Candidate> candidates){
        String[] wynik = linia.split(",");
        Vote vote = new Vote();

        //dodaje stringi do listy tak jak wymagane
        vote.location.add(wynik[2]);
        vote.location.add(wynik[1]);
        vote.location.add(wynik[0]);
        for(int i = 3; i < wynik.length; ++i){
            //zamienia na int
            int number = Integer.parseInt(wynik[i]);
            //get(i) pozwala wziac wymagany obiekt ktory chcemy pod danym idx
            vote.votesForCandidate.put(candidates.get(i - 3), number);
        }
        return vote;
    }

    //static bo nie operuje na konkretnym obiekcie Vote
    //przyjmuje listę z zewnątrz i tworzy nowy obiekt
    public static Vote summarize(ArrayList<Vote> votes, ArrayList<String> location){
        Vote wynik = new Vote();
        for(Vote v : votes){
            //przechodzi po mapie i bierze kolejno klucz czyli imie kandydata
            for(Candidate c : v.votesForCandidate.keySet()){
                v.votesForCandidate.get(c);
                if(wynik.votesForCandidate.containsKey(c)){
                    //c to klucz kandydat
                    //wynik.votesForCandidate.get(c) to glosy na kandydata
                    wynik.votesForCandidate.put(c, wynik.votesForCandidate.get(c) + v.votesForCandidate.get(c));
                } else {
                    wynik.votesForCandidate.put(c, v.votesForCandidate.get(c));
                }
            }
        }//tutaj jets teraz zmienna z procentami
        for(Candidate c : wynik.votesForCandidate.keySet()){
            wynik.totalVotes += wynik.votesForCandidate.get(c);
        }
        wynik.location = location;
        return wynik;
    }

    public int votes(Candidate candidate){
        //get starczy bo jak nie mato da null
        return  votesForCandidate.get(candidate);
    }

//    public double percentage(Candidate candidate){
//        //biore glosy kandydata
//        int candidatevotes = votesForCandidate.get(candidate);
//        int sum = 0;
//        //iterujemy po kluczach i get bierze nam value int i dodaje do sumy
//        for(Candidate c : votesForCandidate.keySet()){
//            sum += votesForCandidate.get(c);
//        }
//        //zamieniam typ przed zwroceniem zeby procenty sei zgadzaly
//        return (double) candidatevotes / sum * 100;
//    }
    //obliczneia z wyzej sa w lini 31
        public double percentage(Candidate candidate){
            return (double) votes(candidate) / totalVotes * 100;
        }

    @Override
    public String toString() {
        String result = "";
        for(Candidate c : votesForCandidate.keySet()){
            //bierzemy imie kandydata i procenty
            result += c.name() + " " + percentage(c) + "\n";
        }
        return result;
    }

    public static ArrayList<Vote> filterByLocation(ArrayList<Vote> votes, ArrayList<String> location){
        ArrayList<Vote> result = new ArrayList<>();
        for(Vote v : votes){
            if(v.getLocation().containsAll(location)){
                result.add(v);
            }
        }
        return result;
    }
}
