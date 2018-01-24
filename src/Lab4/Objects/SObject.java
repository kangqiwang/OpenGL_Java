package Lab4.Objects;

public abstract class SObject {
	
	protected int numVertices;
	protected int numIndices;
	protected float [] vertices;//å?‘é‡?å??æ ‡ç‚¹
	protected float [] normals;//å?‘é‡?æ–¹å?‘
	protected float [] textures;//
	protected int [] indices;//ç‚¹
	protected boolean updated;

	// Shortcuts for math constants and functions
	protected final double PI = Math.PI;

	protected final float sin(double i) {
      return (float) Math.sin(i); 
    }
	
	protected final float cos(double i) { 
      return (float) Math.cos(i); 
    }
	
	public SObject() {
		numVertices = 0;
		numIndices = 0;
		vertices = null;
		normals = null;
		textures = null;
		indices = null;
		updated =false;
	}
	
	protected abstract void genData();

	protected void update() {
		if(!updated){
			genData();
			updated =true;
		}
	}
	
	
	public int getNumVertices(){
		update();
		return numVertices;
	}	
	
	public int getNumIndices(){
		update();
		return numIndices;
	}	

	public float[] getVertices(){
		update();
		return vertices;
	}	

	public float[] getNormals(){
			update();
			return normals;
	}	

	public float[] getTextures(){
		update();
		return textures;
	}	

	public int[] getIndices(){
		update();
		return indices;
	}	
}
