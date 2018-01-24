package Lab4;



import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;
import static com.jogamp.opengl.GL.GL_CULL_FACE;
import static com.jogamp.opengl.GL.GL_STATIC_DRAW;
import static com.jogamp.opengl.GL3.*;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.swing.JFrame;



import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import Lab4.Basic.ShaderProg;
import Lab4.Basic.Transform;
import Lab4.Basic.Vec4;
import Lab4.Objects.SObject;
import Lab4.Objects.SSphere;
import Lab4.Objects.STeapot;

public class VC04 extends JFrame{

	final GLCanvas canvas; //Define a canvas 
	final FPSAnimator animator=new FPSAnimator(60, true);
	final Renderer renderer = new Renderer();

	public VC04() {
        GLProfile glp = GLProfile.get(GLProfile.GL3);
        GLCapabilities caps = new GLCapabilities(glp);
        canvas = new GLCanvas(caps);

        add(canvas, java.awt.BorderLayout.CENTER); // Put the canvas in the frame
		canvas.addGLEventListener(renderer); //Set the canvas to listen GLEvents
		canvas.addMouseListener(renderer);
		canvas.addMouseMotionListener(renderer);
		
		animator.add(canvas);

		setTitle("Lab 4");
		setSize(500,500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		animator.start();
		canvas.requestFocus();
		}

	public static void main(String[] args) {
		new VC04();

	}

	class Renderer implements GLEventListener, MouseListener, MouseMotionListener{

		private Transform T = new Transform();

		//VAOs and VBOs parameters
		private int idPoint=0, numVAOs = 1;
		private int idBuffer=0, numVBOs = 1;
		private int idElement=0, numEBOs = 1;
		private int[] VAOs = new int[numVAOs];
		private int[] VBOs = new int[numVBOs];
		private int[] EBOs = new int[numEBOs];

		//Model parameters
		private int numElements;
		private int vPosition;
		private int vNormal;

		//Transformation parameters
		private int ModelView;
		private int NormalTransform;
		private int Projection; 
		private float scale = 1;
		private float tx = 0;
		private float ty = 0;
		private float rx = 0;
		private float ry = 0;
		
		//Mouse position
		private int xMouse = 0; 
		private int yMouse = 0;

		@Override
		public void display(GLAutoDrawable drawable) {
			GL3 gl = drawable.getGL().getGL3(); // Get the GL pipeline object this 
			
			gl.glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);

//			gl.glBindVertexArray(VAOs[idPoint]);

			T.initialize();
			T.scale(0.3f, 0.3f, 0.3f);
			
			//Mouse control interactive
			T.scale(scale, scale, scale);
			T.rotateX(rx);
			T.rotateY(ry);
			T.translate(tx, ty, 0);
			
			//Locate camera
//			T.LookAt(0, 0, 0, 0, 0, -1, 0, 1, 0);  	//Default					
			
			
			//Send model_view and normal transformation matrices to shader. 
			//Here parameter 'true' for transpose means to convert the row-major  
			//matrix to column major one, which is required when vertices'
			//location vectors are pre-multiplied by the model_view matrix.
			//Note that the normal transformation matrix is the inverse-transpose
			//matrix of the vertex transformation matrix
			gl.glUniformMatrix4fv( ModelView, 1, true, T.getTransformv(), 0 );			
			gl.glUniformMatrix4fv( NormalTransform, 1, true, T.getInvTransformTv(), 0 );			

			gl.glDrawElements(GL_TRIANGLES, numElements, GL_UNSIGNED_INT, 0);
		}

		@Override
		public void dispose(GLAutoDrawable drawable) {
			// TODO Auto-generated method stub			
		}

		@Override
		public void init(GLAutoDrawable drawable) {
			GL3 gl = drawable.getGL().getGL3(); // Get the GL pipeline object this 

			gl.glEnable(GL_CULL_FACE); 

			//SObject object = new STeapot(3);
			SObject object = new SSphere(3);

			float [] vertexArray = object.getVertices();
			float [] normalArray = object.getNormals();
			int [] vertexIndexs =object.getIndices();
			numElements = object.getNumIndices();
			
			gl.glGenVertexArrays(numVAOs,VAOs,0);
			gl.glBindVertexArray(VAOs[idPoint]);

			FloatBuffer vertices = FloatBuffer.wrap(vertexArray);
			FloatBuffer normals = FloatBuffer.wrap(normalArray);
			
			gl.glGenBuffers(numVBOs, VBOs,0);
			gl.glBindBuffer(GL_ARRAY_BUFFER, VBOs[idBuffer]);

		    // Create an empty buffer with the size we need 
			// and a null pointer for the data values
			long vertexSize = vertexArray.length*(Float.SIZE/8);
			long normalSize = normalArray.length*(Float.SIZE/8);
			gl.glBufferData(GL_ARRAY_BUFFER, vertexSize +normalSize, 
					null, GL_STATIC_DRAW); // pay attention to *Float.SIZE/8
		    
			// Load the real data separately.  We put the colors right after the vertex coordinates,
		    // so, the offset for colors is the size of vertices in bytes
		    gl.glBufferSubData( GL_ARRAY_BUFFER, 0, vertexSize, vertices );
		    gl.glBufferSubData( GL_ARRAY_BUFFER, vertexSize, normalSize, normals );

			IntBuffer elements = IntBuffer.wrap(vertexIndexs);
			
			gl.glGenBuffers(numEBOs, EBOs,0);
			gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBOs[idElement]);


			long indexSize = vertexIndexs.length*(Integer.SIZE/8);
			gl.glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexSize, 
					elements, GL_STATIC_DRAW); // pay attention to *Float.SIZE/8						
			
		    ShaderProg shaderproc = new ShaderProg(gl, "Gouraud.vert", "Gouraud.frag");
			int program = shaderproc.getProgram();
			gl.glUseProgram(program);
			
		   // Initialize the vertex position attribute in the vertex shader    
		    vPosition = gl.glGetAttribLocation( program, "vPosition" );
			gl.glEnableVertexAttribArray(vPosition);
			gl.glVertexAttribPointer(vPosition, 3, GL_FLOAT, false, 0, 0L);

		    // Initialize the vertex color attribute in the vertex shader.
		    // The offset is the same as in the glBufferSubData, i.e., vertexSize
			// It is the starting point of the color data
//		 /
				 
		    // This is necessary. Otherwise, the The color on back face may display 
//		    gl.glDepthFunc(GL_LESS);
		    gl.glEnable(GL_DEPTH_TEST);		    
		}
		
		@Override
		public void reshape(GLAutoDrawable drawable, int x, int y, int w,
				int h) {

			GL3 gl = drawable.getGL().getGL3(); // Get the GL pipeline object this 
			
			gl.glViewport(x, y, w, h);

			T.initialize();

			//projection
//			T.Ortho(-1, 1, -1, 1, -1, 1);  //Default
			// to avoid shape distortion because of reshaping the viewport
			//viewport aspect should be the same as the projection aspect
			if(h<1){h=1;}
			if(w<1){w=1;}			
			float a = (float) w/ h;   //aspect 
			if (w < h) {
				T.ortho(-1, 1, -1/a, 1/a, -1, 1);
			}
			else{
				T.ortho(-1*a, 1*a, -1, 1, -1, 1);
			}
			
			// Convert right-hand to left-hand coordinate system
			T.rotateX(-90);
			T.reverseZ();
		    gl.glUniformMatrix4fv( Projection, 1, true, T.getTransformv(), 0 );			

		}
		@Override
		public void mouseDragged(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			
			//left button down, move the object
			if((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0){
				tx += (x-xMouse) * 0.01;
				ty -= (y-yMouse) * 0.01;
				xMouse = x;
				yMouse = y;
			}
			
			//right button down, rotate the object
			if((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0){
			ry += (x-xMouse) * 1;
			rx += (y-yMouse) * 1;
			xMouse = x;
			yMouse = y;
			}
			
			//middle button down, scale the object
			if((e.getModifiers() & InputEvent.BUTTON2_MASK) != 0){
			scale *= Math.pow(1.1, (y-yMouse) * 0.5);
			xMouse = x;
			yMouse = y;
			}			
		}
		@Override
		public void mouseMoved(MouseEvent e) {
			xMouse = e.getX();		
			yMouse = e.getY();	
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
}