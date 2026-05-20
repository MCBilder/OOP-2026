import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ICDCodeTabularOptimizedForMemory implements ICDCodeTabula {
    private String fileName;

    public ICDCodeTabularOptimizedForMemory(String fileName) {
        this.fileName = fileName;
    }

    public String getDescription(String illnesName){
        try(Scanner scanner = new Scanner(new File(fileName))){
            for(int i = 1; i < 88; ++i){
                scanner.nextLine();
            }
            while (scanner.hasNextLine()) {
                String linia = scanner.nextLine();
                if (linia.length() >= 3 && Character.isLetter(linia.charAt(0)) && Character.isDigit(linia.charAt(1)) && Character.isDigit(linia.charAt(2))) {
                    String[] liniaSplit = linia.split(" ", 2);
                    if (linia.startsWith(illnesName)) {
                        return liniaSplit[1];
                    }
                }
            }
            throw new IndexOutOfBoundsException("Brak kodu w bazie");
        }catch (FileNotFoundException e) {

            System.out.println("Nie znaleziono pliku.");
            e.printStackTrace();
        }
        throw new IndexOutOfBoundsException("Brak kodu w bazie");
    }
}
