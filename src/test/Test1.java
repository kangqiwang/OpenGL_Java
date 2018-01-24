package test;

import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;
import static com.jogamp.opengl.GL.GL_STATIC_DRAW;

import java.nio.FloatBuffer;
import javax.swing.JFrame;

import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.GL3;
import static com.jogamp.opengl.GL3.*;

public class Test1 extends JFrame implements GLEventListener {

	private final GLCanvas canvas;
	private final int numVertices=1;
	public Test1(){
		GLProfile glp=GLProfile.get(GLProfile.GL3);
		GLCapabilities caps=new GLCapabilities(glp);
		
		canvas=new GLCanvas(caps);
		canvas.addGLEventListener(this);
		add(canvas, java.awt.BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 600);
		setTitle("Kangqi");
		setVisible(true);
		
		
		
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame f= new Test1();
		
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		GL3 gl=drawable.getGL().getGL3();
		gl.glClear(GL_COLOR_BUFFER_BIT);
		gl.glPointSize(10);
		gl.glLineWidth(10);
		gl.glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		gl.glDrawArrays(GL_POINTS, 0, numVertices);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		GL3 gl=drawable.getGL().getGL3();
		float[] vertexArray= {0.0f,0.0f};
		
		FloatBuffer vertices=FloatBuffer.wrap(vertexArray);
		// A second binding is needed later to use it
//				gl.glGenVertexArrays(numVAOs, VAOs, 0);
//				gl.glBindVertexArray(VAOs[idPoint]);
//
//				// Generate vertex buffer objects (VBOs), and
//				// Bind a VBO, which means this VBO is initialised.
//				// The Data is then pooled into the buffer
//				gl.glGenBuffers(numVBOs, VBOs, 0);
//				gl.glBindBuffer(GL_ARRAY_BUFFER, VBOs[idBuffer]);
				gl.glBufferData(GL_ARRAY_BUFFER, vertexArray.length * (Float.SIZE / 8),
						vertices, GL_STATIC_DRAW);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		// TODO Auto-generated method stub
		
	}

}
