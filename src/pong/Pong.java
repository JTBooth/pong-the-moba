package pong;

import client.GamePiece;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.util.Log;
import packets.SerializableColor;
import server.Player;
import server.PongServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Pong extends BasicGame {
    public static final double PTM_RATIO = 100.0;
    private static final int[] relevantKeys = {Keyboard.KEY_UP,
            Keyboard.KEY_DOWN, Keyboard.KEY_LEFT, Keyboard.KEY_RIGHT};

    public final int pixWidth = 800;
    public final int pixHeight = 600;
    float timeStep;
    int velocityIterations;
    int positionIterations;
    int lastStepTime;
    Keyboard keyboard;
    long frame;
    private String title;
    private List<SolidRect> rectRenderList;
    private List<Ball> ballRenderList;
    private GamePiece[] pieceArray;
    private Player player1;
    private Player player2;
    private World world;
    private AppGameContainer app;
    private SolidRect p1Paddle;
    private SolidRect p2Paddle;
    private Ball ball;
    private SolidRect topWall;
    private SolidRect botWall;
    private PongServer server;
    private Score score;

    public Pong(String title) throws IOException {
        super(title);

        this.title = title;
        rectRenderList = new ArrayList<SolidRect>();
        ballRenderList = new ArrayList<Ball>();
        pieceArray = new GamePiece[0];
        server = new PongServer(this, relevantKeys);

        Vec2 gravity = new Vec2(0, 0);
        world = new World(gravity);


        this.timeStep = 1f / 60f;
        this.velocityIterations = Settings.velocityIterations;
        this.positionIterations = Settings.positionIterations;
        frame = 0;
        
        this.score = new Score(Settings.winningScore);

        AppGameContainer app;
        try {
            app = new AppGameContainer(this);
            app.setDisplayMode((int) (Settings.windowSize[0]), (int) (Settings.windowSize[1]),
                    false);
            app.setVSync(true);
            app.setAlwaysRender(true);
            app.setTargetFrameRate(Settings.fps);

            app.start();
        } catch (SlickException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

        try {
            new Pong("pong");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static int m2p(float meter) {
        return (int) (meter * PTM_RATIO);
    }

    public static float p2m(int pixel) {
        return (float) (pixel / PTM_RATIO);
    }

    private void makeWalls() {
    	int width = (int) (Settings.windowSize[0]/PTM_RATIO);
    	int height = (int) (Settings.windowSize[1]/PTM_RATIO);
        botWall = new SolidRect(width/2, -0.01f, width, 0.005f, BodyType.KINEMATIC, getWorld(), this);
        botWall.getBody().getFixtureList().m_friction = 0;
        topWall = new SolidRect(width/2, (float) (height + 0.01), width, 0.005f, BodyType.KINEMATIC, getWorld(), this);
        topWall.getBody().getFixtureList().m_friction = 0;
    }

    @Override
    public boolean closeRequested() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void init(GameContainer arg0) throws SlickException {
    	int width = (int) (Settings.windowSize[0]/PTM_RATIO);
    	int height = (int) (Settings.windowSize[1]/PTM_RATIO);
        p1Paddle = new SolidRect(0.5f, height/2, 0.2f, Settings.paddleLength, BodyType.KINEMATIC, getWorld(), this);
        p2Paddle = new SolidRect(width - 0.5f, height/2, 0.2f, Settings.paddleLength, BodyType.KINEMATIC, getWorld(), this);
        makeWalls();

        ball = new Ball(width/2, height/2, Settings.ballRadius, getWorld(), this);
        ball.getBody().setLinearVelocity(new Vec2(-Settings.serveSpeed, 0));

        while (player1 == null || player2 == null) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            System.out.println("Waiting for players to load: " + (player1 == null) + " " + (player2 == null));
        }
    }

    @Override
    public void render(GameContainer arg0, Graphics graphics)
            throws SlickException {
        pieceArray = new GamePiece[rectRenderList.size() + ballRenderList.size()];
        int i = 0;
        for (SolidRect sr : rectRenderList) {

            float[] pts = sr.getPointsInPixels();
            Polygon poly = new Polygon(pts);
            pieceArray[i] = new GamePiece(poly, ShapeType.POLY, Settings.paddleColor);
            ++i;
        }

        for (Ball sb : ballRenderList) {
            pieceArray[i] = new GamePiece(new Circle(sb.getX(), sb.getY(), sb.getRadius()), ShapeType.POLY, Settings.ballColor);
            ++i;
        }
    }

    @Override
    public void update(GameContainer arg0, int arg1) throws SlickException {
        ++frame;

        int[] p1keys = player1.getKeys();
        int[] p2keys = player2.getKeys();

        step(p1keys, p2keys);
        
        if (ball.getX() < 0) {
        	score.playerScore(1, 1);
        	resetBall(1);
        } else if (ball.getX() > pixWidth) {
        	score.playerScore(0, 1);
        	resetBall(0);
        }
        
        server.sendUpdate(pieceArray, score.getScore());
    }

    private void resetBall(int i) {
		ball.setPosition((float) (pixWidth/(2*PTM_RATIO)), (float) (pixHeight/(2*PTM_RATIO)));
		System.out.println(ball.getX() + "x" + ball.getY());
		Vec2 ballVelocity;
		if (i == 0) {
			ballVelocity = new Vec2(-Settings.serveSpeed, 0);
		} else if (i == 1) {
			ballVelocity = new Vec2(Settings.serveSpeed,0);
		} else {
			Log.error("You served the ball to not a player: player # " + i + ". Players are 0 and 1.");
			throw new RuntimeException();
		}
		ball.getBody().setLinearVelocity(ballVelocity);

	}

	public void step(int[] p1keys, int[] p2keys) {
        execute(p1keys, player1);
        execute(p2keys, player2);

        world.step(timeStep, velocityIterations, positionIterations);

        int now = (int) System.nanoTime() / 1000000;
        lastStepTime = now;

    }

    public void execute(int[] keys, Player player) {
        float linearVelocity = 0;
        float turnRequest = 0;
        SolidRect paddle = player.getPaddle();
        for (int key : keys) {
            if (key == Keyboard.KEY_DOWN) {
                linearVelocity += Settings.paddleSpeed;
            }
            if (key == Keyboard.KEY_UP) {
                linearVelocity -= Settings.paddleSpeed;
            }
            if (key == Keyboard.KEY_RIGHT) {
                turnRequest += 1;
            }
            if (key == Keyboard.KEY_LEFT) {
                turnRequest -= 1;
            }
        }
        paddle.getBody().setLinearVelocity(new Vec2(0, linearVelocity));
        rubberBandRotation(turnRequest, paddle);
    }

    private void rubberBandRotation(float request, SolidRect paddle) {
        float currentAngle = paddle.getBody().getAngle();
        if (currentAngle > 180) {
            currentAngle = currentAngle - 360;
        }

        if (request == 0) {
            paddle.getBody().setAngularVelocity(-currentAngle * 4);
        } else {
            float rate;
            if (currentAngle * request > 0) {
                currentAngle = Math.abs(currentAngle);
                rate = Math.abs(Settings.maxPaddleRotateAngle - currentAngle) / Settings.maxPaddleRotateAngle;
            } else {
                currentAngle = Math.abs(currentAngle);
                rate = Math.abs(Settings.maxPaddleRotateAngle + currentAngle) / Settings.maxPaddleRotateAngle;
            }


            paddle.getBody().setAngularVelocity(request * rate);
        }

    }

    public void addSolidRect(SolidRect sr) {
        rectRenderList.add(sr);
    }

    public void removeSolidRect(SolidRect sr) {
        rectRenderList.remove(sr);
    }

    public void addSolidBall(Ball sb) {
        ballRenderList.add(sb);
    }

    public void removeSolidBall(Ball sb) {
        ballRenderList.remove(sb);
    }

    public void addPlayer(Player player) {
        if (player1 == null) {
            player1 = player;
            player1.setPaddle(p1Paddle);
        } else if (player2 == null) {
            player2 = player;
            player2.setPaddle(p2Paddle);
        } else {
            Log.info("No room for player " + player.getId());
            // No room for this player.
            // TODO send them an apology
        }


    }

    public Player getPlayer(long playerId) {
        if (player1.getId() == playerId) {
            return player1;
        } else if (player2.getId() == playerId) {
            return player2;
        } else {
            Log.info("No player with id: " + playerId);
            return null;
        }
    }

    public org.jbox2d.dynamics.World getWorld() {
        return world;
    }
    
    public int[] getScore() {
    	return score.getScore();
    }
}