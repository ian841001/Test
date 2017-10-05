import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MakeJpg {

	public static void main(String[] args) {
		File dirPath = new File("/Users/ian/Downloads/makeJpg");
		
		for (File file : dirPath.listFiles()) {
			file.delete();
		}
		
		
//		makeLine(dirPath);
//		makeCurcle(dirPath);
		makeTen(dirPath);
	}
	public static void makeTen(File dirPath) {
		BufferedImage bufferedImage = new BufferedImage(500, 500, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = bufferedImage.createGraphics();
		Polygon polygon = new Polygon();
		for (int i = 0; i < 360; i++) {
			polygon.reset();
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
			double angle;
			angle = (i - 3) * Math.PI / 180;			
			polygon.addPoint((int)(250 + Math.sin(angle) * 70) , (int)(250 + Math.cos(angle) * 70));
			angle = (i + 3) * Math.PI / 180;
			polygon.addPoint((int)(250 + Math.sin(angle) * 70) , (int)(250 + Math.cos(angle) * 70));
			angle = (i + 177) * Math.PI / 180;
			polygon.addPoint((int)(250 + Math.sin(angle) * 70) , (int)(250 + Math.cos(angle) * 70));
			angle = (i - 177) * Math.PI / 180;
			polygon.addPoint((int)(250 + Math.sin(angle) * 70) , (int)(250 + Math.cos(angle) * 70));
			g.setColor(Color.BLACK);
			g.fillPolygon(polygon);
			polygon.reset();
			angle = (i - 83) * Math.PI / 180;			
			polygon.addPoint((int)(250 + Math.sin(angle) * 40) , (int)(250 + Math.cos(angle) * 40));
			angle = (i + 83) * Math.PI / 180;
			polygon.addPoint((int)(250 + Math.sin(angle) * 40) , (int)(250 + Math.cos(angle) * 40));
			angle = (i + 97) * Math.PI / 180;
			polygon.addPoint((int)(250 + Math.sin(angle) * 40) , (int)(250 + Math.cos(angle) * 40));
			angle = (i - 97) * Math.PI / 180;
			polygon.addPoint((int)(250 + Math.sin(angle) * 40) , (int)(250 + Math.cos(angle) * 40));
			g.setColor(Color.BLACK);
			g.fillPolygon(polygon);
			File file = new File(dirPath, String.format("%03d.jpg", i));
			try {
				System.out.println(file.getAbsolutePath());
				ImageIO.write(bufferedImage, "jpg", file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void makeCurcle(File dirPath) {
		BufferedImage bufferedImage = new BufferedImage(500, 500, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = bufferedImage.createGraphics();
		final int r = 300;
		
		
		
		for (int i = -r; i < bufferedImage.getHeight() + r; i++) {
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
			g.setColor(Color.BLACK);
			g.fillRect(215, 0, 70, 500);
			g.setColor(Color.BLUE);
			g.fillOval(250 - r, i - r, r * 2, r * 2);
			File file = new File(dirPath, String.format("%04d.jpg", i + r));
			try {
				System.out.println(file.getAbsolutePath());
				ImageIO.write(bufferedImage, "jpg", file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void makeLine(File dirPath) {
		BufferedImage bufferedImage = new BufferedImage(500, 500, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = bufferedImage.createGraphics();
		Polygon polygon = new Polygon();
		for (int i = 0; i < 360; i++) {
			polygon.reset();
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
			double angle;
			angle = (i - 5) * Math.PI / 180;			
			polygon.addPoint((int)(250 + Math.sin(angle) * 400) , (int)(250 + Math.cos(angle) * 400));
			angle = (i + 5) * Math.PI / 180;
			polygon.addPoint((int)(250 + Math.sin(angle) * 400) , (int)(250 + Math.cos(angle) * 400));
			angle = (i + 175) * Math.PI / 180;
			polygon.addPoint((int)(250 + Math.sin(angle) * 400) , (int)(250 + Math.cos(angle) * 400));
			angle = (i - 175) * Math.PI / 180;
			polygon.addPoint((int)(250 + Math.sin(angle) * 400) , (int)(250 + Math.cos(angle) * 400));
			g.setColor(Color.BLACK);
			g.fillPolygon(polygon);
			
			File file = new File(dirPath, String.format("%03d.jpg", i));
			try {
				System.out.println(file.getAbsolutePath());
				ImageIO.write(bufferedImage, "jpg", file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
