package client;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

import packets.Cerealizer;
import utils.Settings;
import shapes.PongShape;
import utils.Debugger;

public class PongDisplay extends BasicGame {
    private static Debugger debbie = new Debugger(PongDisplay.class.getSimpleName(), Debugger.INFO);

    private byte[] pieces;
    private PongClient client;
    private DisplayListener displayListener;
    private Font font;

    public PongDisplay() {
        super("pongDisp");
        pieces = new byte[0];
        client = new PongClient(this);
        displayListener = client.getDisplayListener();
        AppGameContainer app;
        try {
            app = new AppGameContainer(this);
            app.setDisplayMode(Settings.windowSize[0], Settings.windowSize[1], false);
            app.setVSync(true);
            app.setAlwaysRender(true);
            app.setTargetFrameRate(Settings.fps);
            while (!displayListener.isConnected()){
                Thread.sleep(1000);
                debbie.i("Waiting for other player to connect.");
            }//Wait for both players to connect
            app.start();
        } catch (SlickException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new PongDisplay();
    }

    @Override
    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
        debbie.i("Rendering");
        /** Set Typewriter **/
        graphics.setFont(font);

        /** Render Game Pieces **/
        Cerealizer.render(pieces, graphics);
    }

    @Override
    public void init(GameContainer arg0) throws SlickException {
        debbie.i("Initializing");
        font = new TrueTypeFont(new java.awt.Font("Verdana", java.awt.Font.BOLD, 80), false);
    }

    @Override
    public void update(GameContainer arg0, int arg1) throws SlickException {
        pieces = displayListener.getRenderList();
        int[] relevantCharacters = displayListener.getRelevantCharacters();
        int[] keysPressed = new int[relevantCharacters.length];
        for (int i = 0; i < relevantCharacters.length; ++i) {
            if (Keyboard.isKeyDown(relevantCharacters[i])) {
                keysPressed[i] = relevantCharacters[i];
            } else {
                keysPressed[i] = Keyboard.CHAR_NONE;
            }
        }
        client.submit(keysPressed, displayListener.getGameId());
    }

}