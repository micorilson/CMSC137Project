import java.io.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.image.*;
public class TileMap{
	private int terrainHeight;
	private int terrainWidth;
	private int a;
	private int b;
	private BufferedImage tileset;
	private int[][] terrain;
	private Tile[][] Tiles;
	private int sizeOfTile;


	public void loadTiles(String s){
		try{
			tileset = ImageIO.read(new File(s));
			int numTilesAcross = (tileset.getWidth() + 1)/ (sizeOfTile + 1);
			Tiles = new Tile[2][numTilesAcross];
			BufferedImage subimage;

			for(int col = 0; col< numTilesAcross; col++){

			subimage = tileset.getSubimage(col*sizeOfTile + col,0,sizeOfTile,sizeOfTile);
			Tiles[0][col] = new Tile(subimage, false);
			subimage = tileset.getSubimage(col * sizeOfTile + col,sizeOfTile + 1,sizeOfTile,sizeOfTile);
			Tiles[1][col] = new Tile(subimage,true);

			};



			}catch(Exception e){
			e.printStackTrace();
			}
	}

	public TileMap(String s,int sizeOfTile) {
		this.sizeOfTile = sizeOfTile;
		try{
		BufferedReader br = new BufferedReader(new FileReader(s));

		terrainWidth = Integer.parseInt(br.readLine());
		terrainHeight = Integer.parseInt(br.readLine());
		terrain = new int[terrainHeight][terrainWidth];

		String delimiters = "\\s+";
		for(int row = 0; row < terrainHeight; row++){
			String line = br.readLine();
			String[] tokens = line.split(delimiters);
			for(int col = 0; col < terrainWidth;col++){
			terrain[row][col] = Integer.parseInt(tokens[col]);
			}
		}
		}catch(Exception e){

		}

	}


	public void update() {

	}
	public Boolean isBlocked(int row,int col){
	int rc = terrain[row][col];
	int r = rc / (Tiles[0].length);
	int c = rc % (Tiles[0].length);
	return Tiles[r][c].isBlocked();
}
	public void draw(Graphics2D g){


		for(int row = 0; row < terrainHeight;row++){
			for(int col = 0; col <terrainWidth;col++){

			int rc = terrain[row][col];
			int r = rc/ Tiles[0].length;
			int c = rc % Tiles[0].length;

			g.drawImage(Tiles[r][c].getImage(),a + col * sizeOfTile, b+ row * sizeOfTile,null);

			}
		}
	}

}
