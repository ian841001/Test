import org.opencv.core.Point;

public class Line {
	public Point start;
	public Point end;
	public int len;
	
	
	
	public Line(Point start, Point end, int len) {
		this.start = start;
		this.end = end;
		this.len = len;
	}
	public Point getIntersectionPoint(Line l) {
		return getIntersectionPoint(this, l);
	}
	public static Point getIntersectionPoint(Line l1, Line l2) {
		double a1 = l1.getA();
		double b1 = l1.getB();
		double a2 = l2.getA();
		double b2 = l2.getB();
		
		double x = Line.getA(a1, b1, a2, b2);
		double y = Line.getB(a1, b1, a2, b2);
		return new Point(x, y);
	}
	public double getA() {
		return getA(this);
	}
	public static double getA(Line l) {
		return getA(l.start, l.end);
	}
	public static double getA(Point p1, Point p2) {
		return getA(p1.x, p1.y, p2.x, p2.y);
	}
	public static double getA(double x1, double y1, double x2, double y2) {
		return (y2 - y1) / (x1 * y2 - x2 * y1);
	}
	public double getB() {
		return getB(this);
	}
	public static double getB(Line l) {
		return getB(l.start, l.end);
	}
	public static double getB(Point p1, Point p2) {
		return getB(p1.x, p1.y, p2.x, p2.y);
	}
	public static double getB(double x1, double y1, double x2, double y2) {
		return (x1 - x2) / (x1 * y2 - x2 * y1);
	}
	public double getAngle() {
		return getAngle(this);
	}
	public static double getAngle(Line l) {
		return getAngle(l.start, l.end);
	}
	public static double getAngle(Point p1, Point p2) {
		return Math.atan2(p1.y - p2.y, p1.x - p2.x) * 180 / Math.PI;
	}
}
