package info.easyautomation.lwjglstudies;

import java.util.Random;

import org.lwjgl.opengl.GL11;

public class Box
{
	public static final int sides = 50;
	public int x;
	public int y;
	
	public final float r;
	public final float g;
	public final float b;
	
	private boolean isSelected = false;
	private int grabX;
	private int grabY;
	
	public Box(int x, int y)
	{
		this.x = Utils.clampInt(x, 0, TheGame.width - sides);
		this.y = Utils.clampInt(y, 0, TheGame.height - sides);
		
		Random rand = new Random();
		r = rand.nextInt(256) / 255f;
		g = rand.nextInt(256) / 255f;
		b = rand.nextInt(256) / 255f;
	}
	
	public void draw()
	{
		GL11.glColor3f(r, g, b);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2i(x, y);
		GL11.glVertex2i(x + sides, y);
		GL11.glVertex2i(x + sides, y + sides);
		GL11.glVertex2i(x, y + sides);
		GL11.glEnd();
		if(isSelected)
		{
			GL11.glColor3f(1f, 1f, 1f);
			GL11.glBegin(GL11.GL_LINE_LOOP);
			GL11.glVertex2i(x, y);
			GL11.glVertex2i(x + sides, y);
			GL11.glVertex2i(x + sides, y + sides);
			GL11.glVertex2i(x, y + sides);
			GL11.glEnd();
		}
	}
	
	public void setPosition(int x, int y)
	{
		this.x = Utils.clampInt(x - grabX, 0, TheGame.width - sides);
		this.y = Utils.clampInt(y - grabY, 0, TheGame.height - sides);
	}
	
	public void grab(int x, int y)
	{
		grabX = x - this.x;
		grabY = y - this.y;
		isSelected = true;
	}
	
	public void drop()
	{
		isSelected = false;
	}
	
	public boolean isInside(int x, int y)
	{
        return x > this.x && x < this.x + sides && y > this.y && y < this.y + sides;
	}
}
