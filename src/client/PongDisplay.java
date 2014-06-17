package client;

import java.util.ArrayList;
import java.util.List;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import pong.SolidBall;
import pong.SolidRect;

public class PongDisplay extends BasicGame {
	private List<GamePiece> pieces;
	private PongClient client;
	private DisplayListener displayListener;
	
	public PongDisplay() {
		super("pongDisp");
		pieces = new ArrayList<GamePiece>();
		client = new PongClient();
		displayListener=client.getDisplayListener();
		
		AppGameContainer app;
		try {
			app = new AppGameContainer(this);
			app.setDisplayMode((int) (800), (int) (600),
					false);
			app.setVSync(true);
			app.setTargetFrameRate(60);
			app.start();
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
		for (GamePiece gp : pieces) {
			graphics.setColor(gp.getColor());
			graphics.fill(gp.getShape());
		}
	}

	@Override
	public void init(GameContainer arg0) throws SlickException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(GameContainer arg0, int arg1) throws SlickException {
		pieces = displayListener.getRenderList();
	}

	public static void main(String[] args) {
		new PongDisplay();
	}
	
}
