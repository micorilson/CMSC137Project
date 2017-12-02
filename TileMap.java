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
	private String currentImage;


	public void loadTiles(String s){
		try{
			tileset = ImageIO.read(new File(s)); // read image
			int numTilesAcross = (tileset.getWidth() + 1)/ (sizeOfTile + 1); // gets the number of tiles across
			Tiles = new Tile[2][numTilesAcross];
			BufferedImage subimage; // used to handle and manipulate the image data

			for(int col = 0; col< numTilesAcross; col++){
	
			subimage = tileset.getSubimage(col*sizeOfTile + col,0,sizeOfTile,sizeOfTile);
			Tiles[0][col] = new Tile(subimage, false);
			
			subimage = tileset.getSubimage(col * sizeOfTile + col,sizeOfTile + 1,sizeOfTile,sizeOfTile);
			Tiles[1][col] = new Tile(subimage,true);
			
			
			};
		/*#########################################################################################*/
			if(s == "map3.png"){ 
				currentImage = s;
				for(int row = 0; row < terrainHeight;row++){
					for(int col = 0; col <terrainWidth;col++){

					int rc = terrain[row][col];
					int r = rc/ Tiles[0].length;
					int c = rc % Tiles[0].length;
			
						if(rc == 17){
							Tiles[r][c].setLifePoint(1);
							Tiles[r][c].setType("Obstacle1");// brick
							 
						}else if(rc == 22){
							Tiles[r][c].setLifePoint(3);
							Tiles[r][c].setType("Obstacle2"); // gray one
						}else if(rc == 13){
							Tiles[r][c].setType("Floor");
						}else if(rc == 2){
							Tiles[r][c].setType("Border");
						}else if(rc == 15){
							Tiles[r][c].setType("Divider");
						}
				  }
				}
			}
		/*#########################################################################################*/
			if(s == "map.png"){ 
				currentImage = s;
				//System.out.println(s);
				for(int row = 0; row < terrainHeight;row++){
					for(int col = 0; col <terrainWidth;col++){

					int rc = terrain[row][col];
					int r = rc/ Tiles[0].length;
					int c = rc % Tiles[0].length;
			
						if(rc == 8){
							Tiles[r][c].setLifePoint(1);
							Tiles[r][c].setType("Obstacle1");
						}else if(rc == 14){
	 						Tiles[r][c].setLifePoint(3);
							Tiles[r][c].setType("Obstacle2");
						}else if(rc == 22){
							Tiles[r][c].setType("Floor");
						}else if(rc == 3 || rc == 2 || rc == 1){
							Tiles[r][c].setType("Border");
						}else if(rc == 16){
							Tiles[r][c].setType("Divider");
						}
				  }
				}
			}
		/*#########################################################################################*/
			if(s == "tileset.png"){ 
				currentImage = s;
				for(int row = 0; row < terrainHeight;row++){
					for(int col = 0; col <terrainWidth;col++){

					int rc = terrain[row][col];
					int r = rc/ Tiles[0].length;
					int c = rc % Tiles[0].length;
			
						if(rc == 4){
							Tiles[r][c].setLifePoint(1);
							Tiles[r][c].setType("Obstacle1");	 
						}else if(rc == 14){
							Tiles[r][c].setLifePoint(3);
							Tiles[r][c].setType("Obstacle2");
						}
						else if(rc == 1){
							Tiles[r][c].setType("Floor");
						}else if(rc == 12){
							Tiles[r][c].setType("Border");
						}else if(rc == 2){
							Tiles[r][c].setType("Divider");
						}
				  }
				}
			}
		/*#########################################################################################*/


			}catch(Exception e){
			e.printStackTrace();
			}
	}

	public TileMap(String s,int sizeOfTile) { // constructor of tilemap
		this.sizeOfTile = sizeOfTile;
		try{
		BufferedReader br = new BufferedReader(new FileReader(s)); // bufferreader 

		terrainWidth = Integer.parseInt(br.readLine()); // parse
		terrainHeight = Integer.parseInt(br.readLine());
		terrain = new int[terrainHeight][terrainWidth]; // terrain
		

		String delimiters = "\\s+"; // delimiters
		for(int row = 0; row < terrainHeight; row++){
			String line = br.readLine();
			String[] tokens = line.split(delimiters); // divides the image into tokens
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

			// Life Point checker for obstacle 1 and 2
			if(currentImage == "map3.png" ){
				if(Tiles[r][c].getType()=="Obstacle1" && Tiles[r][c].getLifePoint() == 0){ // obstacle 1(brick one) 
					
					Tiles[r][c].setType("Floor"); // change the obstacle to floor type
					rc = 13; // 13 is the floor type
					terrain[row][col] = rc; // changes the value from to 13
					r = rc/ Tiles[0].length;
					c = rc % Tiles[0].length;


				}else if(Tiles[r][c].getType()=="Obstacle2" && Tiles[r][c].getLifePoint() == 0){ // obstacle2 (gray one) 
				
					Tiles[r][c].setType("Floor"); // change the obstacle to floor type
					rc = 13; // 13 is the floor type
					terrain[row][col] = rc; // changes the value from to 13
					r = rc/ Tiles[0].length;
					c = rc % Tiles[0].length;
				}
			}if(currentImage == "map.png" ){
				if(Tiles[r][c].getType()=="Obstacle1" && Tiles[r][c].getLifePoint() == 0){ // obstacle 1 grass
						
						Tiles[r][c].setType("Floor"); // change the obstacle to floor type
						rc = 22; // 22 is the floor type
					terrain[row][col] = rc; // changes the value from to 22
					r = rc/ Tiles[0].length;
					c = rc % Tiles[0].length;
				}else if(Tiles[r][c].getType()=="Obstacle2" && Tiles[r][c].getLifePoint() == 0){ // obstacle 2 wood
						
						Tiles[r][c].setType("Floor"); // change the obstacle to floor type
						rc = 22; // 22 is the floor type
					terrain[row][col] = rc; // changes the value from to 22
					r = rc/ Tiles[0].length;
					c = rc % Tiles[0].length;
				}
			}if(currentImage == "tileset.png" ){
				if(Tiles[r][c].getType()=="Obstacle1" && Tiles[r][c].getLifePoint() == 0){ // obstacle1 (wood) lifepoint 
					
					Tiles[r][c].setType("Floor"); // change the obstacle to floor type
					rc = 1; // 1 is the floor type
					terrain[row][col] = rc; // changes the value from to 1
					r = rc/ Tiles[0].length;
					c = rc % Tiles[0].length;
				
				}else if(Tiles[r][c].getType()=="Obstacle2" && Tiles[r][c].getLifePoint() == 0){ // obstacle2 (glassy rock) 
					
					Tiles[r][c].setType("Floor"); // change the obstacle to floor type
					rc = 1; // 1 is the floor type
					terrain[row][col] = rc; // changes the value from to 1
					r = rc/ Tiles[0].length;
					c = rc % Tiles[0].length;
				}
			}			
			
			System.out.println(currentImage);
			System.out.println("#################################");
			System.out.println(rc);
			System.out.println(Tiles[r][c].getLifePoint());
			System.out.println(Tiles[r][c].getType());
			// draws the image on the tilemap
			g.drawImage(Tiles[r][c].getImage(),a + col * sizeOfTile, b+ row * sizeOfTile,null);
				
			}
		}
	}

}
