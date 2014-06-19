package pong;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.jbox2d.dynamics.World;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.util.Log;

import packets.SerializableCircle;
import packets.SerializableColor;
import client.GamePiece;
import server.Player;
import server.PongServer;


public class Pong extends BasicGame {
	private String title;
	private List<SolidRect> rectRenderList;
	private List<SolidBall> ballRenderList;
	private List<GamePiece> displayList;
	private Player player1;
	private Player player2;
	private World world;
	private AppGameContainer app;
	float timeStep;
	int velocityIterations;
	int positionIterations;
	public static final double PTM_RATIO = 100.0;
	public final int pixWidth = 800;
	public final int pixHeight = 600;
	int lastStepTime;
	private static final int[] relevantKeys = { Keyboard.KEY_UP,
			Keyboard.KEY_DOWN, Keyboard.KEY_LEFT, Keyboard.KEY_RIGHT };
	private static final float maxPaddleRotateAngle = 0.5f;
	Keyboard keyboard;
	private SolidRect p1Paddle;
	private SolidRect p2Paddle;
	private SolidBall ball;
	long frame;
	private SolidRect topWall;
	private SolidRect botWall;
	private PongServer server;

	public Pong(String title) throws IOException {
		super(title);
		
		this.title = title;
		rectRenderList = new ArrayList<SolidRect>();
		ballRenderList = new ArrayList<SolidBall>();
		displayList = new ArrayList<GamePiece>();
		server = new PongServer(this, relevantKeys);
		
		Vec2 gravity = new Vec2(0, 0);
		world = new World(gravity);
		

		this.timeStep = 1f / 60f;
		this.velocityIterations = 10;
		this.positionIterations = 10;
		frame=0;

		AppGameContainer app;
		try {
			app = new AppGameContainer(this);
			app.setDisplayMode((int) (8 * PTM_RATIO), (int) (6 * PTM_RATIO),
					false);
			app.setVSync(true);
			app.setAlwaysRender(true);
			app.setTargetFrameRate(60);
			
			app.start();
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private void makeWalls() {
		botWall = new SolidRect(4, -0.01f, 8, 0.005f, BodyType.KINEMATIC, getWorld(), this);
		botWall.getBody().getFixtureList().m_friction=0;
		topWall = new SolidRect(4, 6.01f, 8, 0.005f, BodyType.KINEMATIC, getWorld(), this);
		topWall.getBody().getFixtureList().m_friction=0;
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
		p1Paddle = new SolidRect(0.5f, 3, 0.2f, 2, BodyType.KINEMATIC, getWorld(), this);
		p2Paddle = new SolidRect(7.5f, 3, 0.2f, 2, BodyType.KINEMATIC, getWorld(), this);
		makeWalls();

		ball = new SolidBall(4, 3, 0.2f, getWorld(), this);
		ball.getBody().setLinearVelocity(new Vec2(-2, 0));
		
		while (player1 == null || player2 == null) {
			try {Thread.sleep(5000);} catch (InterruptedException e) {}
			System.out.println("Waiting for players to load: " + (player1 == null) + " " + (player2 == null));
		}
	}

	@Override
	public void render(GameContainer arg0, Graphics graphics)
			throws SlickException {
		displayList = new ArrayList<GamePiece>();
		for (SolidRect sr : rectRenderList) {
			float[] pts = sr.getPointsInPixels();
			Polygon poly = new Polygon(pts);
			displayList.add(new GamePiece(poly, new SerializableColor(255,0,0)));
		}

		for (SolidBall sb : ballRenderList) {
			displayList.add(new GamePiece(new SerializableCircle(sb.getX(), sb.getY(), sb.getRadius()), new SerializableColor(0, 255, 0)));
		}
		
		for (GamePiece gp : displayList) {
			graphics.setColor(gp.getColor());
			graphics.fill(gp.getShape());
		}
	}

	@Override
	public void update(GameContainer arg0, int arg1) throws SlickException {
		++frame;
		
		int[] p1keys = player1.getKeys();
		int[] p2keys = player2.getKeys();
		
		step(p1keys, p2keys);
		server.sendUpdate(displayList);
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
				linearVelocity += 2.0;
			}
			if (key == Keyboard.KEY_UP) {
				linearVelocity -= 2.0;
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
			if (currentAngle*request > 0) {
				currentAngle = Math.abs(currentAngle);
				rate = Math.abs(maxPaddleRotateAngle - currentAngle) / maxPaddleRotateAngle;
			} else {
				currentAngle = Math.abs(currentAngle);
				rate = Math.abs(maxPaddleRotateAngle + currentAngle) / maxPaddleRotateAngle;
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

	public void addSolidBall(SolidBall sb) {
		ballRenderList.add(sb);
	}

	public void removeSolidBall(SolidBall sb) {
		ballRenderList.remove(sb);
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
}
