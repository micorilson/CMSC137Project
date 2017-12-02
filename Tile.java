
import java.awt.image.*;

public class Tile{


	private boolean blk; // attributes of Tiles
	private BufferedImage img;
	private String type;
	private int lifepoint;

	public Tile(BufferedImage img, boolean blk){ // constructor
		
		this.blk = blk;
		this.img = img;
		this.lifepoint = 0;
		this.type = "default";
	}
	public boolean isBlocked(){
		return blk;
	}
	public BufferedImage getImage(){
		return img;
	}
	public String getType(){
		return type;
	}
	public int getLifePoint(){
		return lifepoint;
	}
	
	public void setType(String type){
		this.type = type ;
	}
	public void setLifePoint(int lifepoint){
		this.lifepoint = lifepoint;
	}


}
