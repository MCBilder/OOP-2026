public class Main {
    public static void main(String[] args) {
        Point p1 = new Point();
        p1.x = 10;
        p1.y = 5;
        System.out.printf(p1.x + " " + p1.y + '\n');
        System.out.printf(p1.toString() + '\n');
        System.out.println(p1.toSvg());

        Point p2 = new Point();
        p2.x = 15;
        p2.y = 10;
        System.out.printf(p2.x + " " + p2.y + '\n');

        //-------------------------------
        // translate() modyfikuje ten sam punkt, więc nie trzeba tworzyć ani przypisywać nowego obiektu.
        //-------------------------------

        p2.translate(5,5);
        System.out.printf(p2.toString() + '\n');

        //-------------------------------
        // Jeżeli tworzy się NOWY punkt,  musimy go przypisać do zmiennej aby go zachować.
        //-------------------------------
        
        Point p3 = p2.translated(10,10);
        System.out.printf(p3.x + " " + p3.y + '\n');
    }
}