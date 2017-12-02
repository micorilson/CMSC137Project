import java.awt.BorderLayout;
import javax.swing.JFrame;
public class Game{
	public static void main(String[] args){

	
	JFrame mainframe = new JFrame("Team Wars"); // new frame
	GamePanel map = new GamePanel(); // new panel map
	//JFrame map = new JFrame("map");
	mainframe.setLayout(new BorderLayout());

	mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//mainframe.setContentPane(new GamePanel());
	mainframe.add(map, BorderLayout.SOUTH);
	mainframe.setSize(1200,800);	
	mainframe.setVisible(true);

	
	
	
}

}
