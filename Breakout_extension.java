import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import acmx.export.java.util.ArrayList;
import acmx.export.javax.swing.Timer;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
public class Breakout_extension extends GraphicsProgram {

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
	
	private static final int DELAY = 12;
	
	private double vy = 3.00;
	private int numberOfBricks = NBRICKS_PER_ROW * NBRICKS_PER_ROW;
	private RandomGenerator rgen = RandomGenerator.getInstance();
	private double vx = rgen.nextDouble(1.0, 3.0);
	private int lives = NTURNS;
	private static final int firstX = (WIDTH - (BRICK_WIDTH * NBRICKS_PER_ROW + (NBRICKS_PER_ROW - 1) * BRICK_SEP)) /2;
	private boolean finish = true;
	public void run() {
		/* You fill this in, along with any subsidiary methods */
		startProgram();
	}
	
	private void startProgram() {
		setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT);
		speed = DELAY;
		coins = 0;
		numberOfBricks = NBRICKS_PER_ROW * NBRICKS_PER_ROW;
		lives = NTURNS;
		drawBricks();
		setup();
		waitForClick();
		gameClip.play();
		moveBall();
		addMouseListeners();
	}
	
	//draw all bricks
	private void drawBricks() {
		int initialX = firstX;
		int initialY = BRICK_Y_OFFSET; 
		for(int j = 1; j <= NBRICK_ROWS; j++) {
			int hotnumber = rg.nextInt(NBRICKS_PER_ROW);
			for(int i = 0; i < NBRICKS_PER_ROW; i++) {
				brick = new GRect(initialX, initialY, BRICK_WIDTH, BRICK_HEIGHT); 
				add(brick);
				
				if(i == hotnumber){ //adding bricks which gain you coins
					brick.setFilled(true);
					brick.setFillColor(Color.PINK); 
					brick.setColor(Color.PINK);
				}
				colorBricks(j);
				initialX += BRICK_WIDTH + BRICK_SEP;
			}
		initialX = firstX;
		initialY += BRICK_HEIGHT + BRICK_SEP;
		}
	}
	
	//set color to the bricks
	private void colorBricks(int j) {
		brick.setFilled(true);
		if(j <= 2 && !(brick.getFillColor().equals(Color.PINK))) {
			brick.setFillColor(Color.RED);
			brick.setColor(Color.RED);
		}
		else 
		if(j > 2 && j <= 4 && !(brick.getFillColor().equals(Color.PINK))) {
			brick.setFillColor(Color.ORANGE);
			brick.setColor(Color.ORANGE);
		}
		else 
		if(j > 4 && j <= 6 && !(brick.getFillColor().equals(Color.PINK))) {
			brick.setFillColor(Color.YELLOW);
			brick.setColor(Color.YELLOW);
		}
		else 
		if(j > 6 && j <= 8 && !(brick.getFillColor().equals(Color.PINK))) {
			brick.setFillColor(Color.GREEN);
			brick.setColor(Color.GREEN);
		}
		else 
		if(j > 8 && j <= 10 && !(brick.getFillColor().equals(Color.PINK))) {
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
		if(modeClicked == false) { //if white mode is on
			paddle.setFillColor(Color.BLACK);
		}
		else { //if black mode is on
			paddle.setFillColor(Color.WHITE);
			paddle.setColor(Color.WHITE);
		}
		addMouseListeners();
	}
	
	//add ball
	private void addBall() {
		int ballX = WIDTH / 2 - BALL_RADIUS;
		int ballY = HEIGHT / 2 - BALL_RADIUS;
		ball = new GOval(ballX, ballY, 2 * BALL_RADIUS, 2 * BALL_RADIUS);
		add(ball);
		ball.setFilled(true);
		if(modeClicked == false) { //checking mode
			ball.setFillColor(Color.BLACK);
		}
		else {
			ball.setFillColor(Color.WHITE);
			ball.setColor(Color.WHITE);
		}
	}

	//set coins counter
	private void setCoins() {
		if(coinBox != null) {
				remove(coinBox);
		}
		coinBox = new GLabel("" + 10 * coins);
		//coinBox.setFont(new Font("Arial", Font.BOLD, (int) coinBox.getAscent()));
		double coinBoxWidth = coinBox.getWidth();
		double coinBoxHeight = coinBox.getAscent();
		if(10 * coins >= 10) {
			 add(coinBox, WIDTH - coinBoxWidth -  45, HEIGHT -1 ); 
		}
		else {
			add(coinBox, WIDTH - coinBoxWidth -  55, HEIGHT -1 );
		}
		coinBox.setColor(Color.YELLOW);
		addCoin();
	}
	
	
	//add coin image
	private void addCoin() {
		if(modeClicked == true) { //checking mode
			blackcoin();
		}
		else {
			Coin = new GImage("coin.png");
			add(Coin, WIDTH - 80, HEIGHT - coinBox.getAscent() - 1);
			Coin.setSize(15, 15);
			Coin.setColor(Color.WHITE);
		}
	}
	
	//add black coin image
	private void blackcoin() {
		blackCoin = new GImage("blackcoin.png");
		add(blackCoin, WIDTH - 80, HEIGHT - coinBox.getAscent() - 1);
		blackCoin.setSize(15, 15);
		blackCoin.setColor(Color.BLACK);
	}

	//set lives label
	private void setLives() {
		livesBox = new GLabel("" + lives);
		double livesBoxWidth = livesBox.getWidth();
		double livesBoxHeight = livesBox.getAscent();
		add(livesBox, WIDTH - livesBoxWidth - 5, HEIGHT );
		livesBox.setColor(Color.RED);
		livesBox.setFont(new Font("Arial", Font.BOLD, (int) livesBox.getAscent()));
		addHeart(livesBoxWidth, livesBoxHeight);		
	}
	
	//add heart image
	private void addHeart(double width, double height) {
		if(modeClicked == true ) { //checking mode
			blackheart();
		}
		else {
			image = new GImage("heart.png");
			add(image, WIDTH - width - 20, HEIGHT - height);
			image.setSize(15, 15);
			image.setColor(Color.WHITE);
		}
		
	}

	//add black heart
	private void blackheart() {
		blackHeart = new GImage("Blackheart.png");
		add(blackHeart, WIDTH - livesBox.getWidth() - 20, HEIGHT - livesBox.getAscent() - 1);
		blackHeart.setSize(15, 15);
		blackHeart.setColor(Color.BLACK);
	}

	//setup everything
	private void setup() {
		addPaddle();
		addBall();
		DarkMode();
		speedUp();
		setCoins();
		setLives();
		setCoins();
		pauseGame();
	}
	
	//move the ball
	private void moveBall() {
		if (rgen.nextBoolean(0.5)) 
			vx = -vx;
		while(finish) {
			if(isClicked == false){
				ball.move(vx, vy);
					if(modeClicked == false) {
						moveCoin(coinCollector);	
					}
					else {
						moveCoin(blackCoinCollector);
					}
				pause(speed);
			}
			checkBorders();
			checkCollider();
			checkLives(lives);
			checkWin(numberOfBricks);
			}
			addMouseListeners();
		}
	
	//move coin
	private void moveCoin(GImage coin) {
		for(int i = 0; i < myArrayList.size(); i++) { //create array of coins
			coin =(GImage) myArrayList.get(i);
			coin.move(cvx, cvy);
		if(getElementAt(coin.getX(), (coin.getY() + coin.getHeight())) == paddle || getElementAt(coin.getX(), coin.getY()) == paddle || 
		   getElementAt((coin.getX() + coin.getWidth()), (coin.getY() + coin.getHeight())) == paddle || 
		   getElementAt((coin.getX() + coin.getWidth()), coin.getY()) == paddle) {			
			remove(coinBox);
			coins++;
			collectCoinClip.play();
			remove(coin);
			myArrayList.remove(i);
			setCoins();
		}
		else if(coin.getY() + coin.getHeight() >= HEIGHT) {
			remove(coin);
			myArrayList.remove(i);
			}
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
		else if(ball.getY() + 2 * BALL_RADIUS > HEIGHT) {
			lives--;
			if(lives!=0) {
				loseHeartClip.play();
			}
			if(lives > 0 && numberOfBricks > 0) {
				remove(livesBox);
				setLives();
				restart();
			}
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
		else if(collider != null && collider != paddle) {
			if(collider.getColor().equals(Color.PINK)) {
				collectCoin();
			}
			remove(collider);
			vy = -vy;
			numberOfBricks--;
		}
		obj = null;
	}
	
	//collect coin
	private void collectCoin() {
		if(modeClicked == false) {
			coinCollector = new GImage("coin.png");
			coinCollector.setSize(15, 15);
			coinCollector.setColor(Color.WHITE);
			myArrayList.add(coinCollector);
			add(coinCollector, collider.getX() + collider.getWidth() / 2, collider.getY());
		}
		else {
			blackCoinCollector = new GImage("blackcoin.png");
			blackCoinCollector.setSize(15, 15);
			blackCoinCollector.setColor(Color.BLACK);
			myArrayList.add(blackCoinCollector);
			add(blackCoinCollector, collider.getX() + collider.getWidth() / 2, collider.getY());
		}
	}
	//get the colliding object
	private GObject getCollidingObject() {
		if(getElementAt(ball.getX(), ball.getY()) != null &&
		   getElementAt(ball.getX(), ball.getY()) != livesBox &&
		   getElementAt(ball.getX(), ball.getY()) != image &&
		   getElementAt(ball.getX(), ball.getY()) != blackHeart &&
		   getElementAt(ball.getX(), ball.getY()) != darkModeLabel &&
		   getElementAt(ball.getX(), ball.getY()) != whiteModeLabel &&
		   getElementAt(ball.getX(), ball.getY()) != blackCoin &&
		   getElementAt(ball.getX(), ball.getY()) != Coin &&
		   getElementAt(ball.getX(), ball.getY()) != coinBox &&
		   getElementAt(ball.getX(), ball.getY()) != pause &&
		   getElementAt(ball.getX(), ball.getY()) != coinCollector &&
		   getElementAt(ball.getX(), ball.getY()) != blackCoinCollector &&
		   getElementAt(ball.getX(), ball.getY()) != plusBox &&
		   getElementAt(ball.getX(), ball.getY()) != minusBox &&
		   getElementAt(ball.getX(), ball.getY()) != speedBox) {
			obj = getElementAt(ball.getX(), ball.getY());
		}
		else if(getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY()) != null && 
				getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY()) != livesBox  &&
				getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY()) != image &&
				getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY()) != blackHeart &&
				getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY()) != darkModeLabel &&
				getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY()) != whiteModeLabel &&
				getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY()) != pause &&
				getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY()) != Coin &&
				getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY()) != coinBox &&
				getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY()) != blackCoin &&
				getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY()) != coinCollector && 
				getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY()) != blackCoinCollector &&
				getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY()) != plusBox &&
				getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY()) != minusBox &&
				getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY()) != speedBox)  {
			obj = getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY());
		}
		else if(getElementAt(ball.getX() , ball.getY() + 2 * BALL_RADIUS) != null &&
				getElementAt(ball.getX() , ball.getY() + 2 * BALL_RADIUS) != livesBox &&
				getElementAt(ball.getX() , ball.getY() + 2 * BALL_RADIUS) != image &&
				getElementAt(ball.getX() , ball.getY() + 2 * BALL_RADIUS) != blackHeart &&
				getElementAt(ball.getX() , ball.getY() + 2 * BALL_RADIUS) != darkModeLabel &&
				getElementAt(ball.getX() , ball.getY() + 2 * BALL_RADIUS) != whiteModeLabel &&
				getElementAt(ball.getX() , ball.getY() + 2 * BALL_RADIUS) != pause && 
				getElementAt(ball.getX() , ball.getY() + 2 * BALL_RADIUS) != Coin &&
				getElementAt(ball.getX() , ball.getY() + 2 * BALL_RADIUS) != coinBox && 
				getElementAt(ball.getX() , ball.getY() + 2 * BALL_RADIUS) != blackCoin &&
				getElementAt(ball.getX() , ball.getY() + 2 * BALL_RADIUS) != coinCollector &&
				getElementAt(ball.getX() , ball.getY() + 2 * BALL_RADIUS) != blackCoinCollector &&
				getElementAt(ball.getX() , ball.getY() + 2 * BALL_RADIUS) != minusBox &&
				getElementAt(ball.getX() , ball.getY() + 2 * BALL_RADIUS) != plusBox &&
				getElementAt(ball.getX() , ball.getY() + 2 * BALL_RADIUS) != speedBox) {
			obj = getElementAt(ball.getX() , ball.getY() + 2 * BALL_RADIUS);
		}
		else if(getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY() + 2 * BALL_RADIUS) != null &&
				getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY() + 2 * BALL_RADIUS) != livesBox &&
				getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY() + 2 * BALL_RADIUS) != image && 
				getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY() + 2 * BALL_RADIUS) != blackHeart && 
				getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY() + 2 * BALL_RADIUS) != pause &&
				getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY() + 2 * BALL_RADIUS) != whiteModeLabel &&
				getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY() + 2 * BALL_RADIUS) != darkModeLabel &&
				getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY() + 2 * BALL_RADIUS) != coinBox &&
				getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY() + 2 * BALL_RADIUS) != Coin &&
				getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY() + 2 * BALL_RADIUS) != blackCoin &&
				getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY() + 2 * BALL_RADIUS) != coinCollector && 
				getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY() + 2 * BALL_RADIUS) != blackCoinCollector &&
				getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY() + 2 * BALL_RADIUS) != plusBox &&
				getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY() + 2 * BALL_RADIUS) != minusBox &&
				getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY() + 2 * BALL_RADIUS) != speedBox ) {
			obj = getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY() + 2 * BALL_RADIUS);
		}
		return obj;
		
		
	}
	
	//move paddle when the mouse is moved
	public void mouseMoved(MouseEvent e) {
		if(PADDLE_WIDTH + e.getX() <= WIDTH && isClicked != true)
			paddle.setLocation(e.getX(), paddle.getY());
	}
	
	//restart program
	private void restart() {
		ball.setLocation(WIDTH / 2 - BALL_RADIUS, HEIGHT / 2 - BALL_RADIUS);
	}
	
	//check how many life is left
	private void checkLives(int lives) {
		if(lives == 0 && numberOfBricks != 0) {
			removeAll();
			lost = new GLabel("Game Over");
			double lostWidth = lost.getWidth();
			double lostHeight = lost.getAscent();
			gameClip.stop();
			lostClip.play();
			add(lost, WIDTH / 2 - lostWidth / 2, HEIGHT / 2 - lostHeight / 2);
			if(modeClicked == false) { //checking mode
				lost.setColor(Color.BLACK);
				image2 = new GImage("heartbrokenwhite.png");
				add(image2, WIDTH / 2 + lostWidth / 2 + 5, HEIGHT / 2 - lostHeight - 5);
				image2.setSize(17, 17);
				image2.setColor(Color.WHITE);
			}
			else {
				GImage image3 = new GImage("heartbroken.png");
				add(image3, WIDTH / 2 + lostWidth / 2 + 5, HEIGHT / 2 - lostHeight - 5);
				image3.setSize(17, 17);
				image3.setColor(Color.WHITE);
				lost.setColor(Color.WHITE);
			}
			playAgain(); //play again
		}
	}
	
	//check if you have won
	private void checkWin(int numberOfBricks) {
		if(numberOfBricks == 0 && lives > 0) { 
			GLabel win = new GLabel("You Win");
			double winWidth = win.getWidth();
			double winHeight = win.getAscent();
			removeAll();
			gameClip.stop();
			add(win, WIDTH / 2 - winWidth / 2, HEIGHT / 2 - winHeight / 2);
			winClip.play();
			winningBackground();
			if(modeClicked == false) {
				win.setColor(Color.BLACK);
			}
			else {
				win.setColor(Color.WHITE);
			}
			finish = false;
		}
	}
	
	//play again
	private void playAgain() {
		restart = new GLabel("Click to Play Again");
		double restartWidth = restart.getWidth();
		double restartHeight = restart.getAscent();
		restart.setColor(Color.RED);
		add(restart, WIDTH / 2 - restartWidth / 2, HEIGHT - restartHeight);
		waitForClick();
		removeAll();
		startProgram();
	}
	
	//pause the game 
	private void pauseGame() {
		add(pause, pauseWidth /2, HEIGHT );
		if(modeClicked == false) {
			pause.setColor(Color.BLACK);
		}
		else {
			pause.setColor(Color.WHITE);
		}
	}
	
	//resume game
	private void resumeGame() {
		add(resume, resumeWidth/2, HEIGHT);
	}
	
	//add speed label
	private void speedUp() {
		add(speedBox, WIDTH - 1.5 * speedWidth, speedHeight);
		add(plusBox, WIDTH - 0.5 * speedWidth + 5, plusHeight);
		add(minusBox, speedBox.getX() - minusWidth - 5, minusHeight);
		if(modeClicked == false) {
			speedBox.setColor(Color.BLACK);
			plusBox.setColor(Color.BLACK);
			minusBox.setColor(Color.BLACK);
		}
		else {
			speedBox.setColor(Color.WHITE);
			plusBox.setColor(Color.WHITE);
			minusBox.setColor(Color.WHITE);
		}
	}
	
	//add dark mode label
	private void DarkMode() {
		if(modeClicked == false) {
		add(darkModeLabel, darkModeLabelWidth/2, darkModeLabelHeight);
		}
		else {
			WhiteMode();
		}	
	}
	
	//change to dark mode
	private void changeToDark() {
		setBackground(Color.BLACK);
		remove(image);
		blackheart();
		blackcoin();
		ball.setFilled(true);
		ball.setFillColor(Color.WHITE);
		ball.setColor(Color.WHITE);
		paddle.setFilled(true);
		paddle.setColor(Color.WHITE);
		paddle.setFillColor(Color.WHITE);
		pause.setColor(Color.WHITE);
		speedBox.setColor(Color.WHITE);
		resume.setColor(Color.WHITE);
		plusBox.setColor(Color.WHITE);
		minusBox.setColor(Color.WHITE);
	}
	
	//add white mode label
	private void WhiteMode() {
		whiteModeLabel.setColor(Color.WHITE);
		add(whiteModeLabel, whiteModeLabelWidth / 2, whiteModeLabelHeight);
		
	}
	
	//change to white mode
	private void changeToWhite() {
		remove(blackHeart);
		addCoin();
		add(image, WIDTH - livesBox.getWidth() - 20, HEIGHT - livesBox.getAscent());
		setBackground(Color.WHITE);
		ball.setFillColor(Color.BLACK);
		ball.setColor(Color.BLACK);
		paddle.setColor(Color.BLACK);
		paddle.setFillColor(Color.BLACK);
		pause.setColor(Color.BLACK);
		resume.setColor(Color.BLACK);
		speedBox.setColor(Color.BLACK);
		plusBox.setColor(Color.BLACK);
		minusBox.setColor(Color.BLACK);
	}
	
	public void mouseClicked(MouseEvent e) {
		if(e.getX() >=  pauseWidth /2 && e.getX() <= (1.5) * pauseWidth  && e.getY() >= HEIGHT - pauseHeight && isClicked == false) {
			isClicked = true;
			remove(pause);
			resumeGame();
		}
		else if(e.getX() >=  resumeWidth /2 && e.getX() <= (1.5) * resumeWidth  && e.getY() >= HEIGHT - resumeHeight && isClicked == true) {
			isClicked = false;
			remove(resume);
			pauseGame();
		}
		else if(e.getX() >=  darkModeLabelWidth /2 && e.getX() <= (1.5) * darkModeLabelWidth  && e.getY() <= darkModeLabelHeight && modeClicked == false) {
			modeClicked = true;
			changeToDark();
			remove(darkModeLabel);
			WhiteMode();
		}
		else if(e.getX() >=  whiteModeLabelWidth /2 && e.getX() <= (1.5) * whiteModeLabelWidth  && e.getY() <= whiteModeLabelHeight && modeClicked == true) {
			modeClicked = false;
			changeToWhite();
			remove(whiteModeLabel);
			add(darkModeLabel, darkModeLabelWidth/2, darkModeLabelHeight);
		}
		else if(e.getX() >=  plusBox.getX() && e.getX() <=  plusBox.getX() + plusWidth  && e.getY() <= plusHeight){
			speed /= 1.5;
		}
		else if(e.getX() >=  minusBox.getX() && e.getX() <=  minusBox.getX() + plusWidth  && e.getY() <= minusHeight){
			speed *= 1.5;
		}
		}
	
	//background for win
	private void winningBackground() {
		double height = HEIGHT;
		int x = 0;
		double y = Coin.getHeight();
		int number = (int) (WIDTH / Coin.getWidth());
		fillCoins();
		GImage c = (GImage) getElementAt(x, y);
		while(y + 16 * cvy <= height) {
			x = 0;
			for(int i = 0; i < number; i++) {
				if(getElementAt(x, y) != lost) {
				c = (GImage) getElementAt(x, y);
				c.move(0, 8 * cvy);
				pause(0.1);
				}
				x += Coin.getWidth();
			}
			y += 8 * cvy;
			if(y + 16 * cvy >= height) {
				height -= Coin.getWidth();
				fillCoins();
				y = Coin.getHeight();
			}
		}
	}
	
	//fill one line with coins
	private void fillCoins() {
		double x = 0;
		double y = Coin.getHeight();
		int number = (int) (WIDTH / Coin.getWidth());
		for(int i = 0; i < number; i++) {
			if(modeClicked == false) {
				coin1 = new GImage("coin.png");
			}
			else {
				coin1 = new GImage("blackcoin.png");
			}
			coin1.setSize(15, 15);
			add(coin1, x, y);
			x += Coin.getWidth();
		}
	}

	
	AudioClip collectCoinClip = MediaTools.loadAudioClip("collectcoin.au");
	AudioClip loseHeartClip = MediaTools.loadAudioClip("heartlose.au");
	AudioClip winClip = MediaTools.loadAudioClip("win.au");
	AudioClip bounceClip = MediaTools.loadAudioClip("bounce.au");
	AudioClip lostClip = MediaTools.loadAudioClip("lost.au");
	AudioClip gameClip = MediaTools.loadAudioClip("game.au");
	private GRect paddle;
	private GImage image;
	private GImage image2;
	private GRect brick; 
	private GOval ball;
	private GObject obj = null;
	private GObject collider;
	private GLabel restart;
	private GLabel lost;
	private GLabel pause = new GLabel("Pause");
	double pauseWidth = pause.getWidth();
	double pauseHeight = pause.getAscent();
	private boolean isClicked = false;
	private GLabel resume = new GLabel("Resume");
	double resumeWidth = resume.getWidth();
	double resumeHeight = resume.getAscent();
	private GLabel darkModeLabel = new GLabel("Dark mode");
	double darkModeLabelWidth = darkModeLabel.getWidth();
	double darkModeLabelHeight = darkModeLabel.getAscent();
	private GLabel whiteModeLabel = new GLabel("White mode");
	double whiteModeLabelWidth = whiteModeLabel.getWidth();
	double whiteModeLabelHeight = whiteModeLabel.getAscent();
	private boolean modeClicked = false; 
	private GImage blackHeart;
	private GLabel livesBox;
	private double speed;
	private RandomGenerator rg = new RandomGenerator();
	private GLabel coinBox;
	private int coins = 0;
	private GImage Coin;
	private GImage blackCoin;
	private double cvx = 0;
	private double cvy = 3.00;
	private GImage coinCollector ;
	private GImage blackCoinCollector;
	private GLabel plusBox = new GLabel("+");
	private double plusWidth = plusBox.getWidth();
	private double plusHeight = plusBox.getAscent();
	private GLabel speedBox = new GLabel("Speed 1.5x");
	private double speedWidth = speedBox.getWidth();
	private double speedHeight = speedBox.getAscent();
	private GLabel minusBox = new GLabel("-");
	private double minusWidth = minusBox.getWidth();
	private double minusHeight = minusBox.getAscent();
	private GImage coin1;
	private ArrayList myArrayList = new ArrayList();
}