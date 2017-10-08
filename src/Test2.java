import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.swing.JFrame;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Test2 {

	
static PrintStream ps = System.out;
	
	static int status = 0;
	static int deltaX = 0;
	static int deltaY = 0;
	static double angle = 0;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		File dir = new File("C:\\Users\\tp6m3\\Desktop\\eclipse\\lineandstop");
		
		File[] files = dir.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				return pathname.getName().endsWith(".jpg");
			}
		});
		int filesLen = files.length;
		int filesOffset = 0;
		
		filesOffset = 0;
//		filesLen = 0;
//		Mat f = Imgcodecs.imread("C:\\Users\\tp6m3\\Desktop\\eclipse\\lineandstop\\curve.jpg");
//		frameProcess(f, 0);

		for (int i = 0; i < filesLen; i++) {
			System.out.println(files[i + filesOffset].getAbsolutePath());
			Mat f = Imgcodecs.imread(files[i + filesOffset].getAbsolutePath());
			
			frameProcess(f, i);

		}
		
	}
	
	public static void frameProcess(Mat f, int index) {
		Mat edges = new Mat();
		Mat f2 = new Mat();
		Mat hierarchy = new Mat();
		ArrayList<MatOfPoint> contours = new ArrayList<>();
		Imgproc.cvtColor(f, f2, Imgproc.COLOR_BGR2GRAY);
		Imgproc.GaussianBlur(f2, f2, new Size(3, 3), 0, 0);
		Imgproc.Canny(f2, edges, 100, 150, 3, true);
		Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE);
		showImage(f, "f" + String.valueOf(index), 100 + index * 50, 100 + index * 50);
		showImage(edges, "e" + String.valueOf(index), 100 + index * 50, 100 + index * 50);
//		ps.println(contours.size());
//		try {
//			cal(contours, index);
//		 {
//			calLine(contours, index);
		try {
			calRoute(contours, index);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public static void colorConvert(Mat f, int index) {
		Mat edges = new Mat();
		Mat f2 = new Mat();
		Mat hierarchy = new Mat();
		showImage(f, "f3" + String.valueOf(index), 100 + index * 50, 100 + index * 50);

		double[] zero = {0.0, 0.0, 0.0};
		Imgproc.cvtColor(f, f2, Imgproc.COLOR_BGR2HSV);
		ps.printf("%d %d", f.width(), f.height());
//		System.out.printf("%d %d\n", f2.width(), f2.height());
//		BufferedImage b = new BufferedImage(f2.width(), f2.height(), BufferedImage.TYPE_INT_ARGB);
//		showImage(f2, "f" + String.valueOf(index), 100 + index * 50, 100 + index * 50);
		for(int i=0; i<f2.cols(); i++) {
			for(int j=0; j<f2.rows(); j++) {
				double[] HSV = f2.get(j, i);
				double H = HSV[0];
				double S = HSV[1];
				double V = HSV[2];
//				ps.println(HSV.length);
				//System.out.printf("%d %d\n", i, j);
				if(S >= 43 && H>=100 && H < 124 && V >= 46) {       
//					b.setRGB(i, j, Color.BLUE.getRGB());
					f2.put(j, i, HSV);
//					ps.println(Arrays.toString(HSV));
	            } else {
//	            	b.setRGB(i, j, Color.BLACK.getRGB());
	            	f2.put(j, i, zero);
	            }
			}
		}
		Imgproc.cvtColor(f2, f2, Imgproc.COLOR_HSV2BGR);
//		showImage(f2, "f3" + String.valueOf(index), 100 + index * 50, 100 + index * 50);

		Imgproc.GaussianBlur(f2, f2, new Size(3, 3), 0, 0);
		Imgproc.Canny(f2, edges, 250, 250, 3, true);
		ArrayList<MatOfPoint> contours = new ArrayList<>();
		Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE);
		Point[] max = contours.get(0).toArray();
		if(contours.size() > 3) {
//			for(MatOfPoint c : contours) {
//				if (c.toArray().length > max.length) {
//					max = c.toArray();
//				}
//			}
			
			Point p = calCircleCenter(contours.get(0).toArray()[0], contours.get(1).toArray()[0], contours.get(2).toArray()[0]);
			ps.println(p);
			
		} else {
			Point p = calCircleCenter(max[0], max[(0+max.length-1) / 4], max[(0+max.length-1) / 2]);
			ps.println(p);
		}
		
		
		showImage(edges, "f3" + String.valueOf(index), 100 + index * 50, 100 + index * 50);
	}
	
	public static void cal(ArrayList<MatOfPoint> contours, int index) {
		
//		showImage(edges, "edges" + String.valueOf(index), 600 + index * 50, 100 + index * 50);

//		ArrayList<MatOfPoint> contours = frameProcess(f);
		
		ps.println(contours.size());
//		for (MatOfPoint contour : contours) {
//			for (Point point : contour.toArray()) {
//				System.out.printf("%.0f, %.0f\n", point.x, point.y);
//			}
//		}

//		Point[] max = contours.get(0).toArray();
//		for (int i = 1; i < contours.size(); i++) {
//			Point[] tmp = contours.get(i).toArray();
//			if (max.length < tmp.length) {
//				max = tmp;
//			}
//		}
		
		int maxLen = 0;
		ArrayList<Line> lines = new ArrayList<>();
		for (MatOfPoint contour : contours) {
			Point[] points = contour.toArray();
			int pointsLen = points.length;
			
			if (pointsLen < 6) continue;
			
			int preIndex;
			int nexIndex;
			double[] angle1 = new double[pointsLen];
			double[] angle2 = new double[pointsLen];
			for (int i = 0; i < pointsLen; i++) {
				preIndex = i - 2;
				nexIndex = i + 2;
				if (preIndex < 0) preIndex += pointsLen;
				if (nexIndex >= pointsLen) nexIndex -= pointsLen;
				angle1[i] = Line.getAngle(points[preIndex], points[nexIndex]);
			}
			
			for (int i = 0; i < pointsLen; i++) {
				preIndex = i - 1;
				nexIndex = i + 1;
				if (preIndex < 0) preIndex += pointsLen;
				if (nexIndex >= pointsLen) nexIndex -= pointsLen;
				double angle = angle1[preIndex] - angle1[nexIndex];
				if (angle < -180) {
					angle += 360;
				} else if (angle > 180) {
					angle -= 360;
				}
				
				angle2[i] = angle;
			}
			
			preIndex = 0;
			nexIndex = 0;
			
			for (int i = 0; i < pointsLen; i++) {
				if (preIndex >= pointsLen) preIndex -= pointsLen;
				if (nexIndex >= pointsLen) nexIndex -= pointsLen;
				if (Math.abs(angle2[nexIndex]) >= 20) {
					int deltaIndex = nexIndex - preIndex;
					if (deltaIndex < 0) deltaIndex += pointsLen;
					if (deltaIndex > 0) {
						lines.add(new Line(points[preIndex], points[nexIndex], deltaIndex));
						if (maxLen < deltaIndex) {
							maxLen = deltaIndex;
						}
					}
					preIndex = nexIndex + 1;
				}
				nexIndex++;
			}
		}
		
		for (int i = 0; i < lines.size();) {
			if (lines.get(i).len < maxLen / 2) {
				lines.remove(i);
			} else {
				i++;
			}
		}
		
		ArrayList<Line> lines1 = new ArrayList<>();
		ArrayList<Line> lines2 = new ArrayList<>();
		if (true) {
			int lineLenMax1 = 0;
			int lineLenMax2 = 0;
			
			double angle = lines.get(0).getAngle();
			lines1.add(lines.get(0));
			lineLenMax1 = lines.get(0).len;
			
			for (int i = 1; i < lines.size(); i++) {
				double deltaAngle = lines.get(i).getAngle() - angle;
				if (deltaAngle < 0) deltaAngle += 360;
				if (deltaAngle >= 180) deltaAngle -= 180;
				if (deltaAngle >= 90) deltaAngle = 180 - deltaAngle;
				if (deltaAngle < 45) {
					lines1.add(lines.get(i));
					if (lineLenMax1 < lines.get(i).len) {
						lineLenMax1 = lines.get(i).len;
					}
				} else {
					lines2.add(lines.get(i));
					if (lineLenMax2 < lines.get(i).len) {
						lineLenMax2 = lines.get(i).len;
					}
				}
			}
			if (lineLenMax1 < lineLenMax2) {
				ArrayList<Line> tmp = lines1;
				lines1 = lines2;
				lines2 = tmp;
			}
		}
		if(lines2.isEmpty()) {
			//It is Line.
			calRoute(contours, index);
		} else {
			//It is Stop Line.
			Point intersectionPoint = lines1.get(0).getIntersectionPoint(lines2.get(0));
			
			status = 1;
			int deltaX = (int) Math.round(intersectionPoint.x) - 250;
			int deltaY = (int) Math.round(intersectionPoint.y) - 250;
			double angle = lines1.get(0).getAngle();
			ps.printf("( %d , %d ) %.2f\n", deltaX, deltaY, angle);
			
			BufferedImage bufferedImage = new BufferedImage(500, 500, BufferedImage.TYPE_3BYTE_BGR);
			Graphics2D g = bufferedImage.createGraphics();
			
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
			g.setColor(Color.WHITE);
//			for (Point point : max) {
//				g.drawRect((int)point.x, (int)point.y, 1, 1);
//			}

			g.setColor(Color.GREEN);
			g.setStroke(new BasicStroke(3));
			for (Line line : lines1) {
				g.drawLine((int)line.start.x, (int)line.start.y, (int)line.end.x, (int)line.end.y);
			}
			g.setColor(Color.RED);
			for (Line line : lines2) {
				g.drawLine((int)line.start.x, (int)line.start.y, (int)line.end.x, (int)line.end.y);
			}
			g.setStroke(new BasicStroke(1));
			g.setColor(Color.YELLOW);
			drawLine(g, lines1.get(0));
			drawLine(g, lines2.get(0));
			
			g.setColor(Color.BLUE);
			drawDot(g, intersectionPoint, 10);
			
			ps.println("Stop");
			showImage(bufferedImage, "result" + String.valueOf(index), 100 + index * 50, 100 + index * 50);
		}
		
	
	}
	
	public static void calRoute(ArrayList<MatOfPoint> contours, int index) {
		int frameXY = 500;
//		int frameY = 500;
//		ArrayList<Point> points = new ArrayList<>();
		
		ArrayList<Point>[][] pointList = new ArrayList[frameXY/2][4];
		
		for(int i=0; i<250; i++) {
			for(int j=0; j<4; j++) {
				pointList[i][j] = new ArrayList<>();
			}
		}
		for(MatOfPoint contour : contours) {
			for (Point p : contour.toArray()) {
				double r1 = p.x - p.y;
				double r2 = p.x + p.y;
//				ps.println(p);
				if (r1 >= 0 && r2 >= frameXY) {
					pointList[(int) (frameXY-p.x)][1].add(p);
				} else if (r1 >= 0 && r2 < frameXY) {
					pointList[(int) (p.y)][0].add(p);
				} else if (r1 < 0 && r2 >= frameXY) {
					pointList[(int) (frameXY-p.y)][2].add(p);
				} else if (r1 < 0 && r2 < frameXY) {
					pointList[(int) (p.x)][3].add(p);
				}
			}
		}
		
		ArrayList<Point> finalPoint = new ArrayList<>();
		for(int i=0; i<250; i++) {
			finalPoint.clear();
			for(int j=0; j<4; j++) {
				if (pointList[i][j].size() > 0) finalPoint.add(pointList[i][j].get(0));
			}
			if (finalPoint.size() >= 2) {
				ps.println("FIND");
				break;
			}
		}
		
//		Point R = new Point();
//		Point T = new Point();
//		Point L = new Point();
//		Point B = new Point();
//		for(int i=0; i<500; i++) {
//			points.clear();
//			int Rf = 0;
//			int Tf = 0;
//			int Lf = 0;
//			int Bf = 0;
//			for(MatOfPoint contour : contours) {
//				for (Point p : contour.toArray()) {
//					if(p.x == i && p.y <= frameY-i && p.y >= i && Lf == 0) {
//						L = p;
//						Lf = 1;
//						points.add(p);
//					} else if (p.y == i && p.x <= frameX-i && p.x >= i && Tf == 0) {
//						T = p;
//						Tf = 1;
//						points.add(p);
//					} else if (p.x == frameX-i && p.y <= frameY-i && p.y >= i && Rf == 0){
//						R = p;
//						Rf = 1;
//						points.add(p);
//					} else if (p.y == frameY-i && p.x <= 500-i && p.x >= i && Bf == 0) {
//						B = p;
//						Bf = 1;
//						points.add(p);
//					}
//					
//				}
//			}
//			if (Lf+Tf+Rf+Bf>=2) break;
//			
//		}
//		ps.printf("%s %s %s %s", R, T, L, B);
		
		BufferedImage bufferedImage = new BufferedImage(500, 500, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = bufferedImage.createGraphics();
		g.setColor(Color.WHITE);
//		for (Point p : points) {
//			drawDot(g, p, 10);
//		}
		for(Point p : finalPoint) {
			ps.println(p);
			drawDot(g, p, 5);
		}
		
		showImage(bufferedImage, "result" + String.valueOf(index), 100 + index * 50, 100 + index * 50);
	}
	
	public static void calLine(ArrayList<MatOfPoint> contours, int index) {
		ps.println("Line");
//		ArrayList<MatOfPoint> contours = frameProcess(f);
//		for (MatOfPoint contour : contours) {
//			for (Point point : contour.toArray()) {
//				System.out.printf("%.0f, %.0f\n", point.x, point.y);
//			}
//		}
		
		
		
//		Point[] max = contours.get(0).toArray();
//		for (int i = 1; i < contours.size(); i++) {
//			Point[] tmp = contours.get(i).toArray();
//			if (max.length < tmp.length) {
//				max = tmp;
//			}
//		}
		
		int maxLen = 0;
		ArrayList<Line> lines = new ArrayList<>();
		for (MatOfPoint contour : contours) {
			Point[] points = contour.toArray();
			int pointsLen = points.length;
			
			if (pointsLen < 6) continue;
			
			int preIndex;
			int nexIndex;
			double[] angle1 = new double[pointsLen];
			double[] angle2 = new double[pointsLen];
			double[] angle3 = new double[pointsLen];
			for (int i = 0; i < pointsLen; i++) {
				preIndex = i - 2;
				nexIndex = i + 2;
				if (preIndex < 0) preIndex += pointsLen;
				if (nexIndex >= pointsLen) nexIndex -= pointsLen;
				angle1[i] = Line.getAngle(points[preIndex], points[nexIndex]);
			}
			
			for (int i = 0; i < pointsLen; i++) {
				preIndex = i - 1;
				nexIndex = i + 1;
				if (preIndex < 0) preIndex += pointsLen;
				if (nexIndex >= pointsLen) nexIndex -= pointsLen;
				double angle = angle1[preIndex] - angle1[nexIndex];
				if (angle < -180) {
					angle += 360;
				} else if (angle > 180) {
					angle -= 360;
				}
				
				angle2[i] = angle;
			}
			
			for (int i = 0; i < pointsLen; i++) {
				preIndex = i - 1;
				nexIndex = i + 1;
				if (preIndex < 0) preIndex += pointsLen;
				if (nexIndex >= pointsLen) nexIndex -= pointsLen;
				double angle = angle2[preIndex] - angle2[nexIndex];
				if (angle < -180) {
					angle += 360;
				} else if (angle > 180) {
					angle -= 360;
				}
				
				angle3[i] = angle;
			}
			
			preIndex = 0;
			nexIndex = 0;
			
			for (int i = 0; i < pointsLen; i++) {
				if (preIndex >= pointsLen) preIndex -= pointsLen;
				if (nexIndex >= pointsLen) nexIndex -= pointsLen;
				if (Math.abs(angle3[nexIndex]) >= 20) {
					int deltaIndex = nexIndex - preIndex;
					if (deltaIndex < 0) deltaIndex += pointsLen;
					if (deltaIndex > 0) {
						lines.add(new Line(points[preIndex], points[nexIndex], deltaIndex));
						if (maxLen < deltaIndex) {
							maxLen = deltaIndex;
						}
					}
					preIndex = nexIndex + 1;
				}
				nexIndex++;
			}
		}
		
		for (int i = 0; i < lines.size();) {
			if (lines.get(i).len < maxLen / 2) {
				lines.remove(i);
			} else {
				i++;
			}
		}
		
		ArrayList<Line> lines1 = new ArrayList<>();
//		ArrayList<Line> lines2 = new ArrayList<>();
		if (true) {
			int lineLenMax1 = 0;
//			int lineLenMax2 = 0;
			
			double angle = lines.get(0).getAngle();
			lines1.add(lines.get(0));
			lineLenMax1 = lines.get(0).len;
			
			for (int i = 1; i < lines.size(); i++) {
				double deltaAngle = lines.get(i).getAngle() - angle;
				if (deltaAngle < 0) deltaAngle += 360;
				if (deltaAngle >= 180) deltaAngle -= 180;
				if (deltaAngle >= 90) deltaAngle = 180 - deltaAngle;
				if (deltaAngle < 45) {
					lines1.add(lines.get(i));
					if (lineLenMax1 < lines.get(i).len) {
						lineLenMax1 = lines.get(i).len;
					}
				}
//				else {
//					lines2.add(lines.get(i));
//					if (lineLenMax2 < lines.get(i).len) {
//						lineLenMax2 = lines.get(i).len;
//					}
//				}
			}
//			if (lineLenMax1 < lineLenMax2) {
//				ArrayList<Line> tmp = lines1;
//				lines1 = lines2;
//				lines2 = tmp;
//			}
		}
		
//		Point intersectionPoint = lines1.get(0).getIntersectionPoint(lines2.get(0));
		
//		int status = 1;
//		int deltaX = (int) Math.round(intersectionPoint.x) - 250;
//		int deltaY = (int) Math.round(intersectionPoint.y) - 250;
//		double angle = lines1.get(0).getAngle();
		
		
		
		
		
		ps.printf("( %d , %d ) %.2f\n", deltaX, deltaY, angle);
		
		
		BufferedImage bufferedImage = new BufferedImage(500, 500, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = bufferedImage.createGraphics();
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
		g.setColor(Color.WHITE);
//		for (Point point : max) {
//			g.drawRect((int)point.x, (int)point.y, 1, 1);
//		}

		g.setColor(Color.GREEN);
		g.setStroke(new BasicStroke(3));
		for (Line line : lines1) {
			g.drawLine((int)line.start.x, (int)line.start.y, (int)line.end.x, (int)line.end.y);
		}
//		Line vLine = new Line();
//		g.setColor(Color.RED);
//		for (Line line : lines2) {
//			g.drawLine((int)line.start.x, (int)line.start.y, (int)line.end.x, (int)line.end.y);
//		}
		g.setStroke(new BasicStroke(1));
		g.setColor(Color.YELLOW);
		drawLine(g, lines1.get(0));
//		drawLine(g, lines2.get(0));
		
		g.setColor(Color.BLUE);
//		drawDot(g, intersectionPoint, 10);
		
		
		
		
		showImage(bufferedImage, "result" + String.valueOf(index), 100 + index * 50, 100 + index * 50);
	}
	
	public static Point calCircleCenter(Point a, Point b, Point c) {
		Point center = new Point();
		double[][] fX = {{a.x*a.x+a.y*a.y, a.y, 1}, {b.x*b.x+b.y*b.y, b.y, 1}, {c.x*c.x+c.y*c.y, c.y, 1}};
		double[][] n = {{a.x, a.y, 1}, {b.x, b.y, 1}, {c.x, c.y, 1}};
		double[][] fY = {{a.x, a.x*a.x+a.y*a.y, 1}, {b.x, b.x*b.x+b.y*b.y, 1}, {c.x, c.x*c.x+c.y*c.y, 1}};
		double nR = 2 * calMatrix(n);
		center.x = calMatrix(fX) / nR;
		center.y = calMatrix(fY) / nR;
		
		return center;
	}
	
	public static double calMatrix(double[][] s) {
		return (s[0][0]*s[1][1]*s[2][2] + s[0][1]*s[1][2]*s[2][0] + s[0][2]*s[1][0]*s[2][1]) - 
				(s[0][2]*s[1][1]*s[2][0] + s[0][1]*s[1][0]*s[2][2] + s[2][1]*s[1][2]*s[0][0]);
	}
	
	public static void drawDot(Graphics2D g, Point point, int r) {
		g.drawOval((int)point.x - r, (int)point.y - r, 2 * r, 2 * r);
	}
	public static void drawLine(Graphics2D g, Line l) {
		int x1, y1, x2, y2;
		if (l.getA() == 0) {
			x1 = 0;
			y1 = (int) (1.0 / l.getB());
			x2 = 500;
			y2 = (int) (1.0 / l.getB());
		} else if (l.getB() == 0) {
			x1 = (int) (1.0 / l.getA());
			y1 = 0;
			x2 = (int) (1.0 / l.getA());
			y2 = 500;
		} else {
			x1 = 0;
			y1 = (int) (1.0 / l.getB());
			x2 = 500;
			y2 = (int) ((1 - l.getA() * 500) / l.getB());
		}
		g.drawLine(x1, y1, x2, y2);
	}
	

	public static void showImage(Mat f, String title, int x, int y) {
		
		try {
			JFrame frame0 = new JFrame();
			frame0.getContentPane().add(new MyJPanel(f));
			frame0.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame0.setTitle(title);
			frame0.setSize(f.width(), f.height() + 30);
			frame0.setLocation(x, y);
			frame0.setVisible(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static void showImage(BufferedImage f, String title, int x, int y) {
		JFrame frame0 = new JFrame();
		frame0.getContentPane().add(new MyJPanel(f));
		frame0.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame0.setTitle(title);
		frame0.setSize(f.getWidth(), f.getHeight() + 30);
		frame0.setLocation(x, y);
		frame0.setVisible(true);
	}

}
