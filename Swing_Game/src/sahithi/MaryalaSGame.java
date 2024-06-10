package sahithi;



// Import everything from awt package
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
// for key listener
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
// for file ops
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

// Import everything from swing package
import javax.swing.JFrame;
import javax.swing.JPanel;


public class MaryalaSGame extends JPanel implements KeyListener, Runnable  // Implementing KeyListener here allows you to react to key presses
{
	private static final int frameWidth = 1200;  // Set Dimension of full frame
	private static final int frameHeight = 800;
	private static final String[] levels ={"C:\\Users\\nagas\\eclipse-workspaceGrind75\\sahithi\\src\\sahithi\\level1.txt", "C:\\Users\\nagas\\eclipse-workspaceGrind75\\sahithi\\src\\sahithi\\level2.txt", "C:\\Users\\nagas\\eclipse-workspaceGrind75\\sahithi\\src\\sahithi\\level3.txt"};
	private int levelIndex;
	private boolean win;
	private String msg;
	private char[][] board;
 	private ArrayList<Block> walls;
 	private ArrayList<Paintable> paintedThings;
 	private PlayerBall player;
 	private Ball npc;
 	private Monster monster;
 	private Blocker blocker;
	private JFrame frame;
	private Font f;
	private Thread t;
	private boolean gameLoaded, readDirections;
	private long startTime;	// Long is a high-capacity int

	public MaryalaSGame()
	{
		// Load and configure Java Graphics
		frame=new JFrame();
		frame.add(this);  //Add Object to Frame, this will invoke the paintComponent method
		frame.setSize(frameWidth,frameHeight);
		frame.setVisible(true);
		frame.addKeyListener(this); /***********  NEEDED TO USE KEYLISTENER ******/
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Set some more basic game parameters
		win = false;
		gameLoaded = false;
		readDirections = false;
		msg = "MESSAGE DISPLAYED AT TOP OF SCREEN";  // Initial message can be overwritten
		f=new Font("TIMES NEW ROMAN",Font.BOLD,24);  // Set Font  https://docs.oracle.com/javase/7/docs/api/java/awt/Font.html
		t=new Thread(this);  // The thread
		t.start();

		// Call method to load properties from file
		// NOTE: FOR NOW SOME BASIC PROPERTIES ARE ADDED HERE
		levelIndex = 0;
		loadPropertiesFromFile(levels[levelIndex]);
	}

	/*********** ALL PAINTING INITIATED FROM THIS METHOD *************/
	public void paintComponent(Graphics g)

	{
		super.paintComponent(g);  // Need to call this first to initialize in parent class, do not change
		Graphics2D g2d = (Graphics2D)g;  // Cast to Graphics2D which is a subclass of Graphics with additional properties

		/*  Fill background */
		g2d.setColor(new Color(200, 50,122));  // Color on RGB scale
		g2d.fillRect(0,0,frameWidth,frameHeight);

		if (!(gameLoaded && readDirections)){
			introScreen(g2d);
			return;
	  }
	  
		g2d.setColor(Color.BLUE);
		g2d.setFont(f);  // NOTE: Font initialized in the constructor
		g2d.drawString(msg,100,30);  // Write text from msg String
		for (Paintable p : paintedThings)
			p.paint(g2d);
		
		if(player.intersects(monster.getBounds2D()))
	    	badEndScreen(g2d);
	}

	public void introScreen(Graphics2D g2d){
		g2d.setColor(Color.ORANGE);
		g2d.setFont(new Font("TIMES NEW ROMAN",Font.BOLD,60));  // NOTE: Font initialized in the constructor
		g2d.drawString("WELCOME TO THE BLOCK 2B GAME",60,200);
		g2d.setColor(Color.BLACK);
		g2d.setFont(new Font("TIMES NEW ROMAN",Font.BOLD,32));
		g2d.drawString("You are the Cyan Ball",60,250);
		g2d.drawString("Try to catch the Little Ball",60,320);
		g2d.drawString("Move using the arrow keys",60,390);
		g2d.drawString("Press any key when ready",60,460);
	}

	public void badEndScreen(Graphics2D g2d){
		System.out.println("inside badEndScreen");
		g2d.setColor(Color.ORANGE);
		g2d.setFont(new Font("TIMES NEW ROMAN",Font.BOLD,60));
		int count = 0;
		if(player.intersects(npc.getBounds2D()))
			count++;
		if(player.intersects(monster.getBounds2D())) {
			System.out.println(" Inside the method if statement");
			g2d.drawString("Womp womp you lost try again",60,320);
		}
		else if(count == 3)
			g2d.drawString("Congradulations you won!",60,320);
	}

	
	public void callBadEndScreen() {
        BufferedImage bufferedImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        badEndScreen(g2d);
        g2d.dispose();
    }
	public double getTime(){
		return (long)(System.currentTimeMillis()-startTime)/100/10.0;
	}

	/******** THIS METHOD EXECUTES GAME LOGIC **************/
	public void run(){
		delay(1000); // Initial Delay to allow load of data
		repaint();
		while(!(gameLoaded && readDirections))
			delay(100);

		startTime = System.currentTimeMillis();
		while(!win)  // Keep going until win or window is closed
		{
			// Move things once you have moveable items
			npc.move();
			// This last bit of code can go inside the run() method once npc is initialized
			String touching = touchingWall(npc);
			if (touching.length()>0){
				if (touching.contains("L") || touching.contains("R"))
					npc.setDX(-npc.getDX());
				else if (touching.contains("B") || touching.contains("T"))
					npc.setDY(-npc.getDY());
				else {
					npc.setDX(-npc.getDX());
					npc.setDY(-npc.getDY());
				}
				npc.move();
			}
			npc.move();
			
			
			// Move things once you have moveable items
			monster.move();
			// This last bit of code can go inside the run() method once npc is initialized
			String touching1 = touchingWall(monster);
			if (touching1.length()>0){
				if (touching1.contains("L") || touching1.contains("R"))
					monster.setDX(-monster.getDX());
				else if (touching1.contains("B") || touching1.contains("T"))
					monster.setDY(-monster.getDY());
				else {
					monster.setDX(-monster.getDX());
					monster.setDY(-monster.getDY());
				}
				monster.move();
			}
			monster.move();
			
/*
			if(player.intersects(monster.getBounds2D())
			    	badEndScreen(g2d);
			    	return;
				}
	*/		
			if(player.intersects(monster.getBounds2D()))
			{
				/*
				 * System.out.println("Touched Monster"); callBadEndScreen();
				 * 
				 * msg = "Cleared Level"; repaint(); delay(1000);
				 * 
				 * win = false; return;
				 */
				//call badEndScreen
				return;
			}
			
			

			if(player.intersects(npc.getBounds2D())){
				player.addScore(1);
				npc.setColor(Color.CYAN);
				npc.setDX(npc.getDX()*0.99);
				npc.setDY(npc.getDY()*0.99);
				if(npc.getDX()<0.001 && npc.getDY()<0.001){
					msg = "Cleared Level";
					repaint();
					delay(1000);
					levelIndex = (levelIndex+1) % levels.length;
					loadPropertiesFromFile(levels[levelIndex]);
					delay(1000);
				}
			}

			

			if(player.intersects(monster.getBounds2D())){
				player.addScore(1);
				npc.setColor(Color.CYAN);
				npc.setDX(npc.getDX()*0.99);
				npc.setDY(npc.getDY()*0.99);
				
			}

			// handle all intersections
			msg = "Level #"+(levelIndex+1)+"  Time = "+getTime()+"  Score = "+player.getScore();
			delay(5);  //play with this number frame refresh delay in ms
			repaint();
		}
		// Set Win Message
		msg = "********  YOU WIN   ********";
	}

	//Return direction of intersection(s)
	private String direction(Ball ball, Block w){
	     String s = "";
	     Point2D ballTop = new Point2D.Double(ball.getCenterX(),ball.getY());
	     Point2D ballRight = new Point2D.Double(ball.getMaxX(),ball.getCenterY());
	     Point2D ballBottom = new Point2D.Double(ball.getCenterX(),ball.getMaxY());
	     Point2D ballLeft = new Point2D.Double(ball.getX(),ball.getCenterY());

		if (w.contains(ballTop))   	// Ball on Bottom of Block
			s+="B";
		if (w.contains(ballRight))	// Ball Left of Block
			s+="L";
		if (w.contains(ballBottom)) // Ball on Top of Block
			s+="T";
		if (w.contains(ballLeft))		// Ball on Right of Block
			s+="R";

		return s;
	}

	public String touchingWall(Ball b){
		String s = "";
		for (Block w : walls){
			if (b.intersects(w))
				s += "I->"+direction(b,w);
		}
		/*for(Blocker l : blocker){
			if(b.intersects(l))
				s += "I->"+direction(b,l);
		}*/
		return s;
	}

	/*  Key Listener Methods Below
	https://docs.oracle.com/javase/7/docs/api/java/awt/event/KeyListener.html
	*/
	public void keyPressed(KeyEvent ke)
	{
		// Print key codes to know how to trigger events
		System.out.println(ke.getKeyCode());
		readDirections = true;	// you can make this work for a specific key

		if(ke.getKeyCode() == 32){	// Space Bar Change Level
			levelIndex = (levelIndex+1) % levels.length;
			loadPropertiesFromFile(levels[levelIndex]);
			msg = "LOADING NEW LEVEL";
			repaint();
			delay(1000);
		}

		player.setDX(0);
		player.setDY(0);
		if(ke.getKeyCode() == 37){ // Left Arrow
			if(!touchingWall(player).contains("R"))
				player.setDX(-4);
			else
				player.setDX(4);
		}
		if(ke.getKeyCode() == 39){ // Right Arrow
			if(!touchingWall(player).contains("L"))
				player.setDX(4);
			else
				player.setDX(-4);
		}
		if(ke.getKeyCode() == 38){
			if(!touchingWall(player).contains("B"))
				player.setDY(-4);
			else
				player.setDY(4);
		}
		if(ke.getKeyCode() == 40){
			if(!touchingWall(player).contains("T"))
				player.setDY(4);
			else
				player.setDY(-4);
		}

		player.move();
	}

	

	public void keyReleased(KeyEvent ke){}

	public void keyTyped(KeyEvent ke){}

	public void loadPropertiesFromFile(String fileName){
			// Hardcoded for now.  Will adapt to load from file
			int size = 60;
			char[][] temp = {{'@','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#'},
											 {'#',' ',' ',' ',' ',' ',' ','#',' ','#',' ','#',' ','#','#','#'},
											 {'#',' ',' ',' ',' ','@',' ','#',' ','#',' ','#',' ','#','#','#'},
											 {'#',' ','#',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','#','#'},
											 {'#',' ','#','#','#',' ',' ',' ','#',' ',' ','#',' ','#','#','#'},
											 {'#',' ',' ',' ','#',' ',' ',' ',' ','#',' ','#',' ','#','@','#'},
											 {'#',' ','@',' ','#','#','#','#',' ','#',' ',' ',' ',' ',' ','#'},
											 {'#',' ',' ',' ',' ',' ',' ','#',' ','#',' ',' ','#','#','#','#'},
											 {'#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#'}};
			try{
				BufferedReader input = new BufferedReader(new FileReader(fileName));
				String[] propertyNames = {"size","numRows","numCols","numBlocker","numMons"}; // you need to add to this
				String[] valuesAsStrings = new String[propertyNames.length]; //hold values assosciated with fields
				for (int i = 0; i < propertyNames.length; i++){
					String line = input.readLine(); // get a single line
					String[] tokens = line.split("="); // tokens[0] == prop. name, tokens[1] == prop. value as String
					if (tokens[0].trim().equals(propertyNames[i])) // Note trim is used to remove unwanted spaces
						valuesAsStrings[i] = tokens[1].trim();
					else
          				System.out.println("Property Name "+propertyNames[i]+" not found");
				}
				size = Integer.parseInt(valuesAsStrings[0]);
				int numRows = Integer.parseInt(valuesAsStrings[1]);  //convert to int to store field
      			int numCols = Integer.parseInt(valuesAsStrings[2]);
      			int numBlocker = Integer.parseInt(valuesAsStrings[3]);
      			int numMons = Integer.parseInt(valuesAsStrings[4]);

      			board = new char[numRows][numCols];
      			for(int r = 0; r < numRows; r++){
					String rowString = input.readLine();
					for(int c = 0; c < numCols; c++){
						board[r][c] = rowString.charAt(c);
					}
				}
			}catch (IOException io) // catch errors
			{
				System.err.println("Exception =>"+io);
			}
			walls = new ArrayList<Block>();
			for(int r=0;r<board.length;r++){
				for(int c=0;c<board[0].length;c++){
					if (board[r][c]=='#')
							walls.add(new Block(c*size+size,r*size+size,size,size,Color.MAGENTA));
					if (board[r][c]=='@')
							walls.add(new Block(c*size+size,r*size+size,size,size,Color.PINK));
					if (board[r][c] == 'P')
						if(player == null)
							player = new PlayerBall(c*size+size,r*size+size,size,Color.BLACK);
						else{
							int x = player.getScore();
							player = new PlayerBall(c*size+size,r*size+size,size,Color.BLACK);
							player.addScore(x);
						}
					if (board[r][c] == 'N')
							npc = new Ball(c*size+size,r*size+size,size/2,Color.WHITE);
					if(board[r][c] == 'B')
							blocker = new Blocker(c*size+size,r*size+size,size,size, Color.ORANGE);
					if(board[r][c] == 'M')
							monster = new Monster(c*size+size,r*size+size,size-2, Color.YELLOW);

				}
			}
			npc.setDX(1);
			npc.setDY(1);
			
			monster.setDX(1);
			monster.setDY(1);

			paintedThings = new ArrayList<Paintable>();
			paintedThings.addAll(walls);
			paintedThings.add(player);
			paintedThings.add(npc);
			paintedThings.add(monster);
			paintedThings.add(blocker);
			gameLoaded = true;
	}

	public void delay(int ms){
		try{
			t.sleep(ms);  // time in milliseconds (0.001 seconds) to delay
		} catch(InterruptedException e) {}

	}

	public static void main(String args[])
	{
		MaryalaSGame app=new MaryalaSGame();
	}
}