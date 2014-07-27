package client;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import utils.Settings;

/**
 * Created by rbooth on 7/26/14.
 */
public class ClientStateBasedGame extends StateBasedGame {
    AppGameContainer app;
    /**
     * Create a new state based game
     *
     * @param name The name of the game
     */
    public ClientStateBasedGame(String name) {
        super(name);

        try {
            app = new AppGameContainer(this);
            app.setDisplayMode(Settings.windowSize[0], Settings.windowSize[1], false);
            app.setVSync(true);
            app.setAlwaysRender(true);
            app.setTargetFrameRate(Settings.fps);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        addState(new IntroState(app, this));
        addState(new PongDisplayState(this));
    }

    public static void main(String[] args) {

        ClientStateBasedGame game = new ClientStateBasedGame("Pong Client");
        game.enterState(1); // intro

    }

    public void setIPAndEnterGame(String ip) {
        IPHolder.testIp=ip;
        enterState(0); // PongDisplayState;
    }

    @Override
    public boolean closeRequested() {
        return true;
    }
}
