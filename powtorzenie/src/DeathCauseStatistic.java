public class DeathCauseStatistic {
    private String ICD10;
    private int[] deadRate;

    public DeathCauseStatistic(String ICD10, int[] deadRate) {
        this.ICD10 = ICD10;
        this.deadRate = deadRate;
    }

    public void setICD10(String ICD10) {
        this.ICD10 = ICD10;
    }

    public int getDeadRate(int idx) {
        return deadRate[idx];
    }

    public void setDeadRate(int[] deadRate) {
        this.deadRate = deadRate;
    }

    //akcesor (getter)
    public String getICD10() {
        return ICD10;
    }

    //--------------------------------------
    // UWAGA!!!! pamietaj ze jak zwraca obiekt ot typ to klasa ta co jest obiekt
    //--------------------------------------
    public static  DeathCauseStatistic fromCsvLine(String linia){
        //"\t" to nie spacja tylko tabulator
        //split dzieli po danym czynniku na kolejne elementy tablicy
        String[] arrStr = linia.split("\t");
        if(arrStr.length < 2){
           return  null;
        }
        //bierzemy 2 czesc tablicy i dzielimy cyfry w stringu pomijajac ","
        String[] arrStrForInt = arrStr[1].split(",");
        int[] arrInt = new int[arrStrForInt.length];
        for(int i = 0; i < arrInt.length; ++i){
            //equals drobi ze jak jest rowne "tutaj cos" to wsm tyle xd
            if(arrStrForInt[i].equals("-")){
                arrInt[i] = 0;
            }else{
                //zamieniamy cyfry w stringu na int i dopisujemy do tablicy int
                arrInt[i] = Integer.parseInt(arrStrForInt[i]);
            }
        }
        DeathCauseStatistic obiekt = new DeathCauseStatistic(arrStr[0], arrInt);

        return obiekt;
    }

    //--------------------------------------
    // UWAGA!!!! pamietaj ze jak zwraca obiekt ot typ to klasa ta co jest obiekt
    //--------------------------------------
    public AgeBracketDeaths DefineAge(int age){
        //daje nam nr idx bo kazda grupa wiekowa jest co 5
        int idx = age / 5;
        //daje nam dolna granice grupy bo np idx = 5 * 5 to 25
        int young = idx * 5;
        //jestesmy w dolej granicy wiec zostaje nam + 4 lata w grupie
        int old = young + 4;
        //to poprostu wyniki przypisane do danej grupy wiekowej z tablicy
        int deathCount = this.deadRate[idx];
        AgeBracketDeaths obiekt = new AgeBracketDeaths(young, old, deathCount);
        return obiekt;
    }

    public static class AgeBracketDeaths{
        public final int young;
        public final int old;
        public final int deathCount;

        //--------------------------------------
        // UWAGA!!!! jak jest final to musi byc konstruktor
        //--------------------------------------
        public AgeBracketDeaths(int young, int old, int deathCount) {
            this.young = young;
            this.old = old;
            this.deathCount = deathCount;
        }
    }
}
