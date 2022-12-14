import acm.graphics.GLabel;
import acm.graphics.GObject;
import acm.graphics.GOval;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * BREAKOUT GAME MAIN CLASS
 * @author Uka Enkhbold
 * @version 1.0.0
 */
public class BreakOutGame extends GraphicsProgram{

    private GOval ball = new GOval(WIDTH/2,HEIGHT/2, BALL_RADIUS, BALL_RADIUS);
    private double vy,vx;
    private boolean isPlaying = true;
    private GRect paddle = new GRect(PADDLE_WIDTH, PADDLE_HEIGHT);
    private GRect[] brick = new GRect[NROW * NBLOCKS_PER_ROW];
    private int removedBlocks = 0, ndie = 0, i;

    /**
     * Main run function
     */
    public void run () {
        addMouseListeners();
        setSize(WIDTH, HEIGHT);
        initGame();
        playGame();
        for (i = 0; i < NROW * NBLOCKS_PER_ROW; i++){
            if (brick[i] != null) remove(brick[i]);
        }
        remove(paddle);
        remove(ball);
        GLabel title = new GLabel("GAME OVER!!!");
        title.setColor(Color.red);
        title.setLocation(WIDTH/2, HEIGHT/2);
        if(removedBlocks>=NROW * NBLOCKS_PER_ROW){
            title.setLabel("YOU WIN!!!");
            title.setColor(Color.green);
        } else if (ndie >=NTURNS) title.setLabel("YOU LOST!!!");
        add(title);
    }

    /**
     * Mouse movement method along with the ball
     * @param e
     */
    public void mouseMove(MouseEvent e ){
        int x = e.getX();
        if( x > WIDTH-PADDLE_WIDTH)
            x = WIDTH-PADDLE_WIDTH;
        paddle.setLocation(x, paddle.getY());
    }

    /**
     * Prep to start the game
     */
    public void initGame (){
        initBlocks();
        initPaddle();
    }

    /**
     * Ball prep method
     */
    public void initBall(){
        RandomGenerator rgen = RandomGenerator.getInstance();
        ball.setLocation(WIDTH/2, HEIGHT/2);
        ball.setFilled(true);
        vx = rgen.nextDouble(1.0,2.0);
        vy = 3;
        if(rgen.nextBoolean(0.5)) vx= -vx;
        add(ball);
    }

    /**
     * Prep paddle method
     */
    public void initPaddle (){
        paddle.setFilled(true);
        add(paddle, WIDTH/2 - PADDLE_WIDTH/2, PADDLE_Y_OFFSET);
    }

    /**
     * colorful block prep method
     */
    public void initBlocks(){
        double dx,dy;
        Color[] color =  {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN,Color.CYAN};

        for (i= 0; i<NROW * NBLOCKS_PER_ROW; i++){
            brick [i] =new GRect(BLOCK_WIDTH, BLOCK_HEIGHT);
            dx = i%NBLOCKS_PER_ROW;
            dy = i /NROW;
            brick[i].setLocation(dx * BLOCK_WIDTH + dx *BLOCK_GAP, dy * BLOCK_HEIGHT + dy *BLOCK_GAP + BLOCK_Y_OFFSET);
            brick[i].setFilled(true);
            brick[i].setColor(color[i/NBLOCKS_PER_ROW]);
            add(brick[i]);
        }
    }

    /**
     * After starting method
     */

    public void playGame(){
        initBall();
        while(isPlaying){
            ballMove();
            GObject collider = getCollidingObject();
            for (i = 0; i < NROW * NBLOCKS_PER_ROW; i++) {
                if (collider == brick[i]) {
                    remove(brick[i]);
                    removedBlocks++;
                    vy = -vy;
                }
            }
            if (collider == paddle) vy = -vy;
            if (ball.getLocation().getY() > HEIGHT) {
                ndie++;
                if (ndie >= NTURNS) isPlaying = false;
                else {
                    initBall();
                    continue;
                }
            }
            if (ball.getLocation().getY() <= 0) vy = -vy;
            if (ball.getLocation().getX() <= 0 || ball.getLocation().getX() >= WIDTH) vx = -vx;
            pause(20);
            if (removedBlocks >= NROW * NBLOCKS_PER_ROW) isPlaying = false;

        }
    }

    /**
     * Ball movement method
     */
    public void ballMove() {
        ball.move(vx, vy);
    }

    /**
     * Finding bricks that collided with ball
     * @return brick that collided with ball
     */
    public GObject getCollidingObject(){
    GObject result = null;
    if(getElementAt(ball.getX(), ball.getY()) != null){
        result = getElementAt(ball.getX(), ball.getY());
    }
     return result;
    }

/** Window height and width */
    private static final int WIDTH = 400;
    private static final int HEIGHT = 600;
/**Paddle's height and width */
    private static final int PADDLE_WIDTH = 60;
    private static final int PADDLE_HEIGHT = 10;
/**Paddle coordinate Y */
    private static final int PADDLE_Y_OFFSET = 500;
/**Blocks per row */
    private static final int NBLOCKS_PER_ROW = 5;
/**Total number of all rows */
    private static final int NROW = 5;
/** Each block Gap */
    private static final int BLOCK_GAP = 2;
    /**Block width and height */

    private static final int BLOCK_WIDTH = (WIDTH - (NBLOCKS_PER_ROW -1) * BLOCK_GAP) /NBLOCKS_PER_ROW;
    private static final int BLOCK_HEIGHT = 20;
    /** Ball radius */
    private static final int BALL_RADIUS = 8;
    /** First block coordinates Y */
    private static final int BLOCK_Y_OFFSET  = 50;
    /** all lives or total turns number */
    private static final int NTURNS  = 3;
}
