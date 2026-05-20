import java.io.PrintWriter;

public class SvgScene {
    private Polygon[] arr = new Polygon[3];
    private int idx = 0;


    public void addPolygon(Polygon polygon){
         arr[idx] = polygon;
         idx++;
         if(idx == 3){
             idx = 0;
         }
    }

    public String toSvg(){
        String wynik = "";
        for(int i = 0; i < arr.length; i++){
            if(arr[i] != null){
                wynik = wynik + arr[i].toSvg();
            }
        }
        return wynik;
    }

    //exception to wywala wyjatek wrazie czego
    //ale tego wsm nei otwieram ttutaj to ktos inny to ogarnie
    public void save(String fileName) throws Exception {
        double maxW = 0;
        double maxH = 0;
        for(int i = 0; i < arr.length; i++){
            if(arr[i] != null){
                BoundingBox bb = arr[i].boundingBox();
                if(bb.x() + bb.width() > maxW) maxW = bb.x() + bb.width();
                if(bb.y() + bb.height() > maxH) maxH = bb.y() + bb.height();
            }
        }
        String wynik = "<svg width=\"" + maxW + "\" height=\"" + maxH + "\">";
        for(int i = 0; i < arr.length; i++){
            if(arr[i] != null) wynik = wynik + arr[i].toSvg();
        }
        wynik = wynik + "</svg>";
        PrintWriter writer = new PrintWriter(fileName);
        writer.print(wynik);
        writer.close();
    }
}
