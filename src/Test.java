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

public class Test {
	
	static PrintStream ps = System.out;
	
	static int showIndex = 0;
	
	static int status = 0;
	static int deltaX = 0;
	static int deltaY = 0;
	static double angle = 0;

	public static void main(String[] avgs) {
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		File dir = new File("/Users/ian/Documents/making/project/TDK_21th/jpg/ten");
		
		File[] files = dir.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				return pathname.getName().endsWith(".jpg");
			}
		});
		int filesLen = files.length;
		int filesOffset = 0;
			
		filesOffset = 4;
		filesLen = 1;
		for (int i = 0; i < filesLen && i + filesOffset < files.length; i++) {
//			System.out.println(files[i + filesOffset].getAbsolutePath());
			Mat f = Imgcodecs.imread(files[i + filesOffset].getAbsolutePath());
			cal2(f, i);
			
		}
		
		

//		VideoCapture camera = new VideoCapture();
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		camera.open(0);
//		System.out.println(camera.set(Videoio.CV_CAP_PROP_FRAME_WIDTH, 500));
//		System.out.println(camera.set(Videoio.CV_CAP_PROP_FRAME_HEIGHT, 500));
//
////		System.out.println(camera.get(Videoio.CV_CAP_PROP_FRAME_WIDTH));
////		System.out.println(camera.get(Videoio.CV_CAP_PROP_FRAME_HEIGHT));
//		
//		Mat f = new Mat();
//		
//		camera.read(f);
//		camera.read(f);
//		
//		ps.println(f.toString());
//		
//		if (!camera.isOpened()) {
//			System.out.println("error");
//		} else {
//			showImage(f, "", 100, 100);
//		}
//		camera.release();

	}
	public static void cal2(Mat f, int index) {
		Mat lines = new Mat();
	    Mat grey = new Mat();
		Mat edges = new Mat();

	    Imgproc.cvtColor(f, grey, Imgproc.COLOR_BGR2GRAY);
		Imgproc.GaussianBlur(grey, grey, new Size(3, 3), 0, 0);
		Imgproc.Canny(grey, edges, 250, 150, 3, true);
	    Imgproc.HoughLinesP(edges, lines, 1, Math.PI / 180, 0, 10, 0);
	    
	    
	    
	    
	    
	    BufferedImage bufferedImage = new BufferedImage(500, 500, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = bufferedImage.createGraphics();
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
		g.setColor(Color.WHITE);
//		for (Point point : max) {
//			g.drawRect((int)point.x, (int)point.y, 1, 1);
//		}

		g.setColor(Color.GREEN);
		g.setStroke(new BasicStroke(1));
		
	    for (int i = 0; i < lines.rows(); i++) {
	    	double[] vec = lines.get(i, 0);
	    	double len = Math.pow(Math.pow(vec[0] - vec[2], 2) + Math.pow(vec[1] - vec[3], 2), 0.5);
	    	ps.printf("( %d , %d ) , ( %d , %d ) : %6.2f\n", (int) vec[0], (int) vec[1], (int) vec[2], (int) vec[3], len);
	    	g.drawLine((int) vec[0], (int) vec[1], (int) vec[2], (int) vec[3]);
	    }
	    showImage(bufferedImage, "result" + String.valueOf(index), 0 + showIndex * 50, 100 + showIndex * 50);
	    showImage(edges, "edges" + String.valueOf(index), 500 + showIndex * 50, 100 + showIndex * 50);
		showImage(f, "f" + String.valueOf(index), 1000 + showIndex * 50, 100 + showIndex * 50);
		
		showIndex++;
	    ps.printf("%3d : %2d\n", index, lines.rows());
	    
	}
	
	
	
	
	public static void cal(Mat f, int index) {
		
		Mat f2 = new Mat();
		Mat edges = new Mat();
		Mat hierarchy = new Mat();
		Mat linesF = new Mat();

		// showImage(f, "f" + String.valueOf(index), 100 + index * 50, 100 + index * 50);
		Imgproc.cvtColor(f, f2, Imgproc.COLOR_BGR2GRAY);
		Imgproc.GaussianBlur(f2, f2, new Size(3, 3), 0, 0);
		Imgproc.Canny(f2, edges, 250, 150, 3, true);
		Imgproc.HoughLinesP(edges, linesF, 1, Math.PI / 180, 20, 10, 5);
		// showImage(f, "f", 100, 100);
		// showImage(f2, "f2", 100, 100);
		// showImage(f3, "f3", 100, 100);
		

		ArrayList<MatOfPoint> contours = new ArrayList<>();
		Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE);
		
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
		
		Point intersectionPoint = null;
		if (!lines1.isEmpty() && !lines2.isEmpty()) {
			intersectionPoint= lines1.get(0).getIntersectionPoint(lines2.get(0));
			
			status = 1;
			deltaX = (int) Math.round(intersectionPoint.x) - 250;
			deltaY = (int) Math.round(intersectionPoint.y) - 250;
			angle = lines1.get(0).getAngle() + index - 90;
			if (angle < -180) {
				angle += 360;
			}
			if (angle > 180) {
				angle -= 360;
			}
			
			if (angle < -90) {
				angle += 180;
			}
			if (angle > 90) {
				angle -= 180;
			}
			
			ps.printf("%03d : ( %3d , %3d ) %5.2f\n", index, deltaX, deltaY, angle);
		}
		
		
		
		
		// if (deltaX != 250 || deltaY != 250 || angle != index)
		
		
		
		
		
		
		
		
		if (status == 0 || Math.abs(angle) > 5) {
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
			if (!lines1.isEmpty()) {
				drawLine(g, lines1.get(0));
			}
			if (!lines2.isEmpty()) {
				drawLine(g, lines2.get(0));
			}
			
			if (intersectionPoint != null) {
				g.setColor(Color.BLUE);
				drawDot(g, intersectionPoint, 10);
			}
			
			
			
			
			
			showImage(bufferedImage, "result" + String.valueOf(index), 0 + showIndex * 50, 100 + showIndex * 50);
			showImage(edges, "edges" + String.valueOf(index), 500 + showIndex * 50, 100 + showIndex * 50);
			showImage(f, "f" + String.valueOf(index), 1000 + showIndex * 50, 100 + showIndex * 50);
			
			showIndex++;
		}
		
		
		
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
