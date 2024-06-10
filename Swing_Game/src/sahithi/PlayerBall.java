package sahithi;



import java.awt.Color;

public class PlayerBall extends Ball{

	private int score;
	private boolean canMove;


	public PlayerBall(double x,double y, double s, Color c){
		super(x,y,s,c);
		score = 0;
		this.canMove = true;
	}

 	public PlayerBall(double x,double y, double s, String fileName){
		super(x,y,s,fileName);
		score = 0;
	}

	public int getScore()	{ return score; }

	public void addScore(int x)	{ score +=x; }


}

