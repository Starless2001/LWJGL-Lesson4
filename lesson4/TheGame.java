package info.easyautomation.lwjglstudies;

import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class TheGame
{
	public static final int width = 680;
	public static final int height = 480;
	private final ArrayList<Box> boxes = new ArrayList<>();
	private Box selectedBox;
	
	private TheGame()
	{
		createDisplay();
		initOpenGL();
		gameLoop();
		
	}
	
	private void initOpenGL()
	{
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, height, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glClearColor(173f / 255f, 216f / 255f, 230f / 255f, 255f);
		
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glLineWidth(3f);
		Keyboard.enableRepeatEvents(false);
	}
	
	private void createDisplay()
	{
		try
		{
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setTitle("Darkness");
			Display.create();
		}
		catch (LWJGLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void gameLoop()
	{
		while(!Display.isCloseRequested())
		{
			input();
			render();
			Display.update();
			Display.sync(60);
		}
		
		Display.destroy();
	}
	
	private int transformMouseY(int value)
	{
		return height - value - 1;
	}
	
	/**
	 * pressing 1 on the keyboard creates a new box at mouse location. (Mouse button 3 does the same)
	 * pressing the left mouse button inside a box grabs it
	 * to drag a box keep the left mouse button held down and move the mouse pointer across the screen
	 * pressing the right button over a box while not dragging any box deletes it 
	 */
	private void input()
	{
		if(Keyboard.next())
		{
			if(Keyboard.getEventKey() == Keyboard.KEY_1 && !Keyboard.getEventKeyState())
			{
				int mouseX = Mouse.getEventX();
				int mouseY = transformMouseY(Mouse.getEventY());
				
				boxes.add(new Box(mouseX, mouseY));
			}
		}
		if(Mouse.next())
		{
			int mouseX = Mouse.getEventX();
			int mouseY = transformMouseY(Mouse.getEventY());
			
			if(Mouse.getEventButton() == 0)//grabs a box if none is already grabbed
			{
				if(Mouse.getEventButtonState())
				{
					if(selectedBox == null)
					{
						for(Box box : boxes)
						{
							if(box.isInside(mouseX, mouseY))
							{
								box.grab(mouseX, mouseY);
								selectedBox = box;
								break;
							}
						}
					}
				}
				else//drops the box
				{
					if(selectedBox != null)
					{
						selectedBox.drop();
						selectedBox = null;
					}
				}
			}
			//moves the box if one is selected.
			else if(Mouse.getEventButton() == -1)//mouse moving, no button associated, but one or more buttons may be pressed
			{
				if(selectedBox != null)
				{
					selectedBox.setPosition(mouseX, mouseY);
				}
			}
			//deletes the box if right clicked upon
			else if(Mouse.getEventButton() == 1 && Mouse.getEventButtonState() && selectedBox == null)
			{
				for(Box box : boxes)
				{
					if(box.isInside(mouseX, mouseY))
					{
						selectedBox = box;
						break;
					}
				}
				boxes.remove(selectedBox);
				selectedBox = null;
			}
			//creates a new box at mouse position
			else if(Mouse.getEventButton() == 3 && Mouse.getEventButtonState() && selectedBox == null)
			{
				Box box = new Box(mouseX, mouseY);
				boxes.add(box);
			}
		}
	}
	private void render()
	{
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		for(Box box : boxes)
		{
			box.draw();
		}
	}
	
	public static void main(String args[])
	{
		@SuppressWarnings("unused")
		TheGame theGame = new TheGame();
	}
}
