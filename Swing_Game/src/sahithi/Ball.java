package sahithi;



import java.awt.geom.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.io.*;

public class Ball extends Ellipse2D.Double implements Moveable, Paintable {

  private Color color;
  private double dx, dy;
  private Image image;

  public Ball(double x,double y, double s, Color c){
		super(x,y,s,s);
		color = c;
  }

  public Ball(double x,double y, double s, String fileName){
		super(x,y,s,s);
		 try {
				image = ImageIO.read(new File(fileName));
		 } catch (IOException ex) {
				System.out.println("Could not load image ["+fileName+"]");
		 }
  }

  	public Color getColor() { return color; }

  	public void setColor(Color c) { color = c; }

	public void setX(double x) { this.x = x; }

	public void setY(double y) { this.y = y; }

	public void setDX(double dx) { this.dx = dx; }

	public void setDY(double dy) { this.dy = dy; }

	public void setSize(double s) { setFrame (x,y,s,s); }

	public double getX() { return x; }

	public double getY() { return y; }

	public double getDX() { return dx; }

	public double getDY() { return dy; }

	public double getSize() { return width; }

	public void fill(Graphics2D g2d){
		g2d.setColor(color);
		g2d.fill(this);
	}

	public void paint(Graphics2D g2d){
		
			g2d.setColor(color);
			g2d.fill(this);
		
			g2d.drawImage(image, (int)x, (int)y, (int)width,(int)height,null);
	}



	public void move(){
		x+=dx;
		y+=dy;
}

}