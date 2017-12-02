import java.awt.image.*;

public class Obstacle2{


	private boolean blk; // attributes of obstacle2
	private BufferedImage img;
	private int lifepoint;

	public Obstacle2(BufferedImage img, boolean blk){ // constructor
		this.lifepoint = 3;
		this.blk = blk;
		this.img = img;
	}
	public boolean isBlockedTwo(){
		return blk;
	}
	public BufferedImage getImageTwo(){
		return img;
	}
	public int getLifePointTwo(){
		return lifepoint;
	}


}
