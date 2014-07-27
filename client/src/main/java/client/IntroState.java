package client;

import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.awt.Font;

/**
 * Created by rbooth on 7/26/14.
 */
public class IntroState extends BasicGameState {
    AppGameContainer app;
    ClientStateBasedGame boss;
    TextField ipEntryField;
    TextField instructions;
    TextField goButton;
    Rectangle goButtonRect = new Rectangle(350, 320, 100, 40);
    int countdown=3;
    boolean done=false;

    @Override
    public int getID() {
        return 1;
    }

    public IntroState(AppGameContainer app, ClientStateBasedGame boss) {
        this.boss=boss;
        this.app=app;
    }



    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {
        if (button == 0 && goButtonRect.contains(x, y)) {
            instructions.setText("Thank you. Please wait for your opponent.");
            done=true;

            System.out.println("clicked on GO");
        }
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {

        System.out.println("initialized introState");
        ipEntryField = new TextField(app, new TrueTypeFont(new Font("Arial", Font.PLAIN, 20), true), 300, 290, 200, 20, null);
        ipEntryField.setTextColor(new Color(255, 255, 255));
        ipEntryField.setBorderColor(new Color(255, 255, 255));
        ipEntryField.setBackgroundColor(new Color(0, 0, 0));


        instructions = new TextField(app, new TrueTypeFont(new Font("Arial", Font.PLAIN, 12), true), 275, 266, 250, 14, null);
        instructions.setTextColor(new Color(255,255,255));
        instructions.setBackgroundColor(new Color(0,0,0));
        instructions.setBorderColor(new Color(0,0,0));
        instructions.setText("type your IP address here, then click \"go\"");


        goButton = new TextField(app, new TrueTypeFont(new Font("Arial", Font.PLAIN, 40), true), 350, 320, 100, 40, null);
        goButton.setBackgroundColor(new Color(255,0,0));
        goButton.setBorderColor(new Color(0,0,0));
        goButton.setTextColor(new Color(255,255,255));
        goButton.setText("GO");

        ipEntryField.setFocus(true);



    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        ipEntryField.render(container, g);
        instructions.render(container, g);
        goButton.render(container, g);
    }



    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        if (done) {
            countdown -= 1;
        }
        if (countdown == 0) {
            if (ipEntryField.getText().equals("")) {
                boss.setIPAndEnterGame("192.168.1.88"); //FIXME - shh don't tell JT
            } else {
                boss.setIPAndEnterGame(ipEntryField.getText());
            }
        }
    }

}