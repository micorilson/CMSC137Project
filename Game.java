
import javax.swing.JFrame;
public class Game{
	public static void main(String[] args){

	JFrame frame = new JFrame("Team Wars");

	frame.setContentPane(new GamePanel());
	frame.setVisible(true);

	frame.pack();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
}

}
