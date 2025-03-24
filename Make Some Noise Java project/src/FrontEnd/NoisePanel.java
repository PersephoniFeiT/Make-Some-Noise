package FrontEnd;

import javax.swing.*;
import java.awt.image.*;

public class NoisePanel extends JPanel{

	private BufferedImage bitmap;

	public NoisePanel(int w, int h) {
		bitmap = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

		// Initialize bitmap to a gradient
		int color = 0x00000000;
		for (int y = 0; y < h; y++) {
			color += 0x00FFFFFF/h;
			for (int x = 0; x < w; x++) {
				color += 0xFF000000/w;
				bitmap.setRGB(x, y, color);
			}
		}
	}

	public void setNoiseRaster(WritableRaster raster) {
		bitmap.setData(raster);
	}

	public void setPixel(int x, int y, int color) {
		bitmap.setRGB(x, y, color);
		repaint();
	}

	@Override
	protected void paintComponent(java.awt.Graphics g) {
		super.paintComponent(g);

		// Draw the bitmap on the JPanel
		g.drawImage(bitmap, 0, 0, null);
	}
}
