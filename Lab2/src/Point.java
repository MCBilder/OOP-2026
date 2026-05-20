
public class Point {
    //Pola
    private double x;
    private double y;

    //Gettery
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    //Settery
    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    //konstruktor
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point(){
        this.x = 0.0;
        this.y = 0.0;
    }

    //konstruktor kopiujacy
    public Point(Point other) {
        this.x = other.getX();
        this.y = other.getY();
    }


    public String toString(){
        return "x: " + this.x + "\nY: " + this.y;
    }

    public String toSvg(){
        return "<circle r=\"45\" cx=\"" + this.x + "\" cy=\"" + this.y + "\" fill=\"red\" />";
    }
//-------------------------------
//jezeli nie zwraca nic to VOID
//-------------------------------

    public void  translate(double dx, double dy){
        this.x = this.x + dx;
        this.y = this.y + dy;
    }

//-------------------------------
// Zwracamy Point bo chcemy oddać cały punkt (x i y), a double mogłoby przechować tylko jedną liczbę.
//-------------------------------

    public Point translated(double dx, double dy){
        Point result = new Point(x + dx, y + dy);
        return result;
    }
}