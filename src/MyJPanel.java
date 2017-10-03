import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

import javax.swing.JPanel;

import org.opencv.core.Mat;

public class MyJPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	BufferedImage bufferedImage;

	public MyJPanel(Mat frame) {
		int type = 0;
        if (frame.channels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY;
        } else if (frame.channels() == 3) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        } else {
        	throw new RuntimeException(String.valueOf(frame.channels()));
        }
        bufferedImage = new BufferedImage(frame.width(), frame.height(), type);
        WritableRaster raster = bufferedImage.getRaster();
        DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
        byte[] data = dataBuffer.getData();
        frame.get(0, 0, data);
        
	}
	public MyJPanel(BufferedImage frame) {
		bufferedImage = frame;
	}
	
	@Override
    public void paint(Graphics g) {
        g.drawImage(bufferedImage, 0, 0, this);
    }
}
