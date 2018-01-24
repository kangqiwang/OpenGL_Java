package Lab3;

import static com.jogamp.opengl.GL3.*;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.swing.JFrame;

import Lab3.Basic.ShaderProg;
import Lab3.Basic.Transform;
import Lab3.Objects.SCube;
import Lab3.Objects.SObject;
import Lab3.Objects.SSphere;
import Lab3.Objects.STeapot;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

public class VC03 extends JFrame{

	final GLCanvas canvas; //Define a canvas 
	final FPSAnimator animator=new FPSAnimator(60, true);
	final Renderer renderer = new Renderer();

	public VC03() {
        GLProfile glp = GLProfile.get(GLProfile.GL3);
        GLCapabilities caps = new GLCapabilities(glp);
        canvas = new GLCanvas(caps);

		add(canvas, java.awt.BorderLayout.CENTER); // Put the canvas in the frame
		canvas.addGLEventListener(renderer); //Set the canvas to listen GLEvents
		
		animator.add(canvas);

		setTitle("CG03");
		setSize(500,500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		animator.start();
		canvas.requestFocus();
		
		}

	public static void main(String[] args) {
		new VC03();

	}

	class Renderer implements GLEventListener, MouseListener, MouseMotionListener {

		private Transform T = new Transform(); //model_view transform

		//VAOs and VBOs parameters
		private int idPoint=0, numVAOs = 2;
		private int idBuffer=0, numVBOs = 2;
		private int idElement=0, numEBOs = 2;
		private int[] VAOs = new int[numVAOs];
		private int[] VBOs = new int[numVBOs];
		private int[] EBOs = new int[numEBOs];

		//Model parameters
		private int[] numElements = new int[numEBOs];
		private long vertexSize; 
		private int vPosition;
		
		//Transformation parameters
		private int ModelView;
		private int Projection; 
		//Transformation parameters
		private int NormalTransform;
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

			gl.glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
			
			//Transformation for the first object (sphere)
			T.initialize();
			//******************************************
			//Add code here to transform (including scale) 
			//the first object (sphere) to appropriate location
			T.scale(0.3f, 0.3f, 0.3f);
			T.rotateX(-35);
			ry=5;
			T.scale(scale, scale, scale);
			T.rotateX(rx);
			T.rotateY(ry);
			ty=(float) 0.5;
			T.translate(tx, ty, 0);
			
			
			//Locate camera
			T.lookAt(0, 0, 0, 0, 0, -100, 0, 1, 0);  	//Default					
			
			//Send model_view and normal transformation matrices to shader. 
			//Here parameter 'true' for transpose means to convert the row-major  
			//matrix to column major one, which is required when vertices'
			//location vectors are pre-multiplied by the model_view matrix.
			gl.glUniformMatrix4fv( ModelView, 1, true, T.getTransformv(), 0 );			

			//bind and draw the first object
			idPoint=0;
			idBuffer=0;
			idElement=0;
			bindObject(gl);
		    gl.glDrawElements(GL_TRIANGLES, numElements[idElement], GL_UNSIGNED_INT, 0);	

			//******************************************
			//Add transformation, binding and drawing code here 
		    //to put the second object (teapot) to appropriate place
		 //   gl.glMatrixMode();
//		    idPoint=10;
//		    idBuffer=10;
//		    idElement=10;
//		    bindObject(gl);
//		    gl.glDrawElements(GL_TRIANGLES, numElements[idElement], GL_UNSIGNED_INT, 0);
		}

		
		@Override
		public void init(GLAutoDrawable drawable) {
			GL3 gl = drawable.getGL().getGL3(); // Get the GL pipeline object this 
			
			
			gl.glEnable(GL_CULL_FACE); 

			//compile and use the shader program
			ShaderProg shaderproc = new ShaderProg(gl, "Lab3.vert", "Lab3.frag");
			int program = shaderproc.getProgram();
			gl.glUseProgram(program);

			// Initialize the vertex position and normal attribute in the vertex shader    
		    vPosition = gl.glGetAttribLocation( program, "vPosition" );
		    gl.glGetAttribLocation( program, "vNormal" );

		    // Get connected with the ModelView, NormalTransform, and Projection matrices
		    // in the vertex shader
		    ModelView = gl.glGetUniformLocation(program, "ModelView");
		    Projection = gl.glGetUniformLocation(program, "Projection");

			
		    // Generate VAOs, VBOs, and EBOs
		    gl.glGenVertexArrays(numVAOs,VAOs,0);
			gl.glGenBuffers(numVBOs, VBOs,0);
			gl.glGenBuffers(numEBOs, EBOs,0);

		    SObject sphere = new SSphere();
			idPoint=0;
			idBuffer=0;
			idElement=0;
			createObject(gl, sphere);

			//******************************************
			//create the second object: a teapot
			//using the STeapot class provided
		   //	SObject steapot=new STeapot();
		    //createObject(gl, steapot);
		    
			//create the first object: a sphere

		    // This is necessary. Otherwise, the The color on back face may display 
		    gl.glDepthFunc(GL_LESS);
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
		public void dispose(GLAutoDrawable drawable) {
			// TODO Auto-generated method stub
			
		}
		
		public void createObject(GL3 gl, SObject obj) {
			float [] vertexArray = obj.getVertices();
			int [] vertexIndexs =obj.getIndices();
			numElements[idElement] = obj.getNumIndices();

			bindObject(gl);
			
			FloatBuffer vertices = FloatBuffer.wrap(vertexArray);

		    // Create an empty buffer with the size we need 
			// and a null pointer for the data values
			vertexSize = vertexArray.length*(Float.SIZE/8);
			gl.glBufferData(GL_ARRAY_BUFFER, vertexSize, 
					vertices, GL_STATIC_DRAW); // pay attention to *Float.SIZE/8
		    
			// Load the real data separately.  We put the colors right after the vertex coordinates,
		    // so, the offset for colors is the size of vertices in bytes
//		    gl.glBufferSubData( GL_ARRAY_BUFFER, 0, vertexSize, vertices );

			IntBuffer elements = IntBuffer.wrap(vertexIndexs);			

			long indexSize = vertexIndexs.length*(Integer.SIZE/8);
			gl.glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexSize, 
					elements, GL_STATIC_DRAW); // pay attention to *Float.SIZE/8						
			gl.glEnableVertexAttribArray(vPosition);
			gl.glVertexAttribPointer(vPosition, 3, GL_FLOAT, false, 0, 0L);
//			gl.glEnableVertexAttribArray(vNormal);
//		    gl.glVertexAttribPointer(vNormal, 3, GL_FLOAT, false, 0, vertexSize);
		}

		public void bindObject(GL3 gl){
			gl.glBindVertexArray(VAOs[idPoint]);
			gl.glBindBuffer(GL_ARRAY_BUFFER, VBOs[idBuffer]);
			gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBOs[idElement]);			
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
