public class Main {
    public static void main(String[] args) {
        Point p1 = new Point(10,5);
//        p1.setX(10);
//        p1.setY(5);
        System.out.printf(p1.getX() + " " + p1.getY() + '\n');
        System.out.printf(p1.toString() + '\n');
        System.out.println(p1.toSvg());

        Point p2 = new Point(15,10);
//        p2.setY(15);
//        p2.setY(10);
        System.out.printf(p2.getX() + " " + p2.getY() + '\n');

        //-------------------------------
        // translate() modyfikuje ten sam punkt, więc nie trzeba tworzyć ani przypisywać nowego obiektu.
        //-------------------------------

        p2.translate(5,5);
        System.out.printf(p2.toString() + '\n');

        //-------------------------------
        // Jeżeli tworzy się NOWY punkt,  musimy go przypisać do zmiennej aby go zachować.
        //-------------------------------

        Point p3 = p2.translated(10,10);
        System.out.printf(p3.getX() + " " + p3.getY() + '\n');
    }
}