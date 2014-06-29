package client;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.*;

public class PongDisplay extends BasicGame {
    private GamePiece[] pieces;
    private PongClient client;
    private DisplayListener displayListener;
    private int[] keysPressed;

    public PongDisplay() {
        super("pongDisp");
        pieces = new GamePiece[0];
        client = new PongClient(this);
        displayListener = client.getDisplayListener();

        AppGameContainer app;
        try {
            app = new AppGameContainer(this);
            app.setDisplayMode((int) (800), (int) (600), false);
            app.setVSync(true);
            app.setTargetFrameRate(60);
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