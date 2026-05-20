import java.util.Arrays;

public class Polygon {
    private  Point[] arr1;

    //
    public Polygon(Point[] arr1) {
        this.arr1 = new Point[arr1.length];
        for(int i = 0; i < arr1.length;++i){
            this.arr1[i] = new Point(arr1[i]);
        }
    }

    //konstruktor kopiujacy, gleboka kopia tablicy
    public Polygon(Polygon other){
        this.arr1 = new Point[other.arr1.length];
        for(int i = 0; i < arr1.length;++i){
            this.arr1[i] = new Point(other.arr1[i]);
        }
    }

    @Override
    public String toString() {
        //Arrays.toString(arr1) to metoda co przechodzi po tablicy
        //i wywoluje toString dla kazdego elementu a potem
        //klei to w jedno
        return "Polygon{" + "arr1=" + Arrays.toString(arr1) + '}';
    }

    public String toSvg(){
        String wynik = "";
        for(int i = 0; i < arr1.length; i++){
            wynik = wynik + arr1[i].getX() + "," + arr1[i].getY() + " ";
        }
        return "<polygon points=\"" + wynik + "\"/>";
    }

    public BoundingBox boundingBox(){
        double minX;
        double minY;
        double maxX;
        double maxY;

        minX = arr1[0].getX();
        minY = arr1[0].getY();
        maxX = arr1[0].getX();
        maxY = arr1[0].getY();
        for(int i = 1; i < arr1.length; i++){
            if(minX > arr1[i].getX()){
                minX = arr1[i].getX();
            }
            if(minY > arr1[i].getY()){
                minY = arr1[i].getY();
            }
            if(maxX < arr1[i].getX()) {
                maxX = arr1[i].getX();
            }
            if(maxY < arr1[i].getY()){
                maxY = arr1[i].getY();
            }
        }
        double width = maxX - minX;
        double height = maxY - minY;
        BoundingBox object = new BoundingBox(minX, minY, width, height);
        return object;
    }


}
