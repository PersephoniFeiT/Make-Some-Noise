package FrontEnd;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

/**
 * A graphical object that combines a writtable raster and a {@link JPanel}, allowing a panel that can be drawn to by pixel
 */
public class NoisePanel extends JPanel{

	private BufferedImage bitmap;

	/**
	 * Create a new NoisePanel
	 * @param w the number of pixels in the width
	 * @param h the number of pixels in the height
	 */
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

	/**
	 * Set the bitmap to have a new raster
	 * @param raster the NoisePanel's new WritableRaster
	 */
	public void setNoiseRaster(WritableRaster raster) {
		bitmap.setData(raster);
	}

	/**
	 * Set a single pixel in the Writable Raster to a color
	 * @param x the x-coordinate of the pixel to change
	 * @param y the y-coordinate of the pixel to change
	 * @param color the new color of selected pixel
	 */
	public void setPixel(int x, int y, int color) {
		bitmap.setRGB(x, y, color);
		repaint();
	}

	/**
	 * Set the panel to have a new bitmap
	 * @param img
	 */
	public void setBitmap(BufferedImage img) {
		this.bitmap = img;
		repaint();
	}

	@Override
	protected void paintComponent(java.awt.Graphics g) {
		super.paintComponent(g);

		// Draw the bitmap on the JPanel
		g.drawImage(bitmap, 0, 0, null);
	}

	/**
	 * @return the number of pixels in a column of writable pixels in the NoisePanel
	 */
	public int getBitmapHeight() {
		return bitmap.getHeight();
	}

	/**
	 * @return the number of pixels in a row of writable pixels in the NoisePanel
	 */
	public int getBitmapWidth() {
		return bitmap.getWidth();
	}

	/**
	 * Write the image in this NoisePanel to a file on the local disk
	 * @param file the file to write to
	 * @throws IOException if any error occurs while writing
	 */
	public void writeToFile(File file) throws IOException {
		ImageIO.write(bitmap, "png", file);
	}
}
