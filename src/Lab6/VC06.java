package Lab6;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

public class VC06 extends Component implements KeyListener {

	private BufferedImage in, out;
	int width, height;
	File inputFile;

	public VC06() {
		loadImage();
		addKeyListener(this);
	}

	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}

	public void paint(Graphics g) {
		g.drawImage(out, 0, 0, null);
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Image Processing");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		VC06 img = new VC06();
		frame.add("Center", img);
		frame.pack();
		img.requestFocusInWindow();
		frame.setVisible(true);
	}

	private void processing() {
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++) {
				Color pixel = new Color(in.getRGB(x, y)); // get the color
				int r = pixel.getRed();                   // red component
				int g = pixel.getGreen();                 // green component
				int b = pixel.getBlue();                  // blue component
				r = g = b = (int) (0.299*r + 0.587*g + 0.114*b); //grayscale 
				out.setRGB(x, y, (new Color(r, g, b)).getRGB());
			}

		repaint();
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		if (ke.getKeyCode() == KeyEvent.VK_ESCAPE)
			System.exit(0);
		if (ke.getKeyChar() == 's' || ke.getKeyChar() == 'S') {// Save the processed image
			saveImage();
		} else if (ke.getKeyChar() == 'p' || ke.getKeyChar() == 'P') {// Image Processing
			processing();
		}
	}

	private void loadImage() {
		try {
			in = ImageIO.read(new File("Daffodil.jpg"));
			width = in.getWidth();
			height = in.getHeight();
			out = in;
		} catch (IOException e) {
			System.out.println("Image could not be read");
			System.exit(1);
		}
	}

	private void saveImage() {
		try {
			ImageIO.write(out, "jpg", new File("DaffodilG.jpg"));
		} catch (IOException ex) {
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}