package client;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

import packets.GamePiece;
import pong.Settings;

public class PongDisplay extends BasicGame {
    private GamePiece[] pieces;
    private Scoreboard scoreboard;
    private PongClient client;
    private DisplayListener displayListener;
    public PongDisplay() {
        super("pongDisp");
        pieces = new GamePiece[0];
        client = new PongClient(this);
        displayListener = client.getDisplayListener();

        AppGameContainer app;
        try {
            app = new AppGameContainer(this);
            app.setDisplayMode(Settings.windowSize[0], Settings.windowSize[1], false);
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

        new PongDisplay();
    }

    @Override
    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
        /** Set Typewriter **/
        graphics.setFont(scoreboard.getFont());

        /** Render Score Board **/
        scoreboard.render();
        
        /** Render Game Pieces **/
        for (GamePiece gp : pieces) {
            graphics.setColor(gp.getColor());
            graphics.fill(gp.getShape());
        }

        
    }

    @Override
    public void init(GameContainer arg0) throws SlickException {
        scoreboard = new Scoreboard(new TrueTypeFont(new java.awt.Font("Verdana", java.awt.Font.BOLD, 80), false));
    }

    @Override
    public void update(GameContainer arg0, int arg1) throws SlickException {
        pieces = displayListener.getRenderList();
        scoreboard.update(displayListener.getScores());
        int[] relevantCharacters = displayListener.getRelevantCharacters();
        int[] keysPressed = new int[relevantCharacters.length];
        for (int i = 0; i < relevantCharacters.length; ++i) {
            if (Keyboard.isKeyDown(relevantCharacters[i])) {
                keysPressed[i] = relevantCharacters[i];
            } else {
                keysPressed[i] = Keyboard.CHAR_NONE;
            }
        }
        client.submit(keysPressed);
    }

}