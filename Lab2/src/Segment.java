public class Segment {
    private Point start;
    private Point end;

    //Getery
    public Point getStart() {
        return start;
    }
    public Point getEnd() {
        return end;
    }

    //Setery
    public void setStart(Point start) {
        this.start = start;
    }
    public void setEnd(Point end) {
        this.end = end;
    }

    //konstruktor robi kopie przec co segment przestaje byc wrazliwy na zmiany
    public Segment(Point start, Point end) {
        this.start = new Point(start);
        this.end = new Point(end);
    }

    public double length(){
        return Math.sqrt(Math.pow(this.end.getX() - this.start.getX(), 2) + Math.pow(this.end.getY() - this.start.getY(), 2));
    }

    public Segment maxSegment(Segment[] segments){
        int max_len = 0;
        Segment max_segment = segments[0];
        for(Segment s : segments){
            if (s.length() > max_len){
                max_segment = s;
            }
        }
        return max_segment;
    }

    @Override
    public String toString() {
        return "Segment{" + "start=" + start + ", end=" + end + '}';
    }
}