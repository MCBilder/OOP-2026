import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class ICDCodeTabularOptimizedForTime implements ICDCodeTabula{
    private HashMap<String, String> dane;

    public ICDCodeTabularOptimizedForTime(String fileName){
        try{
            dane = new HashMap<>();
            File plik = new File(fileName);
            Scanner scanner = new Scanner(plik);
            for(int i = 1; i < 88; ++i){
                scanner.nextLine();
            }
            while (scanner.hasNextLine()){

                String linia = scanner.nextLine();
                if(linia.length() >= 3 && Character.isLetter(linia.charAt(0)) && Character.isDigit(linia.charAt(1)) && Character.isDigit(linia.charAt(2))){
                    String[] danieLinia = linia.split(" ", 2);
                    //put dodaje dane do mapy
                    dane.put(danieLinia[0], danieLinia[1]);
                }
            }
            scanner.close();
        }catch (FileNotFoundException e) {

            System.out.println("Nie znaleziono pliku.");
            e.printStackTrace();
        }
    }

    public String getDescription(String illnesName){
        if(!dane.containsKey(illnesName)){
            throw new IndexOutOfBoundsException("Brak kodu w bazie");
        }
        //get przeszukuje mape w poszukiwaniu slowa
        return dane.get(illnesName);
    }

}
