import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
public class Breakout extends GraphicsProgram {

/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

/** Separation between bricks */
	private static final int BRICK_SEP = 4;

/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP ) / NBRICKS_PER_ROW;

/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

/** Number of turns */
	private static final int NTURNS = 3;
	private static final int DELAY = 11;
	private double vy = 3.00;
	private int numberOfBricks = NBRICKS_PER_ROW * NBRICKS_PER_ROW;
	private RandomGenerator rgen = RandomGenerator.getInstance();
	private double vx = rgen.nextDouble(1.0, 3.0);
	private int lives = NTURNS;
	private static final int firstX = (WIDTH - (BRICK_WIDTH * NBRICKS_PER_ROW + (NBRICKS_PER_ROW - 1) * BRICK_SEP)) /2;
	public void run() {
		/* You fill this in, along with any subsidiary methods */
		setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT);
		drawBricks();
		setup();
		waitForClick();
		moveBall();
		addMouseListeners();
	}

	//draw all bricks
	private void drawBricks() {
		int initialX = firstX;
		int initialY = BRICK_Y_OFFSET; 
		for(int j = 1; j <= NBRICK_ROWS; j++) {
			for(int i = 0; i < NBRICKS_PER_ROW; i++) {
				brick = new GRect(initialX, initialY, BRICK_WIDTH, BRICK_HEIGHT);
				add(brick);
				initialX += BRICK_WIDTH + BRICK_SEP;
				colorBricks(j);
			}
		initialX = firstX;
		initialY += BRICK_HEIGHT + BRICK_SEP;
		}
	}
	
	//set color to the bricks
	private void colorBricks(int j) {
		brick.setFilled(true);
		if(j <= 2) {
			brick.setFillColor(Color.RED);
			brick.setColor(Color.RED);
		}
		else 
		if(j > 2 && j <= 4) {
			brick.setFillColor(Color.ORANGE);
			brick.setColor(Color.ORANGE);
		}
		else 
		if(j > 4 && j <= 6) {
			brick.setFillColor(Color.YELLOW);
			brick.setColor(Color.YELLOW);
		}
		else 
		if(j > 6 && j <= 8) {
			brick.setFillColor(Color.GREEN);
			brick.setColor(Color.GREEN);
		}
		else 
		if(j > 8 && j <= 10) {
			brick.setFillColor(Color.CYAN);
			brick.setColor(Color.CYAN);
		}
	}
	
	//add paddle
	private void addPaddle() {
		int paddleX = WIDTH /2 - PADDLE_WIDTH /2;
		int paddleY = HEIGHT - PADDLE_Y_OFFSET;
		paddle = new GRect(paddleX, paddleY, PADDLE_WIDTH, PADDLE_HEIGHT);
		add(paddle);
		paddle.setFilled(true);
		paddle.setFillColor(Color.BLACK);
		addMouseListeners();
		
	}
	
	//add ball
	private void addBall() {
		int ballX = WIDTH / 2 - BALL_RADIUS;
		int ballY = HEIGHT / 2 - BALL_RADIUS;
		ball = new GOval(ballX, ballY, 2 * BALL_RADIUS, 2 * BALL_RADIUS);
		add(ball);
		ball.setFilled(true);
		ball.setFillColor(Color.BLACK);
	}

	
	//setup everything
	private void setup() {
		addPaddle();
		addBall();
	}
	
	//move the ball
	private void moveBall() {
		if (rgen.nextBoolean(0.5)) 
			vx = -vx;
		while(lives != 0) {
			ball.move(vx, vy);
			checkBorders();
			checkCollider();
			checkLives(lives);
			checkWin(numberOfBricks);
		pause(DELAY);
		}
	}
	
	//check if ball hits the borders
	private void checkBorders() {
		if(ball.getX() + 2 * BALL_RADIUS > WIDTH || ball.getX() < 0) {
			vx = -vx;
		}
		else
		if( ball.getY() < 0) {
			vy = -vy;
		}
		else
		if(ball.getY() + 2 * BALL_RADIUS > HEIGHT) {
			lives--;
			if(lives > 0)
			restart();
		}
	}
	
	//check if it is collider
	private void checkCollider() {
		 collider = getCollidingObject();
		 if(collider == paddle  && vy > 0) {
			if(ball.getY() + BALL_RADIUS > paddle.getY()) {
				vy = -vy;
				vx = -vx;
				bounceClip.play();
			} 
			else {
				bounceClip.play();
				vy = -vy;
			}
		}
		else if(collider != null) {
			remove(collider);
			vy = -vy;
			numberOfBricks--;
		}
		obj = null; 
	}
	
	//get the colliding object
	private GObject getCollidingObject() {
		if(getElementAt(ball.getX(), ball.getY()) != null &&
		   getElementAt(ball.getX(), ball.getY()) != livesBox) {
			obj = getElementAt(ball.getX(), ball.getY());
		}
		else if(getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY()) != null && 
				getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY()) != livesBox) {
			obj = getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY());
		}
		else if(getElementAt(ball.getX() , ball.getY() + 2 * BALL_RADIUS) != null &&
				getElementAt(ball.getX() , ball.getY() + 2 * BALL_RADIUS) != livesBox) {
			obj = getElementAt(ball.getX() , ball.getY() + 2 * BALL_RADIUS);
		}
		else if(getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY() + 2 * BALL_RADIUS) != null &&
				getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY() + 2 * BALL_RADIUS) != livesBox) {
			obj = getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY() + 2 * BALL_RADIUS);
		}
		return obj;
	}
	
	//restart program
	private void restart() {
		ball.setLocation(WIDTH / 2 - BALL_RADIUS, HEIGHT / 2 - BALL_RADIUS);
	}
	
	//check how many life is left
	private void checkLives(int lives) {
		if(lives == 0) {
			GLabel lost = new GLabel("Game Over");
			double lostWidth = lost.getWidth();
			double lostHeight = lost.getAscent();
			removeAll();
			add(lost, WIDTH / 2 - lostWidth / 2, HEIGHT / 2 - lostHeight / 2);
		}
	}
	
	//check if you have won
	private void checkWin(int numberOfBricks) {
		if(numberOfBricks == 0) {
			GLabel win = new GLabel("You Win");
			double winWidth = win.getWidth();
			double winHeight = win.getAscent();
			removeAll();
			add(win, WIDTH / 2 - winWidth / 2, HEIGHT / 2 - winHeight / 2);
		}
	}
	
	
	//move paddle when the mouse is moved
	public void mouseMoved(MouseEvent e) {
		if(PADDLE_WIDTH + e.getX() <= WIDTH)
			paddle.setLocation(e.getX(), paddle.getY());
	}

	AudioClip bounceClip = MediaTools.loadAudioClip("bounce.au");
	private GRect paddle;
	private GRect brick; 
	private GOval ball;
	private GObject obj = null;
	private GObject collider;
	private GLabel livesBox;
}