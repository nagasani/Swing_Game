package mygame;

import java.awt.geom.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.io.*;
public class Block extends Rectangle.Double implements Paintable{

  private Color color;
  private Image image;
  private String fileName;
  private boolean hasImage;

  public Block(double x,double y, double h, double w, Color c){
		super(x,y,h,w);
		color = c;
		hasImage = false;
  }

	public Block(double x,double y, double h, double w, String fileName){
		super(x,y,h,w);
		this.fileName = fileName;
		 try {
				image = ImageIO.read(new File(fileName));
		 } catch (IOException ex) {
				System.out.println("Could not load image ["+fileName+"]");
		 }
		 hasImage = true;
  }

  public Color getColor() { return color; }

	public void setColor(Color newC) { color = newC; }

	public void setX(double x) { this.x = x; }

	public void setY(double y) { this.y = y; }

	public double getX() { return x; }

	public double getY() { return y; }

	public String getFileName(){ return fileName; }

	public void paint(Graphics2D g2d){
		if (!hasImage){
				g2d.setColor(color);
				g2d.fill(this);
		} else
			g2d.drawImage(image, (int)x, (int)y, (int)width,(int)height,null);
	}

}