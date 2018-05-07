/*
Author: Khalid Asad
Date: January, 2010
Description: This program is a game of Duck Hunter where ducks are moving
	     all over the screen, and the goal is to kill as many as
	     possible by clicking over them.

Method List:

main
public duckhunt (class method)
windowClosing
mainClass (class method)
fileActions
findNames
mousePressed
mouseClicked (empty)
mouseEntered (empty)
mouseExited (empty)
Released (empty)
keyPressed
keyTyped (empty)
keyReleased (empty)
run
update
paint */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;

//-----------------------DuckHunt-----------------------------------------
// add frame (a pane)
public class DuckHunt extends Frame
{
    // create a class, run main method and create a class method
    public MainClass mainClass;
    public static void main (String[] args)
    {
	// create an object that can handle multiple classes
	DuckHunt duckHunt = new DuckHunt ();

    }


    public DuckHunt ()
    {
	// add try and catch statement so that mainClass method inside of MainClass
	// class can use throws IOException
	try
	{
	    mainClass = new MainClass ();
	}
	catch (Exception e)
	{
	}

	mainClass.setSize (672, 715);
	/* set size of frame, add mainClass in the center, add window
	listener to frame, set background of frame, set the cursor of
	the frame, and finally show the frame */
	setSize (660, 700);
	add ("Center", mainClass);
	addWindowListener (new WindowAction ());
	setLayout (new FlowLayout ());
	this.setBackground (new Color (950));
	setCursor (Cursor.getPredefinedCursor (Cursor.CROSSHAIR_CURSOR));
	show ();
    }
}
//----------------------------winExit--------------------------------------
class WindowAction extends WindowAdapter
{
    // if X on the window was pressed, confirm if the user would like to end
    // the game
    public void windowClosing (WindowEvent e)
    {
	int response = JOptionPane.showConfirmDialog (null,
		"Are you sure you want to end the game?",
		"End Game?", JOptionPane.YES_NO_OPTION);
	if (response == 0)
	{
	    System.exit (0);
	}

    }
}
//--------------------------MainClass--------------------------------------
class MainClass extends Canvas implements Runnable, MouseListener, KeyListener
{
    /* create variables for: thread(like timer), graphics, final image that cannot be
       changed once an image is chosen for it, toolkit to get pictures, co-ordinates
       of image, number to add on to co-ordinates, speed of thread, random number
       generator, booleans to confirm something in one method and use that
       confirmation to do something in another method. Static means that the variable
       can be used in static methods. */
    Thread t;
    Graphics offscreenGraphics;
    final Image background1, sprite1, sprite2, sprite3, sprite4, sprite5;
    Image sprite;
    Image offscreenImage;
    Toolkit toolkit = Toolkit.getDefaultToolkit ();
    int posX = 350, posY = 330;
    int mouseX, mouseY;
    int addValueX = 1, addValueY = 1;
    int chosenSpeed, speed = 1;
    int misses = 0, round = 1;
    int difficulty;
    int randomNumX, randomNumY;
    boolean duckClicked = false;
    boolean delay = false;
    static boolean search = false, sort = false, write = false, instructions = false;
    boolean waste = false;
    static Random randGen = new Random ();
    static String name[];
    static int scorerScore[];
    static String lines[];
    static String userName;
    static int score;
    Font f = new Font ("SansSerif", Font.PLAIN, 30);

    // main method of this class
    public MainClass () throws IOException
    {
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	  get background, sprites, add mouselistener and thread, also start
	  the thread */
	background1 = toolkit.getImage ("Images/background.jpg");
	sprite1 = toolkit.getImage ("Images/duckright.gif");
	sprite2 = toolkit.getImage ("Images/deadduck.gif");
	sprite3 = toolkit.getImage ("Images/shotduck.gif");
	sprite4 = toolkit.getImage ("Images/duckup.gif");
	sprite5 = toolkit.getImage ("Images/duckleft.gif");
	sprite = sprite1;
	addMouseListener (this);
	addKeyListener (this);
	t = new Thread (this, "MainClass");
	t.start ();

	//++++++++++++++++++++++++++++++++Set Difficulty of Game++++++++++++++
	// until user enters a number between 1 and 3 for difficulty
	while (difficulty < 1 || difficulty > 3)
	{
	    difficulty = Integer.parseInt (JOptionPane.showInputDialog
		    ("Enter difficulty from 1 (hardest) to 3 (easiest)", "2"));
	    
	    // fastest
	    if (difficulty == 1)
	    {
		chosenSpeed = 50;
	    }
	    // medium
	    else if (difficulty == 2)
	    {
		chosenSpeed = 75;
	    }
	    // slowest
	    else if (difficulty == 3)
	    {
		chosenSpeed = 125;
	    }
	}
	speed = chosenSpeed;

	//+++++++++++++++++++++++++++++++Ask Information+++++++++++++++++++++++
	userName = JOptionPane.showInputDialog
	    ("Enter a username longer than 2 characters", "tester");

	JOptionPane.showMessageDialog (null, "Press on application with mouse " +
		"and then type I  on the keyboard to check instructions!");

	//+++++++++++++++++++++++read high score, store info++++++++++++++++++++
	// declare readers so text file can be read and stored
	FileReader fileR = new FileReader ("highScores.txt");
	BufferedReader lineReader = new BufferedReader
	    (new FileReader ("highScores.txt"));
	BufferedReader output = new BufferedReader (fileR);

	int lineNumber;
	// count number of lines until lineReader cannot read anymore lines
	for (lineNumber = 0 ; lineReader.readLine () != null ;)
	{
	    lineNumber++;
	}

	/* divide by 2 so that 2 arrays can be used to read every 2 lines
	   ex.
	   Xeroy
	   999999 */
	lineNumber = lineNumber / 2;

	// create array sizes
	name = new String [lineNumber];
	scorerScore = new int [lineNumber];

	// loop to load information from lines into arrays
	for (int i = 0 ; i < name.length ; i++)
	{
	    name [i] = output.readLine ();
	    scorerScore [i] = Integer.parseInt (output.readLine ());
	}

	fileR.close ();
	// ++++++++++++++++++++++read instructions+++++++++++++++++++++++++++++
	FileReader fileR2 = new FileReader ("instructions.txt");
	BufferedReader lineReader2 = new BufferedReader
	    (new FileReader ("instructions.txt"));
	BufferedReader output2 = new BufferedReader (fileR2);

	int lineNumber2;
	// count number of lines until lineReader cannot read anymore lines
	for (lineNumber2 = 0 ; lineReader2.readLine () != null ;)
	{
	    lineNumber2++;
	}

	lines = new String [lineNumber2];

	// loop to load information from lines into arrays
	for (int i = 0 ; i < name.length ; i++)
	{
	    lines [i] = output2.readLine ();
	    System.out.println (lines [i]);
	}

	fileR2.close ();
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    }


    public static void fileActions (boolean check) throws IOException
    {
	//+++++++++++++++++++++++searching+++++++++++++++++++++++++++++++++
	if (search == true)
	{
	    // asks user to enter a name to search for
	    String searchName;
	    int position;
	    searchName = JOptionPane.showInputDialog ("Enter a name to search for",
		    "xeroy");

	    /* find position with using scorerName as input into findName
	       method and also searchName to find as input */
	    position = findName (name, searchName);

	    // if position was less than 0 in findName method
	    if (position < 0)
	    {
		JOptionPane.showMessageDialog (null, "Scorer not found");
	    }
	    else
	    {
		// output the current ceos statistics
		JOptionPane.showMessageDialog (null, name [position]
			+ " , " + scorerScore [position]);
	    }
	}

	//+++++++++++++++++++++++sorting+++++++++++++++++++++++++++++++++++
	else if (sort == true)
	{
	    // sort the arrays by highest to lowest score
	    for (int i = 0 ; i < name.length ; i++)
	    {
		//loop through to compare two elements next to each other
		for (int j = 0 ; j < name.length - 1 ; j++)
		{
		    //compare one element to the next
		    if (scorerScore [j] > scorerScore [j + 1])
		    {
			// swap scores if not in incresing order
			int tempScore = scorerScore [j];
			scorerScore [j] = scorerScore [j + 1];
			scorerScore [j + 1] = tempScore;
			// swap names to keep lists correct
			String tempName = name [j];
			name [j] = name [j + 1];
			name [j + 1] = tempName;
		    }
		}
	    }
	    JOptionPane.showMessageDialog (null, "Sorting Complete!");
	}

	//++++++++++++++++++++++++writing to file++++++++++++++++++++++++++
	else if (write == true)
	{
	    // edit file
	    FileWriter fileW = new FileWriter ("highScores.txt");
	    PrintWriter input = new PrintWriter (fileW);

	    // write to file
	    for (int i = 0 ; i < name.length ; i++)
	    {
		input.println (name [i]);
		input.println (scorerScore [i]);
	    }

	    input.println (userName);
	    input.println (score);

	    fileW.close ();

	    JOptionPane.showMessageDialog (null, "Message Written!");

	    // output message using system.out
	    for (int i = 0 ; i < name.length ; i++)
	    {
		System.out.println (name [i]);
		System.out.println (scorerScore [i]);
	    }
	    System.out.println (userName);
	    System.out.println (score);
	}

	// ++++++++++++++++++++++++view instructions+++++++++++++++++++++++++++++
	else if (instructions == true)
	{
	    for (int i = 0 ; i < name.length ; i++)
	    {
		System.out.println (lines [i]);
	    }
	}
    }


    // method to find a name that was searched for by the user
    public static int findName (String array[], String nameToFind)
    {
	int place = -1;

	// check through the array
	for (int i = 0 ; i < array.length ; i++)
	{
	    /* if name being searched for is inside the array save position
	       and end loop */
	    if (nameToFind.equalsIgnoreCase (array [i]) == true)
	    {
		place = i;
		break;
	    }
	}
	return place;
    }


    // if mouse is pressed on whatever is added to mouse listener
    public void mousePressed (MouseEvent m)
    {
	misses++;
	if (sprite == sprite1 || sprite == sprite4 || sprite == sprite5)
	{
	    // declare and find sprite's x value, y value, width value, and
	    // height value
	    final ImageIcon spriteIcon = new ImageIcon (sprite);
	    mouseX = m.getX ();
	    mouseY = m.getY ();
	    int spriteWidth = spriteIcon.getIconWidth ();
	    int spriteHeight = spriteIcon.getIconHeight ();

	    // if sprite was clicked, confirm delay, duckClicked, add to # of
	    // killed counter and reset miss counter
	    if (mouseX > posX && mouseX < posX + spriteWidth && mouseY > posY
		    && mouseY < posY + spriteHeight)
	    {
		delay = true;
		duckClicked = true;
		score = score + 100;
		misses = 0;
	    }
	}

	// if sprite was not clicked on 3 times
	if (misses > 2)
	{
	    misses = 0;
	    score = score - 100;
	}

	// change rounds
	if (score == 1000)
	{
	    round = 2;
	}
	else if (score == 2000)
	{
	    round = 3;
	}
	else if (score == 5000)
	{
	    round = 4;
	}
	else if (score == 10000)
	{
	    round = 5;
	}
	else if (score == 25000)
	{
	    round = 6;
	}
	else if (score == 50000)
	{
	    round = 7;
	}
	else if (score == 100000)
	{
	    round = 8;
	}
	else if (score == 250000)
	{
	    round = 9;
	}
	else if (score == 500000)
	{
	    round = 10;
	}
	// user wins, write their high socre
	else if (score == 1000000)
	{
	    round = 0;

	    // need a try catch constructor to run this
	    try
	    {
		fileActions (write);
	    }
	    catch (Exception e)
	    {
	    }

	    JOptionPane.showMessageDialog (null, "YOU WIN!! HIGH SCORE WRITTEN DOWN!");

	    System.exit (0);
	}
    }


    // 5 methods must be included in order for Mouse listener to work
    // ====================================== //
    public void mouseClicked (MouseEvent m)
    {
    }


    public void mouseEntered (MouseEvent m)
    {
    }


    public void mouseExited (MouseEvent m)
    {
    }


    public void mouseReleased (MouseEvent m)
    {
    }


    // ====================================== //

    // Handle the key-pressed event
    public void keyPressed (KeyEvent e)
    {
	/* if 1 is pressed on the keyboard, go through fileActions method and
	  searching high scores part will happen */
	if (e.getKeyCode () == KeyEvent.VK_1)
	{
	    search = true;

	    try
	    {
		fileActions (search);
	    }
	    catch (Exception x)
	    {
	    }

	    search = false;
	}
	// sort high scores
	else if (e.getKeyCode () == KeyEvent.VK_2)
	{
	    sort = true;

	    try
	    {
		fileActions (sort);
	    }
	    catch (Exception x)
	    {
	    }

	    sort = false;
	}
	// write high scores
	else if (e.getKeyCode () == KeyEvent.VK_3)
	{
	    write = true;

	    try
	    {
		fileActions (write);
	    }
	    catch (Exception x)
	    {
	    }

	    write = false;
	}
	// display instructions
	else if (e.getKeyCode () == KeyEvent.VK_I)
	{
	    instructions = true;

	    try
	    {
		fileActions (instructions);
	    }
	    catch (Exception x)
	    {
	    }

	    instructions = false;
	}
	// change speeds if q, w, or e are pressed
	else if (e.getKeyCode () == KeyEvent.VK_Q)
	{
	    chosenSpeed = 50;
	}
	else if (e.getKeyCode () == KeyEvent.VK_W)
	{
	    chosenSpeed = 75;
	}
	else if (e.getKeyCode () == KeyEvent.VK_E)
	{
	    chosenSpeed = 125;
	}
	speed = chosenSpeed;
    }


    // keyTyoed and keyReleased methods must also be here for keylistener
    public void keyTyped (KeyEvent e)
    {
    }


    public void keyReleased (KeyEvent e)
    {
    }


    // ====================================== //

    // run thread using this method and implements runnable
    public void run ()
    {
	// how many pixels will advance
	for (; score < 1000000 ;)
	{
	    // need try catch to be able to pause/slow down timer
	    try
	    {
		speed = chosenSpeed;
		randomNumY = randGen.nextInt (700) + 1;
		randomNumX = randGen.nextInt (660) + 1;

		// randomize movements of sprite
		// down
		if (randomNumY < 250 && randomNumY > 0)
		{
		    addValueY = -15;
		}
		// no movement up or down
		else if (randomNumY > 250 && randomNumY < 450)
		{
		    addValueY = 0;
		}
		// up
		else if (randomNumY > 450 && randomNumY < 660)
		{
		    addValueY = 15;
		}

		// left
		if (randomNumX < 250 && randomNumX > 0)
		{
		    addValueX = -15;
		}
		// no movement left or right
		else if (randomNumX > 250 && randomNumX < 450)
		{
		    addValueX = 0;
		}
		// right
		else if (randomNumX > 450 && randomNumX < 660)
		{
		    addValueX = 15;
		}

		// change images according to direction
		// duck goes up
		if (addValueY == 1 && addValueX == 0)
		{
		    sprite = sprite4;
		}
		// duck goes right
		else if (addValueX > 0)
		{
		    sprite = sprite1;
		}
		// duck goes left
		else if (addValueX < 0)
		{
		    sprite = sprite5;
		}

		if (duckClicked == true)
		{
		    /* when the duck is shot, setup a delay to run once and
		       show the shot duck image for 200 milleseconds then
		       change the speed back to normal */
		    if (delay == true)
		    {
			addValueX = 0;
			addValueY = 1;
			sprite = sprite3;
			t.sleep (200);
			delay = false;
			t.sleep (speed);
		    }
		    speed = 2;
		    sprite = sprite2;
		    addValueX = 0;
		    addValueY = 1;
		    /* if sprite is past bottom part of window, make movements
		       normal again, make sprite appear in random place, change
		       sprite. */
		    if (posY > 660)
		    {
			addValueX = 1;
			posY = randomNumY;
			posX = randomNumX;
			duckClicked = false;
			sprite = sprite1;
		    }
		}

		// if sprite is past bottom side of window, make it so it stays inside
		if (posY > 660)
		{
		    posY = posY - 20;
		    while (posY > 0)
		    {
			posY--;
		    }
		}
		// if sprite is past right side of window, make it so it stays inside
		else if (posX > 660)
		{
		    posX = posX - 20;
		    while (posX > 0)
		    {
			posX--;
		    }
		}
		// if sprite is past top side of window, make it so it stays inside
		else if (posY < 0)
		{
		    posY = posY + 20;
		    while (posY < 660)
		    {
			posY++;
		    }
		}
		// if sprite is past left side of window, make it so it stays inside
		else if (posX < 0)
		{
		    posX = posX + 20;
		    while (posX < 660)
		    {
			posX++;
		    }
		}

		// moves image
		posY = posY + addValueY;
		posX = posX + addValueX;
		// change movement speed of piture and restart this method
		t.sleep (speed);
		repaint ();
	    }
	    // need this to pause/slow down thread
	    catch (InterruptedException e)
	    {
	    }
	}
    }


    // =============================draw images============================

    // prevent flickering
    public void update (Graphics g)
    {
	paint (g);
    }


    public void paint (Graphics g)
    {
	if (offscreenImage == null)
	{
	    /* set up a double buffer (have everything get drawn on a
	     graphics variable, then draw that variable on another
	     graphics variable, the main one, g) */
	    offscreenImage = createImage (this.size ().width,
		    this.size ().height);
	    offscreenGraphics = offscreenImage.getGraphics ();
	}
	offscreenGraphics.setFont (f);
	// draw images and round information
	offscreenGraphics.drawImage (background1, 0, 0, this);
	offscreenGraphics.drawImage (sprite, posX, posY, this);
	offscreenGraphics.drawString ("Round: " + round, 30, 645);
	offscreenGraphics.drawString ("Score: " + score, 215, 645);
	offscreenGraphics.drawString ("No. of Misses: " + misses, 425, 645);
	g.drawImage (offscreenImage, 0, 0, this);
    }
}
//--------------------------end--------------------------------------------
