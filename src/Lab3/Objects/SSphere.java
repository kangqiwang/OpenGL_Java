package Lab3.Objects;



import java.util.Arrays;

public class SSphere extends SObject{
	private float radius;
	private float height;
	private int slices;//园上的点数
	private int stacks;//总共的园数
		
	public SSphere(){	
		super();
		init();
		update();
	}
		
	public SSphere(float radius, float height){
		super();
		init();
		this.radius = radius;
		this.height=height;
		update();
	}
	
	public SSphere(float radius, float height, int slices, int stacks){
		super();
		this.radius = radius;
		this.height=height;
		this.slices = slices;
		this.stacks = stacks;
		update();
	}
	
	private void init(){
		this.radius = 1;
		this.height=2;
		this.slices = 20;
		this.stacks = 20;
	}

	@Override
	protected void genData() {
		int i,j,k;
		
		double deltaLong=PI*2/slices;
		double zheight=height/stacks;
		double  deltaLat=PI/stacks;


		
		// Generate vertices coordinates, normal values, and texture coordinates
		
		numVertices = (slices+1)*(stacks-1)+2; 
		vertices = new float[numVertices*3];
		normals = new float[numVertices*3];
		textures = new float[numVertices*2];
		
		//North pole point
		normals[0] = 0; normals[1] = 0; normals[2] = 1;
		vertices[0] = 0; vertices[1] = 0; vertices[2] = (float) height/2;
		textures[0]= 0.5f; textures[1] = 1.0f;

		k = 1;
		//vertices on the main body
		
		for(i=1;i<stacks;i++){
			for(j=0;j<=slices;j++){
				normals[3*k] = cos(deltaLong*j);
				normals[3*k+1] = sin(deltaLong*j);
				normals[3*k+2] = cos(deltaLat*i);
				vertices[3*k] = radius*normals[3*k];
				vertices[3*k+1] = radius*normals[3*k+1];
				vertices[3*k+2] = radius*normals[3*k+2];
				//vertices[3*k+2] = height;
				textures[2*k] = (float) j/slices;
				textures[2*k+1] = 1-(float) i/stacks;
				k++;
			}
		}

		//South pole point
		//******************************************
		//Add the normals and vertices at the South pole point
		normals[3*k] = 0; normals[3*k+1] = 0; normals[3*k+2] = -1;
		vertices[3*k] = 0; vertices[3*k+1] = 0; vertices[3*k+2] = -height/2;
		textures[2*k] = 0.5f; textures[2*k+1] = 0.0f;  
		
		
		
		// Generate indices for triangular mesh
		numIndices = (stacks-1)*slices*6;
		indices = new int[numIndices];
		
		k = 0;
		
		/*
		 *  mesh the body, except the top and the bottom
		 */
		
		
		
		for(i=1;i<stacks-1;i++)
		{
			for(j=1;j<=slices;j++) {
				
				//each quad gives two triangles
				//triangle one
				indices[k++] = (i-1)*(slices+1)+j;
				indices[k++] = i*(slices+1)+j;
				indices[k++] = i*(slices+1)+j+1;
				//triangle two
				indices[k++] = (i-1)*(slices+1)+j;
				indices[k++] = i*(slices+1)+j+1;
				indices[k++] = (i-1)*(slices+1)+j+1;
			}
		}
		
		
		
		for(j=1;j<=slices;j++){
			indices[k++] = 0;
			indices[k++] = j;
			indices[k++] = j+1;
		}

		//South Pole, numElement:slices*3 
		int temp = numVertices-1;
		for(j=temp-1;j>temp-slices-1;j--){
			indices[k++] = temp;
			indices[k++] = j;
			indices[k++] = j-1;
		}
		
		
		
		
		
		
		
		
		
		//North Pole, numElement:slices*3 	
		//******************************************
		//Add here the indices in North Pole region
//		System.out.println();
//		System.out.println(numIndices);
//		System.out.println(slices);
//		System.out.println(stacks);
		
		
	//	System.out.println("vertices"+Arrays.toString(vertices));
		//System.out.println("normals"+Arrays.toString(normals));
		
		
//		for(i=numVertices-2;i>numVertices-slices-2;i--)
//		{
//			indices[k++]=(numVertices-1);
//			
//			indices[k++]=i;
//			
//			indices[k++]=i-1;
//			System.out.println(k);
//			
//		}
		

		
		//South Pole, numElement:slices*3 
		//******************************************
//		//Add here the indices in South Pole region
//		for(i=0;i<slices;i++) {
//		//System.out.println(k);
//		indices[k++]=0;
//		//System.out.println(k);
//		indices[k++]=i+1;
//		//System.out.println(k);
//		indices[k++]=i+2;
//		}
////		k from 1-119
		//k=0;
//		System.out.println();
//		System.out.println(k+"outsied");
		//Main body, numElement:(stacks-2)*slices*6 
//		for(i=1;i<stacks-1;i++){
//			for(j=1;j<slices;j++){
//				//each quad gives two triangles
//				//triangle one
//				//System.out.println(k+"inside first");		
//				//System.out.println("i="+i);
//			//	System.out.println("j="+j);
//				indices[k++] = (i-1)*(slices+1)+j;
//			//	System.out.println(indices[k-1]);
//				indices[k++] = i*(slices+1)+j;
//			//	System.out.println(indices[k-1]);
//				indices[k++] = i*(slices+1)+j+1;
//			//	System.out.println(indices[k-1]);
//				//triangle two
//				indices[k++] = (i-1)*(slices+1)+j;
//			//	System.out.println(indices[k-1]);
//				indices[k++] = i*(slices+1)+j+1;
//				//System.out.println(indices[k-1]);
//				indices[k++] = (i-1)*(slices+1)+j+1;
//				//System.out.println(indices[k-1]);
//			}
//			
//		}

		//System.out.println("indices"+Arrays.toString(indices));
	}	
	
	public void setRadius(float radius){
		this.radius = radius;
		updated = false;
	}
		
	public void setSlices(int slices){
		this.slices = slices;
		updated = false;
	}

	public void setStacks(int stacks){
		this.stacks = stacks;
		updated = false;
	}
		
	public float getRadius(){
		return radius;
	}
		
	public int getSlices(){
		return slices;
	}

	public int getStacks(){
		return stacks;
	}	
}