import java.util.HashMap;

public class VoteMap extends VoivodeshipMap{
    private HashMap<String, Vote> wyniki;

    public VoteMap(HashMap<String, Vote> wyniki) {
        this.wyniki = wyniki;
    }

    private Candidate findWinner(Vote vote) {
        Candidate winner = null;
        int max = 0;
        for(Candidate c : vote.getVotesForCandidate().keySet()) {
            if(vote.votes(c) > max) {
                max = vote.votes(c);
                winner = c;
            }
        }
        return winner;
    }
    @Override
    public void saveToSvg(String filePath) {
        HashMap<String, String> colors = new HashMap<>();
        colors.put("Robert BIEDROŃ", "#FF0000");
        colors.put("Krzysztof BOSAK", "#000000");
        colors.put("Andrzej Sebastian DUDA", "#003399");
        colors.put("Szymon Franciszek HOŁOWNIA", "#00AA00");
        colors.put("Marek JAKUBIAK", "#FF6600");
        colors.put("Władysław Marcin KOSINIAK-KAMYSZ", "#006600");
        colors.put("Mirosław Mariusz PIOTROWSKI", "#8B0000");
        colors.put("Paweł Jan TANAJNO", "#FF69B4");
        colors.put("Rafał Kazimierz TRZASKOWSKI", "#FF8C00");
        colors.put("Waldemar Włodzimierz WITKOWSKI", "#808080");
        colors.put("Stanisław Józef ŻÓŁTEK", "#FFFF00");

        StringBuilder svgBuilder = new StringBuilder();
        svgBuilder.append("<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.0\" ")
                .append("width=\"497\" height=\"463\" viewBox=\"0 0 497 463\">\n");

        for(String woj : getVoivodeshipNames()) {
            Vote vote = wyniki.get(woj);
            String color = "#CCCCCC";
            if(vote != null) {
                Candidate winner = findWinner(vote);
                if(winner != null) {
                    color = colors.getOrDefault(winner.name(), "#CCCCCC");
                }
            }
            String path = getVoivodeshipPath(woj);
            svgBuilder.append(String.format("<path d=\"%s\" style=\"fill:%s\" id=\"%s\"/>\n", path, color, woj));
        }
        svgBuilder.append("</svg>");

        try {
            java.nio.file.Files.write(java.nio.file.Paths.get(filePath), svgBuilder.toString().getBytes());
            System.out.println("Map saved to: " + filePath);
        } catch (java.io.IOException e) {
            System.err.println("Error saving file: " + e.getMessage());
        }
    }

}
