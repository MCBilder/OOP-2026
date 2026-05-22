import java.util.ArrayList;
import java.util.HashMap;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //to dlatego ze sumarize przyjmuje array a nie striga
        ArrayList<String> loc = new ArrayList<>();
        loc.add("dolnośląskie");

        Election election = new Election();
        election.populate("kandydaci.txt");
        //zeby zadzialal to string w electionTurn musialem zrobic tosring wywolujacy tostring xd
        System.out.println(election.getFirstTurn().toString()+ "\n");
        System.out.println(election.getFirstTurn().summarize());
        System.out.println(election.getFirstTurn().summarize(loc));

        VoivodeshipMap map = new VoivodeshipMap();
        map.saveToSvg("mapa.svg");

        HashMap<String, Vote> wyniki = new HashMap<>();
        for(String woj : map.getVoivodeshipNames()) {
            loc.add(woj);
            wyniki.put(woj, election.getSecondTurn().summarize(loc));
        }

        VoteMap voteMap = new VoteMap(wyniki);
        voteMap.saveToSvg("mapa_wyniki.svg");
        System.out.println(wyniki.get("mazowieckie"));
    }
}