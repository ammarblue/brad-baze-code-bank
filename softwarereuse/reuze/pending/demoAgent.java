package reuze.pending;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.Timer;

import javax.swing.AbstractAction;
import javax.swing.Action;

import com.software.reuze.aa_ActionDynamic;
import com.software.reuze.aa_AgentMock;
import com.software.reuze.aa_AgentProgramTableDriven;
import com.software.reuze.aa_a_Agent;
import com.software.reuze.aa_i_Action;
import com.software.reuze.aa_i_Percept;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

@SuppressWarnings("serial")
public class demoAgent extends PApplet
{
	public static final aa_i_Action JUMP_ACTION = new aa_ActionDynamic("jump");
	public static final aa_i_Action ATTACK_ACTION = new aa_ActionDynamic("attack");
	public static final aa_i_Percept OBSTACLE_PERCEPT = new Obstacle();
	public static final aa_i_Percept ENEMY_PERCEPT = new Enemy();
	public Map<List<aa_i_Percept>, aa_i_Action> perceptSequenceActions;
	
	// Map theoretical actions to actions defined in Player class
	public static Map<aa_i_Action,Integer> actionMap;
	// Map theoretical percepts to Enemy Sprite Class instantiations
	public static Map<PerceptSprite,aa_i_Percept> perceptMap;
	
	public static aa_i_Percept perceptList[];
	public static PerceptSprite perceptSpriteList[];
	public static int perceptCount = 0;
	// Starting positions for graphical percepts
	public static int perceptStartX;
	public static int perceptStartY;
	public static aa_a_Agent agent;
	PFont f;
	
	public static PlayerSprite zero;
	// World position of the player
	public static int playerx;
	public static int playery;
	public static EnemySprite enemy1;
	public static ObstacleSprite obstacle1;
	
	private void createTable()
	{
		perceptSequenceActions = new HashMap<List<aa_i_Percept>, aa_i_Action>(30);
		
		perceptSequenceActions.put(createPerceptSequence(OBSTACLE_PERCEPT), JUMP_ACTION);
		perceptSequenceActions.put(createPerceptSequence(ENEMY_PERCEPT), ATTACK_ACTION);
		
		perceptSequenceActions.put(createPerceptSequence(
				OBSTACLE_PERCEPT,
				OBSTACLE_PERCEPT), JUMP_ACTION);
		perceptSequenceActions.put(createPerceptSequence(
				OBSTACLE_PERCEPT,
				ENEMY_PERCEPT), ATTACK_ACTION);
		perceptSequenceActions.put(createPerceptSequence(
				ENEMY_PERCEPT,
				OBSTACLE_PERCEPT), JUMP_ACTION);
		perceptSequenceActions.put(createPerceptSequence(
				ENEMY_PERCEPT,
				ENEMY_PERCEPT), ATTACK_ACTION);
		
		perceptSequenceActions.put(createPerceptSequence(
				OBSTACLE_PERCEPT,
				OBSTACLE_PERCEPT, 
				OBSTACLE_PERCEPT), JUMP_ACTION);
		perceptSequenceActions.put(createPerceptSequence(
				OBSTACLE_PERCEPT,
				OBSTACLE_PERCEPT, 
				ENEMY_PERCEPT), ATTACK_ACTION);
		perceptSequenceActions.put(createPerceptSequence(
				OBSTACLE_PERCEPT,
				ENEMY_PERCEPT, 
				OBSTACLE_PERCEPT), JUMP_ACTION);
		perceptSequenceActions.put(createPerceptSequence(
				OBSTACLE_PERCEPT,
				ENEMY_PERCEPT, 
				ENEMY_PERCEPT), ATTACK_ACTION);
		perceptSequenceActions.put(createPerceptSequence(
				ENEMY_PERCEPT,
				OBSTACLE_PERCEPT, 
				OBSTACLE_PERCEPT), JUMP_ACTION);
		perceptSequenceActions.put(createPerceptSequence(
				ENEMY_PERCEPT,
				OBSTACLE_PERCEPT, 
				ENEMY_PERCEPT), ATTACK_ACTION);
		perceptSequenceActions.put(createPerceptSequence(
				ENEMY_PERCEPT,
				ENEMY_PERCEPT, 
				OBSTACLE_PERCEPT), JUMP_ACTION);
		perceptSequenceActions.put(createPerceptSequence(
				ENEMY_PERCEPT,
				ENEMY_PERCEPT, 
				ENEMY_PERCEPT), ATTACK_ACTION);
		
		perceptSequenceActions.put(createPerceptSequence(
				OBSTACLE_PERCEPT,
				OBSTACLE_PERCEPT,
				OBSTACLE_PERCEPT, 
				OBSTACLE_PERCEPT), JUMP_ACTION);
		perceptSequenceActions.put(createPerceptSequence(
				OBSTACLE_PERCEPT,
				OBSTACLE_PERCEPT,
				OBSTACLE_PERCEPT, 
				ENEMY_PERCEPT), ATTACK_ACTION);
		perceptSequenceActions.put(createPerceptSequence(
				OBSTACLE_PERCEPT,
				OBSTACLE_PERCEPT,
				ENEMY_PERCEPT, 
				OBSTACLE_PERCEPT), JUMP_ACTION);
		perceptSequenceActions.put(createPerceptSequence(
				OBSTACLE_PERCEPT,
				OBSTACLE_PERCEPT,
				ENEMY_PERCEPT, 
				ENEMY_PERCEPT), ATTACK_ACTION);
		perceptSequenceActions.put(createPerceptSequence(
				OBSTACLE_PERCEPT,
				ENEMY_PERCEPT,
				OBSTACLE_PERCEPT, 
				OBSTACLE_PERCEPT), JUMP_ACTION);
		perceptSequenceActions.put(createPerceptSequence(
				OBSTACLE_PERCEPT,
				ENEMY_PERCEPT,
				OBSTACLE_PERCEPT, 
				ENEMY_PERCEPT), ATTACK_ACTION);
		perceptSequenceActions.put(createPerceptSequence(
				OBSTACLE_PERCEPT,
				ENEMY_PERCEPT,
				ENEMY_PERCEPT, 
				OBSTACLE_PERCEPT), JUMP_ACTION);
		perceptSequenceActions.put(createPerceptSequence(
				OBSTACLE_PERCEPT,
				ENEMY_PERCEPT,
				ENEMY_PERCEPT, 
				ENEMY_PERCEPT), ATTACK_ACTION);
		
		perceptSequenceActions.put(createPerceptSequence(
				ENEMY_PERCEPT,
				OBSTACLE_PERCEPT,
				OBSTACLE_PERCEPT, 
				OBSTACLE_PERCEPT), JUMP_ACTION);
		perceptSequenceActions.put(createPerceptSequence(
				ENEMY_PERCEPT,
				OBSTACLE_PERCEPT,
				OBSTACLE_PERCEPT, 
				ENEMY_PERCEPT), ATTACK_ACTION);
		perceptSequenceActions.put(createPerceptSequence(
				ENEMY_PERCEPT,
				OBSTACLE_PERCEPT,
				ENEMY_PERCEPT, 
				OBSTACLE_PERCEPT), JUMP_ACTION);
		perceptSequenceActions.put(createPerceptSequence(
				ENEMY_PERCEPT,
				OBSTACLE_PERCEPT,
				ENEMY_PERCEPT, 
				ENEMY_PERCEPT), ATTACK_ACTION);
		perceptSequenceActions.put(createPerceptSequence(
				ENEMY_PERCEPT,
				ENEMY_PERCEPT,
				OBSTACLE_PERCEPT, 
				OBSTACLE_PERCEPT), JUMP_ACTION);
		perceptSequenceActions.put(createPerceptSequence(
				ENEMY_PERCEPT,
				ENEMY_PERCEPT,
				OBSTACLE_PERCEPT, 
				ENEMY_PERCEPT), ATTACK_ACTION);
		perceptSequenceActions.put(createPerceptSequence(
				ENEMY_PERCEPT,
				ENEMY_PERCEPT,
				ENEMY_PERCEPT, 
				OBSTACLE_PERCEPT), JUMP_ACTION);
		perceptSequenceActions.put(createPerceptSequence(
				ENEMY_PERCEPT,
				ENEMY_PERCEPT,
				ENEMY_PERCEPT, 
				ENEMY_PERCEPT), ATTACK_ACTION);
	}
	Action generatePercept = new AbstractAction()
	{
	    public void actionPerformed(ActionEvent e)
	    {
			if (perceptCount < perceptList.length)
			{
				perceptSpriteList[perceptCount++].currentState = PerceptSprite.RUNNING;
			}
	    }
	};
	public void setup()
	{
		size(400,500);
		frameRate(12);
		smooth();
		f = createFont("Consolas",12,true);
		
		createTable();
		
		String imageFile = "./data/Zero.png";
		PImage img = loadImage(imageFile);
		zero = new PlayerSprite(img, 31, 5, 90, 70);
		
		// Create a hard-coded environment
		// If you want to change the order of percepts, this is the first place
		// you have to make changes. The program only supports up to four percepts
		// and these three sections must have the same three percept type order
		perceptList = new aa_i_Percept[6];
		perceptList[0] = OBSTACLE_PERCEPT;
		perceptList[1] = ENEMY_PERCEPT;
		perceptList[2] = OBSTACLE_PERCEPT;
		perceptList[3] = OBSTACLE_PERCEPT;
		perceptList[4] = ENEMY_PERCEPT;
		perceptList[5] = ENEMY_PERCEPT;
		
		// This is the second section to make changes in.
		perceptSpriteList = new PerceptSprite[6];
		
		img = loadImage("./data/Obstacle1.png");
		perceptSpriteList[0] = new ObstacleSprite(img, 3, 3, 90, 70);
		
		img = loadImage("./data/Enemy2.png");
		perceptSpriteList[1] = new EnemySprite(img, 10, 4, 90, 70);
		perceptSpriteList[4] = new EnemySprite(img, 10, 4, 90, 70);
		perceptSpriteList[5] = new EnemySprite(img, 10, 4, 90, 70);
		
		img = loadImage("./data/Obstacle1.png");
		perceptSpriteList[2] = new ObstacleSprite(img, 3, 3, 90, 70);
		
		//img = loadImage("./data/Obstacle1.png");
		perceptSpriteList[3] = new ObstacleSprite(img, 3, 3, 90, 70);

		// This is the third section to make changes in the percepts
		perceptMap = new HashMap<PerceptSprite,aa_i_Percept>(6);
		perceptMap.put(perceptSpriteList[0], OBSTACLE_PERCEPT);
		perceptMap.put(perceptSpriteList[1], ENEMY_PERCEPT);
		perceptMap.put(perceptSpriteList[2], OBSTACLE_PERCEPT);
		perceptMap.put(perceptSpriteList[3], OBSTACLE_PERCEPT);
		perceptMap.put(perceptSpriteList[4], ENEMY_PERCEPT);
		perceptMap.put(perceptSpriteList[5], ENEMY_PERCEPT);

		agent = new aa_AgentMock(new aa_AgentProgramTableDriven(
				perceptSequenceActions));
		
//		imageFile = "../sprites/Enemy2.png";
//		img = loadImage(imageFile);
//		enemy1 = new EnemySprite(img, 10, 4, 90, 70);
//		
//		imageFile = "../sprites/Obstacle1.png";
//		img = loadImage(imageFile);
//		obstacle1 = new ObstacleSprite(img, 3, 3, 90, 70);
		
		actionMap = new HashMap<aa_i_Action,Integer>(2);
		actionMap.put(JUMP_ACTION, zero.JUMPING);
		actionMap.put(ATTACK_ACTION, zero.ATTACKING);
		
		Timer timer;
		timer = new Timer(3000, generatePercept);
		timer.start();
	}
	
	public void draw()
	{
		background(255);
		
		rectMode(CORNERS);
		fill(255,0,0);
		rect(0,400,400,420);
		
		textFont(f);
		fill(0,0,0);
		textAlign(LEFT);

		text("Percept Sequence",0,10); text("Action",120,10);
		text("O",         0,20);  text("J",120,20);
		text("O, O",      0,30);  text("J",120,30);
		text("O, E",      0,40);  text("A",120,40);
		text("O, O, O",   0,50);  text("J",120,50);
		text("O, O, E",   0,60);  text("A",120,60);
		text("O, E, O",   0,70);  text("J",120,70);
		text("O, E, E",   0,80);  text("A",120,80);
		text("E, O, O",   0,90);  text("J",120,90);
		text("E, O, E",   0,100); text("A",120,100);
		text("E, E, O",   0,110); text("J",120,110);
		text("E, E, E",   0,120); text("A",120,120);
		text("O, O, O, O",0,130); text("J",120,130);
		text("O, O, O, E",0,140); text("A",120,140);
		text("O, O, E, O",0,150); text("J",120,150);
		text("O, O, E, E",0,160); text("A",120,160);
		text("O, E, O, O",0,170); text("J",120,170);
		text("O, E, O, E",0,180); text("A",120,180);
		text("O, E, E, O",0,190); text("J",120,190);
		text("O, E, E, E",0,200); text("A",120,200);
		text("E, O, O, O",0,210); text("J",120,210);
		text("E, O, O, E",0,220); text("A",120,220);
		text("E, O, E, O",0,230); text("J",120,230);
		text("E, O, E, E",0,240); text("A",120,240);
		text("E, E, O, O",0,250); text("J",120,250);
		text("E, E, O, E",0,260); text("A",120,260);
		text("E, E, E, O",0,270); text("J",120,270);
		text("E, E, E, E",0,280); text("A",120,280);
		
		playerx = 140 + zero.x;
		playery = 330 + zero.y;
		image(zero.getPImage(zero.currentState),playerx,playery);
		perceptStartX = 340;
		perceptStartY = 330;
		
		for (int i = 0; i < perceptList.length; i++)
		{
			if (perceptSpriteList[i].currentState != PerceptSprite.GONE)
				image(perceptSpriteList[i].getPImage(perceptSpriteList[i].currentState),perceptStartX + perceptSpriteList[i].x,perceptStartY + perceptSpriteList[i].y);
		}
		//image(obstacle1.getPImage(obstacle1.currentState),perceptStartX + obstacle1.x,perceptStartY + obstacle1.y);
		
		text("player x: " + playerx, 200, 20);
		text("player state: " + zero.currentState, 200, 30);
		text("Obstacle 1 x: " + perceptSpriteList[0].x,200,40);
	}
	
	/**
	 * I copied this method from TableDrivenAgentProgramTest.
	 * @param percepts
	 * @return
	 */
	private static List<aa_i_Percept> createPerceptSequence(aa_i_Percept... percepts) {
		List<aa_i_Percept> perceptSequence = new ArrayList<aa_i_Percept>();

		for (aa_i_Percept p : percepts) {
			perceptSequence.add(p);
		}

		return perceptSequence;
	}
}
class Obstacle implements aa_i_Percept
{
	
}
class Enemy implements aa_i_Percept
{
	
}
class PlayerSprite extends AnimatedSprite
{
	public final int RUNNING = 0;
	public final int JUMPING = 11;
	public final int ATTACKING = 23;
	static Map<Integer,Integer> stateMap;
	
	public PlayerSprite(PImage img, int numOfFrames, int columns, int w, int h)
	{
		super(img, numOfFrames, columns, w, h);
		stateMap = new HashMap<Integer,Integer>(31);
		int x;
		int y;
		currentState = RUNNING;
		
		// Create images/frames from sprite sheet
		for (int i = 0; i < numOfFrames; i++)
		{
			x = (i % columns) * w;
			y = (i / columns) * h;
			frames[i] = img.get(x, y, w, h);
			
			if ((i >= 0) && (i <= 10))
			{
				stateMap.put(i, RUNNING);
			}
			else if ((i >= 11) && (i <= 22))
			{
				stateMap.put(i, JUMPING);
			}
			else if ((i >= 23) && (i <= 30))
			{
				stateMap.put(i, ATTACKING);
			}
		}
	}
	@Override
	public PImage getPImage(int state)
	{
		// Skip to the first occurrence of the desired frame set
		if (stateMap.get(frameNum) != state)
			frameNum = state;
		
		if (stateMap.get(frameNum) == JUMPING)
		{
			if (jumpState == 0)
			{
				jumpState = asc;
			}
			if (jumpState == asc)
			{
				if (frameNum - JUMPING >= 5)
				{
					jumpState = desc;
				}
				else
					y -= jumpHeight;
			}
			else if (jumpState == desc)
			{
				if (frameNum + 1>= ATTACKING)
				{
					jumpState = 0;
					currentState = RUNNING;
					frameNum = RUNNING;
					return frames[frameNum];
				}
				else
					y += jumpHeight;
			}
		}
		else if (stateMap.get(frameNum) == ATTACKING)
		{
			if (frameNum + 1 >= numOfFrames)
			{
				currentState = RUNNING;
				frameNum = RUNNING;
				return frames[frameNum];
			}
		}
		
		return frames[frameNum++];
	}
	public void execute(PerceptSprite perceptSprite)
	{
		// Map the enemy to a percept type
		aa_i_Percept percept = demoAgent.perceptMap.get(perceptSprite);
		// Execute an action based on the percept
		aa_i_Action act = demoAgent.agent.execute(percept);
		
		if(act == demoAgent.JUMP_ACTION)
			currentState = JUMPING;
		else if(act == demoAgent.ATTACK_ACTION)
		{
			currentState = ATTACKING;
			perceptSprite.currentState = EnemySprite.EXPLODING;
		}
		//System.out.println("Action Recognized");
	}
}
class EnemySprite extends PerceptSprite
{
	public static int RUNNING = 0;
	public static int EXPLODING = 4;
	public Map<Integer,Integer> stateMap;
	
	public EnemySprite(PImage img, int numOfFrames, int columns, int w, int h)
	{
		super(img, numOfFrames, columns, w, h);
		stateMap = new HashMap<Integer,Integer>(9);
		speed = 5;
		currentState = GONE;
		
		int x;
		int y;
		
		// Create images/frames from sprite sheet
		for (int i = 0; i < numOfFrames; i++)
		{
			x = (i % columns) * w;
			y = (i / columns) * h;
			frames[i] = img.get(x, y, w, h);
			
			if ((i >= 0) && (i <= 3))
			{
				stateMap.put(i, RUNNING);
			}
			else if ((i >= 4) && (i <= 9))
			{
				stateMap.put(i, EXPLODING);
			}
		}
	}
	@Override
	public PImage getPImage(int state)
	{
		if (frameNum >= numOfFrames)
			frameNum = state;
		// Skip to the first occurrence of the desired frame set
		if (stateMap.get(frameNum) != state)
			frameNum = state;
		
		if (stateMap.get(frameNum) == RUNNING)
		{
			if (frameNum + 1 >= numOfFrames)
			{
				frameNum = RUNNING;
				return frames[frameNum];
			}
			else
			{
				x -= speed;
			}
		}
		else if (stateMap.get(frameNum) == EXPLODING)
		{
			if (frameNum + 1 >= numOfFrames)
			{
				currentState = GONE;
				frameNum = GONE;
				return frames[9];
			}
		}
		
		checkLocation();
		return frames[frameNum++];
	}
}
class ObstacleSprite extends PerceptSprite
{
	public Map<Integer,Integer> stateMap;
	
	public ObstacleSprite(PImage img, int numOfFrames, int columns, int w, int h)
	{
		super(img, numOfFrames, columns, w, h);
		stateMap = new HashMap<Integer,Integer>(9);
		speed = 6;
		currentState = GONE;
		
		int x;
		int y;
		
		// Create images/frames from sprite sheet
		for (int i = 0; i < numOfFrames; i++)
		{
			x = (i % columns) * w;
			y = (i / columns) * h;
			frames[i] = img.get(x, y, w, h);
			
			if ((i >= 0) && (i <= 2))
			{
				stateMap.put(i, RUNNING);
			}
		}
	}
	@Override
	public PImage getPImage(int state)
	{
		if (frameNum >= numOfFrames)
		{
			frameNum = state;
		}
		// Skip to the first occurrence of the desired frame set
		if (stateMap.get(frameNum) != state)
			frameNum = state;
		
		if (stateMap.get(frameNum) ==  RUNNING)
		{
			x -= speed;
		}
		checkLocation();
		
		if ((x + 90 + demoAgent.perceptStartX) < 0)
		{
			currentState = GONE;
		}
		return frames[frameNum++];
	}
}
abstract class PerceptSprite extends AnimatedSprite
{
	public int speed;
	public static int GONE = -1;
	public static int RUNNING = 0;
	public int executed;
	
	public void checkLocation()
	{
		// This is the actual location of the percept. Relative + Starting
		int worldX = x + demoAgent.perceptStartX;
		
		if (executed == 0)
		// If this percept is too close to the agent...
		if (worldX - demoAgent.playerx <= 40)
		{
			demoAgent.zero.execute(this);
			executed = 1;
		}
	}
	public PerceptSprite(PImage img, int numOfFrames, int columns, int w, int h)
	{
		super(img, numOfFrames, columns, w, h);
		executed = 0;
	}
}
abstract class AnimatedSprite
{
	PImage frames[];
	// Number of columns on the sprite sheet
	int columns;
	// Current frame number / array index
	int frameNum;
	int numOfFrames;
	// Sprite relative positions
	int x, y;
	public int currentState;
	
	int jumpState = 0;
	int jumpHeight = 8;
	int asc = 1;
	int desc = 2;
	
	public AnimatedSprite(PImage img, int numOfFrames, int columns, int w, int h)
	{
		this.frames = new PImage[numOfFrames];
		this.columns = columns;
		this.frameNum = 0;
		this.numOfFrames = numOfFrames;
	}
	
	abstract public PImage getPImage(int state);
}