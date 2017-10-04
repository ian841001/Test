import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

public class MyJPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	BufferedImage bufferedImage;

	public MyJPanel(Mat frame) throws IOException {
//		int type = 0;
//        if (frame.channels() == 1) {
//            type = BufferedImage.TYPE_BYTE_GRAY;
//        } else if (frame.channels() == 3) {
//            type = BufferedImage.TYPE_3BYTE_BGR;
//        } else {
//        	throw new RuntimeException(String.valueOf(frame.channels()));
//        }
//        bufferedImage = new BufferedImage(frame.width(), frame.height(), type);
//        WritableRaster raster = bufferedImage.getRaster();
//        DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
//        byte[] data = dataBuffer.getData();
//        frame.get(0, 0, data);
		bufferedImage = mat2BufferedImage(frame);
        
	}
	public MyJPanel(BufferedImage frame) {
		bufferedImage = frame;
	}
	
	@Override
    public void paint(Graphics g) {
        g.drawImage(bufferedImage, 0, 0, this);
    }

	static BufferedImage mat2BufferedImage(Mat matrix) throws IOException {        
	    MatOfByte mob=new MatOfByte();
	    Imgcodecs.imencode(".jpg", matrix, mob);
	    byte ba[]=mob.toArray();

	    BufferedImage bi=ImageIO.read(new ByteArrayInputStream(ba));
	    return bi;
	}
	
}
