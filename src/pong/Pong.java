package pong;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import packets.GamePiece;
import server.Player;
import server.PongServer;


public class Pong extends BasicGame {
    public static final double PTM_RATIO = 100.0;
    private static final int[] relevantChars = Settings.relevantChars;
    float timeStep;
    int velocityIterations;
    int positionIterations;
    int lastStepTime;
    long frame;

    private String title;
    private List<SolidRect> rectRenderList;
    private List<Ball> ballRenderList;
    private GamePiece[] pieceArray;
    private Player player1;
    private Player player2;
    private World world;
    private Spellkeeper spellkeeper;
    private SolidRect p1Paddle;
    private SolidRect p2Paddle;
    private Ball ball;
    private PongServer server;
    private Scorekeeper score;

    public Pong(String title) throws IOException {
        super(title);

        this.title = title;
        rectRenderList = new ArrayList<SolidRect>();
        ballRenderList = new ArrayList<Ball>();
        pieceArray = new GamePiece[0];
        server = new PongServer(this, relevantChars);

        Vec2 gravity = new Vec2(0, 0);
        world = new World(gravity);


        this.timeStep = 1f / 60f;
        this.velocityIterations = Settings.velocityIterations;
        this.positionIterations = Settings.positionIterations;
        frame = 0;

        this.score = new Scorekeeper(Settings.winningScore);

        AppGameContainer app;
        try {
            app = new AppGameContainer(this);
            app.setDisplayMode(Settings.windowSize[0], Settings.windowSize[1],
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

    @Override
    public boolean closeRequested() {
        return true;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void init(GameContainer arg0) throws SlickException {
        int width = (int) (Settings.windowSize[0] / PTM_RATIO);
        int height = (int) (Settings.windowSize[1] / PTM_RATIO);

        makePaddles(height, width);
        makeWalls();
        makeBall(height, width);

        while (player1 == null || player2 == null) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignored) {}
            System.out.println("Waiting for players to load: " + (player1 == null) + " " + (player2 == null));
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
        } else if (ball.getX() > Settings.windowSize[0]) {
            score.playerScore(0, 1);
            resetBall(0);
        }

        server.sendUpdate(pieceArray);
    }

    public void step(int[] p1keys, int[] p2keys) {
        execute(p1keys, player1);
        execute(p2keys, player2);

        world.step(timeStep, velocityIterations, positionIterations);

        lastStepTime = (int) System.nanoTime() / 1000000;
    }

    private void resetBall(int i) {
        ball.setPosition((float) (Settings.windowSize[0] / (2 * PTM_RATIO)), (float) (Settings.windowSize[1] / (2 * PTM_RATIO)));
        ball.getBody().setAngularVelocity(0);
        System.out.println(ball.getX() + "x" + ball.getY());
        Vec2 ballVelocity;
        if (i == 0) {
            ballVelocity = new Vec2(-Settings.serveSpeed, 0);
        } else if (i == 1) {
            ballVelocity = new Vec2(Settings.serveSpeed, 0);
        } else {
            Log.error("You served the ball to not a player: player # " + i + ". Players are 0 and 1.");
            throw new RuntimeException();
        }
        ball.getBody().setLinearVelocity(ballVelocity);

    }

    public void execute(int[] keys, Player player) {
        float linearVelocity = 0;
        float turnRequest = 0;
        SolidRect paddle = player.getPaddle();
        for (int key : keys) {
            switch (key){
                case Keyboard.KEY_DOWN: {
                    linearVelocity += Settings.paddleSpeed;
                    break;
                }

                case Keyboard.KEY_UP: {
                    linearVelocity -= Settings.paddleSpeed;
                    break;
                }

                case Keyboard.KEY_RIGHT: {
                    turnRequest += 1;
                    break;
                }

                case Keyboard.KEY_LEFT: {
                    turnRequest -= 1;
                    break;
                }
                case Keyboard.KEY_SPACE: {
                    makeLaser(player);
                }
            }
            if (key == Keyboard.KEY_SPACE) {
            	
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


    public org.jbox2d.dynamics.World getWorld() {
        return world;
    }

    /** Make Methods **/

    private void makeWalls() {
    	int width = (int) (Settings.windowSize[0]/PTM_RATIO);
    	int height = (int) (Settings.windowSize[1]/PTM_RATIO);
        SolidRect botWall = new SolidRect(width/2, -0.01f, width, 0.005f, BodyType.KINEMATIC, getWorld(), this);
        rectRenderList.remove(botWall);
        botWall.getBody().getFixtureList().m_friction = 0;
        SolidRect topWall = new SolidRect(width/2, (float) (height + 0.01), width, 0.005f, BodyType.KINEMATIC, getWorld(), this);
        rectRenderList.remove(topWall);
        topWall.getBody().getFixtureList().m_friction = 0;
    }

    private void makePaddles(int height, int width){
        p1Paddle = new SolidRect(0.5f, height / 2, 0.2f, Settings.paddleLength, BodyType.KINEMATIC, getWorld(), this);
        p2Paddle = new SolidRect(width - 0.5f, height / 2, 0.2f, Settings.paddleLength, BodyType.KINEMATIC, getWorld(), this);

    }

    private void makeBall(int height, int width){
        ball = new Ball(width / 2, height / 2, Settings.ballRadius, getWorld(), this, true, '1');
        ball.getBody().setLinearVelocity(new Vec2(-Settings.serveSpeed, 0));
    }

    Ball makeLaser(Player player){
        //Paddle positions and normals
        float[] points = player.getPaddle().getPointsInPixels();
        Vec2[] normals = player.getPaddle().getShape().getNormals();

        //Laser Starting and Direction
        float x,y;
        Vec2 direction;

        //Set correct values based on player
        if (player.getId() == player1.getId()){
            x = (points[2] + points[4])/2;
            y = (points[3] + points[5])/2;
            direction = normals[1];
        } else {
            x = (points[0] + points[6])/2;
            y = (points[1] + points[7])/2;
            direction = normals[3];
        }

        return new Laser(x, y, Settings.laserRadius, direction, getWorld(), this);
    }

    @Override
    public void render(GameContainer arg0, Graphics graphics)
            throws SlickException {
        pieceArray = new GamePiece[rectRenderList.size() + ballRenderList.size()];
        int i = 0;
        for (SolidRect sr : rectRenderList) {

            float[] pts = sr.getPointsInPixels();
            Polygon poly = new Polygon(pts);
            if (sr == p1Paddle) {
                pieceArray[i] = new GamePiece(poly, ShapeType.POLY, '0');
            } else if (sr == p2Paddle) {
                pieceArray[i] = new GamePiece(poly, ShapeType.POLY, '2');
            } else {
                pieceArray[i] = new GamePiece(poly, ShapeType.POLY, '3');
            }

            ++i;
        }

        for (Ball sb : ballRenderList) {
            pieceArray[i] = new GamePiece(new Circle(sb.getX(), sb.getY(), sb.getRadius()), ShapeType.POLY, sb.getColor());
            ++i;
        }
    }

    /** Add/Remove Pong Objects **/

    public void removeSolidRect(SolidRect sr) {
        rectRenderList.remove(sr);
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

    public void addSolidBall(Ball sb) {
        ballRenderList.add(sb);
    }

    public void addSolidRect(SolidRect sr) {
        rectRenderList.add(sr);
    }

    public void addLaser(Laser laser){
        ballRenderList.add(laser);
    }


    /**
     * Getters *
     */

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
    
    public Player getPlayer(int player) {
    	if (player == 0) {
    		return player1;
    	} else if (player == 1) {
    		return player2;
    	} else {
    		return null;
    	}
    }

    public int[] getScore() {
        return score.getScore();
    }
}