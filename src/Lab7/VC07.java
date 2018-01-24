package Lab7;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.Raster;
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

public class VC07 extends Component implements KeyListener {

	private BufferedImage in, out;
	int width, height;
	File inputFile;

	public static final float[] SHARPEN3x3 = { // sharpening filter kernel
        0.f, -1.f,  0.f,
       -1.f,  5.f, -1.f,
       0.f, -1.f,  0.f
    };

    public static final float[] BLUR3x3 = {
        0.1f, 0.1f, 0.1f,    // low-pass filter kernel
        0.1f, 0.2f, 0.1f,
        0.1f, 0.1f, 0.1f
    };

	public VC07() {
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
		VC07 img = new VC07();
		frame.add("Center", img);
		frame.pack();
		img.requestFocusInWindow();
		frame.setVisible(true);

	}

	
	private void processing() { //image filtering using filter()

		BufferedImageOp op = null;

		op = new ConvolveOp(new Kernel(3, 3, SHARPEN3x3),
                ConvolveOp.EDGE_NO_OP, null);
		
        out = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        op.filter(in, out);

        repaint();
	}
	
/*
	
	private void processing() { //image filtering without using filter()
		int[] rArray = new int[width*height];

		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++) {
				Color pixel = new Color(in.getRGB(x, y));	// get the color
				rArray[y*width+x] = pixel.getRed();			// red component
			}
		
		for (int y = 1; y < height-1; y++)
			for (int x = 1; x < width-1; x++) {
				int [] rNeighbour = {
					rArray[(y-1)*width+x-1], rArray[(y-1)*width+x], rArray[(y-1)*width+x+1],
					rArray[y*width+x-1], rArray[y*width+x], rArray[y*width+x+1],
					rArray[(y+1)*width+x-1], rArray[(y+1)*width+x], rArray[(y+1)*width+x+1]						
				};

				float r = 0;
				for(int i = 0; i<9; i++)
					r = r + rNeighbour[i]*BLUR3x3[i];
				r = Math.max(0, r);
				r = Math.min(255, r);

				out.setRGB(x, y, (new Color( (int)r, 0, 0)).getRGB());
			}

		repaint();
	}
	*/
		
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
            if (in.getType() != BufferedImage.TYPE_INT_RGB) {
                BufferedImage bi2 =
                    new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics big = bi2.getGraphics();
                big.drawImage(in, 0, 0, null);
                out = in = bi2;
            }
//			out = in;
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