import javax.swing.*;
import java.awt.image.*;

public class NoisePanel extends JPanel{

	private BufferedImage noiseImage;

	public NoisePanel(IndexColorModel cm, int w, int h) {
		noiseImage = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY, cm);
	}

	public void setNoiseRaster(WritableRaster raster) {
		noiseImage.setData(raster);
	}


}
