
import java.awt.image.*;

public class Tile{


	private boolean blk; // attributes of Tiles
	private BufferedImage img;

	public Tile(BufferedImage img, boolean blk){ // constructor

		this.blk = blk;
		this.img = img;
	}
	public boolean isBlocked(){
		return blk;
	}
	public BufferedImage getImage(){
		return img;
	}


}
