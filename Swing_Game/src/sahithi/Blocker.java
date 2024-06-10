package sahithi;



import java.awt.geom.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.io.*;
public class Blocker extends Block implements Paintable{

	private Color color;
	private Image image;
	private boolean hasImage;

	public Blocker(double x, double y, double h, double w, Color c){
		super(x,y,h,w,c);
	}

	public void setColor(Color newC) { color = newC; }

	public void setX(double x) { this.x = x; }

	public void setY(double y) { this.y = y; }

	public double getX() { return x; }

	public double getY() { return y; }

}