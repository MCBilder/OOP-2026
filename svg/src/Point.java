
public class Point {
    //POLA
    public double x;
    public double y;

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
        Point result = new Point();
        result.x = this.x + dx;
        result.y = this.y + dy;
        return result;
    }
}
