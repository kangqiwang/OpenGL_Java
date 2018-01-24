package Lab1;

import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;
import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_FLOAT;
import static com.jogamp.opengl.GL.GL_POINTS;
import static com.jogamp.opengl.GL.GL_STATIC_DRAW;

import java.nio.FloatBuffer;

import javax.swing.JFrame;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;


public class SimpleM extends JFrame implements GLEventListener{

//	private final GLJPanel canvas = new GLJPanel(); // Declare a canvas
	private final GLCanvas canvas; // Declare a canvas

	private int idPoint = 0, numVAOs = 1;
	private int idBuffer = 0, numVBOs = 1;
	private int vPosition =0;
	
	private final int numVertices = 1;
	float[] vertexArray = {  0.0f, 0.0f, 0.0f, 0.5f, 0.3f, -0.7f  };

	private int[] VAOs = new int[numVAOs];
	private int[] VBOs = new int[numVBOs];

	public SimpleM() {
        GLProfile glp = GLProfile.get(GLProfile.GL3);
        //about JOGL3
        GLCapabilities caps = new GLCapabilities(glp);
        //here profile obj should be passed as parameter
        canvas = new GLCanvas(caps);
        //here capabilities obj should be passed as parameter
        canvas.addGLEventListener(this); // Listen for openGL events
        add(canvas, java.awt.BorderLayout.CENTER); // Add the canvas into the frame              
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit when click close
        setSize(500, 500);  //set the window size
        setTitle("Modulised Simple Graphics"); //window title
        setVisible(true); // Display the frame
        /*
         * 也可以 creating frame
         * Frame frame=new frame("Basic Frame");
         *adding canvas to frame
         *frame.add(canvas);
         *frame.setvisible(true)
         */
       
	}

	public static void main(String[] args) {
		JFrame f = new SimpleM();

	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL3 gl = drawable.getGL().getGL3(); // Get the GL pipeline object this 
		
		gl.glClear(GL_COLOR_BUFFER_BIT);

		gl.glPointSize(5);                                                                                                                                                                                                                                                                                                                                  
		gl.glDrawArrays(GL_POINTS, 0, numVertices);
		
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL3 gl = drawable.getGL().getGL3(); // Get the GL pipeline object this 

		gl.glGenVertexArrays(numVAOs,VAOs,0);
		gl.glBindVertexArray(VAOs[idPoint]);
		
		float [] vertexArray = {
				0f, 0f // put it on centre 
		};

		FloatBuffer vertices = 
				FloatBuffer.allocate(vertexArray.length).put(vertexArray);
		vertices.rewind(); //very important, otherwise, no correct result.
		
		gl.glGenBuffers(numVBOs, VBOs,0);
		gl.glBindBuffer(GL_ARRAY_BUFFER, VBOs[idBuffer]);
		gl.glBufferData(GL_ARRAY_BUFFER, vertexArray.length*(Float.SIZE/8), 
				vertices, GL_STATIC_DRAW); // pay attention to *Float.SIZE/8

		ShaderProg shaderproc = new ShaderProg(gl, "Lab1.vert", "Lab1.frag");
		int program = shaderproc.getProgram();
		gl.glUseProgram(program);
		
		gl.glVertexAttribPointer(vPosition, 3, GL_FLOAT, false, 0, 0L);
		gl.glEnableVertexAttribArray(vPosition);
		
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

}