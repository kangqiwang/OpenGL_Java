package Lab4.Lab5;

/*NWET Version for Mac*/
import static com.jogamp.opengl.GL3.*;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;


import com.jogamp.nativewindow.WindowClosingProtocol.WindowClosingMode;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import Lab4.Basic.ShaderProg;
import Lab4.Basic.Transform;

public class VC05M extends JFrame{
    private GLWindow window;

	final FPSAnimator animator=new FPSAnimator(60, true);
	final Renderer renderer = new Renderer();

	public VC05M() {
        GLProfile glp = GLProfile.get(GLProfile.GL3);
        GLCapabilities caps = new GLCapabilities(glp);
        window = GLWindow.create(caps);

		window.addGLEventListener(renderer); //Set the window to listen GLEvents
		animator.add(window);

		window.setDefaultCloseOperation(WindowClosingMode.DISPOSE_ON_CLOSE); 
		//The above line will only close the window but not exit the program
		//Add System.exit(0) in dispose() function will cause it exit

		window.setTitle("VC05");
		window.setSize(500,500);
		window.setVisible(true);
		animator.start();
		}

	public static void main(String[] args) {
		new VC05M();

	}

	class Renderer implements GLEventListener{
		private Texture texture;
		private Transform T = new Transform();

		//VAOs and VBOs parameters
		private int idPoint=0, numVAOs = 1;
		private int idBuffer=0, numVBOs = 1;
		private int idElement=0, numEBOs = 1;
		private int[] VAOs = new int[numVAOs];
		private int[] VBOs = new int[numVBOs];
		private int[] EBOs = new int[numEBOs];

		//Model parameters
		private int numElements = 6;

		//Transformation parameters
		private int ModelView;
		private int Projection; 
		
		//texture parameters
		ByteBuffer texImg;
        private int texWidth, texHeight;

		@Override
		public void display(GLAutoDrawable drawable) {
			GL3 gl = drawable.getGL().getGL3(); // Get the GL pipeline object this 
			
			gl.glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);

			T.initialize();
			T.translate(-0.5f, -0.5f, 0f);
			T.scale(2f, 2f, 1f);

			//Locate camera
//			T.LookAt(0, 0, 0, 0, 0, -1, 0, 1, 0);  	//Default					
			
			// send model_view to shader. Here parameter true for transpose 
			//means to convert the row-major matrix to column major one,
			//which is required when pre-multiply 			
		    gl.glUniformMatrix4fv( ModelView, 1, true, T.getTransformv(), 0 );
		    
		    gl.glDrawElements(GL_TRIANGLES, numElements, GL_UNSIGNED_INT, 0);	//for solid sphere		
}

		@Override
		public void dispose(GLAutoDrawable drawable) {
			System.exit(0);			
		}

		@Override
		public void init(GLAutoDrawable drawable) {
			GL3 gl = drawable.getGL().getGL3(); // Get the GL pipeline object this 

	        try {
	            texture = TextureIO.newTexture(new File("WelshDragon.jpg"), false);
	        } catch (IOException ex) {
	            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
	        }
	        
//	        texture.setTexParameteri(gl, GL_TEXTURE_WRAP_S, GL_REPEAT); //Default GL_CLAMP_TO_EDGE
//	        texture.setTexParameteri(gl, GL_TEXTURE_WRAP_T, GL_REPEAT);
//	        texture.setTexParameteri(gl, GL_TEXTURE_MAG_FILTER, GL_LINEAR); //Default GL_CLAMP_TO_EDGE
//	        texture.setTexParameteri(gl, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

	        float [] vertexCoord = { //define vertex coordinates 
					0, 0, 0,
					1, 0, 0,
					0, 1, 0,
					1, 1, 0
			};
			float[] texCoord ={ //define texture coordinates
//					0, 0,
//					1, 0,
//					0, 1,
//					1, 1					
					-2, -2,
					2, -2,
					-2, 2,
					2, 2

					// It's better to call the following functions
					// to get the image texture coordinates
//					texture.getImageTexCoords().left(),	
//					texture.getImageTexCoords().bottom(),
//					texture.getImageTexCoords().right(),	
//					texture.getImageTexCoords().bottom(),
//					texture.getImageTexCoords().left(),	
//					texture.getImageTexCoords().top(),
//					texture.getImageTexCoords().right(),	
//					texture.getImageTexCoords().top(),
			};

			float[] vertexColours ={ //define vertex colours
					0, 0, 0,
					1, 0, 0,
					0, 1, 0,
					0, 0, 1					
//					1, 1, 1,
//					1, 1, 1,
//					1, 1, 1,
//					1, 1, 1					
			};
			
			int [] vertexIndexs ={
					0, 1, 2,
					2, 1, 3
			};
			
		    // This is necessary. Otherwise, the The color on back face may display 
//		    gl.glDepthFunc(GL_LEQUAL);
		    gl.glEnable(GL_DEPTH_TEST);

			gl.glGenVertexArrays(numVAOs,VAOs,0);
			gl.glBindVertexArray(VAOs[idPoint]);

			FloatBuffer data = FloatBuffer.wrap(vertexCoord);
			FloatBuffer colours = FloatBuffer.wrap(vertexColours);
			FloatBuffer textures = FloatBuffer.wrap(texCoord);
			
			gl.glGenBuffers(numVBOs, VBOs,0);
			gl.glBindBuffer(GL_ARRAY_BUFFER, VBOs[idBuffer]);

			// Create an empty buffer with the size we need 
			// and a null pointer for the data values
			long coordSize = vertexCoord.length*(Float.SIZE/8);
			long colourSize = vertexColours.length*(Float.SIZE/8);
			long texSize = texCoord.length*(Float.SIZE/8);
			gl.glBufferData(GL_ARRAY_BUFFER, coordSize + colourSize + texSize, 
					null, GL_STATIC_DRAW); // pay attention to *Float.SIZE/8
		    
			// Load the real data separately.  We put the colors right after the vertex coordinates,
		    // so, the offset for colors is the size of vertices in bytes
		    gl.glBufferSubData( GL_ARRAY_BUFFER, 0, coordSize, data );
		    gl.glBufferSubData( GL_ARRAY_BUFFER, coordSize, colourSize, colours );
		    gl.glBufferSubData( GL_ARRAY_BUFFER, coordSize + colourSize, texSize, textures );

			IntBuffer elements = IntBuffer.wrap(vertexIndexs);
			
			gl.glGenBuffers(numEBOs, EBOs,0);
			gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBOs[idElement]);

			long indexSize = vertexIndexs.length*(Integer.SIZE/8);
			gl.glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexSize, 
					elements, GL_STATIC_DRAW); // pay attention to *Float.SIZE/8						

			ShaderProg shaderproc = new ShaderProg(gl, "ColourTex.vert", "ColourTex.frag");
			int program = shaderproc.getProgram();
			gl.glUseProgram(program);
	
		   // Initialize the vertex position attribute in the vertex shader    
		    int vPosition = gl.glGetAttribLocation( program, "vPosition" );
			gl.glEnableVertexAttribArray(vPosition);
			gl.glVertexAttribPointer(vPosition, 3, GL_FLOAT, false, 0, 0L);

		    // Initialize the vertex color attribute in the vertex shader.
		    // The offset is the same as in the glBufferSubData, i.e., vertexSize
			// It is the starting point of the color data
		    int vColour = gl.glGetAttribLocation( program, "vColour" );
			gl.glEnableVertexAttribArray(vColour);
		    gl.glVertexAttribPointer(vColour, 3, GL_FLOAT, false, 0, coordSize);
		    
		    int vTexCoord = gl.glGetAttribLocation( program, "vTexCoord" );
		    gl.glEnableVertexAttribArray( vTexCoord );
		    gl.glVertexAttribPointer( vTexCoord, 2, GL_FLOAT, false, 0, coordSize+colourSize);

		    //Get connected with the ModelView matrix in the vertex shader
		    ModelView = gl.glGetUniformLocation(program, "ModelView");
		    //NormalTransform = gl.glGetUniformLocation(program, "NormalTransform");
		    Projection = gl.glGetUniformLocation(program, "Projection");

		    // Set the value of the fragment shader texture sampler variable
		    //   ("texture") to the the appropriate texture unit. In this case,
		    //   zero, for GL_TEXTURE0 which was previously set by calling
		    //   glActiveTexture().
		    gl.glUniform1i( gl.glGetUniformLocation(program, "tex"), 0 ); 		    		    
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
			T.reverseZ();
		    gl.glUniformMatrix4fv( Projection, 1, true, T.getTransformv(), 0 );			

		}
		
//	   private ByteBuffer readImage(String filename) throws IOException {
//
//	        ByteBuffer imgbuf;
//	        BufferedImage img = ImageIO.read(new FileInputStream(filename));
//
//	        texWidth = img.getWidth();
//	        texHeight = img.getHeight();
//	        DataBufferByte datbuf = (DataBufferByte) img.getData().getDataBuffer();
//	        imgbuf = ByteBuffer.wrap(datbuf.getData());
//	        return imgbuf;
//	    }
	}
}
