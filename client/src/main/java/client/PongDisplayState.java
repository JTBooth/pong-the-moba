package client;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import serialization.Bytes;
import serialize.PongPacket;
import utils.Debugger;
import utils.Registry;

public class PongDisplayState extends BasicGameState {
    private static Debugger debbie = new Debugger(PongDisplayState.class.getSimpleName());

    private Map<Character, PongPacket> packetTemplate;
    private byte[] pieces;
    private PongClient client;
    private DisplayListener displayListener;
    private Font font;

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        /** Set Typewriter **/
        graphics.setFont(font);

        /** Render Game Pieces **/
        int pointer = 0;
        char id;

        debbie.d("Entering while loop");
        while (pointer < pieces.length) {
            id = Bytes.twoBytes2Char(Arrays.copyOfRange(pieces, pointer, pointer += 2));
            pointer = packetTemplate.get(id).deserialize(pieces, pointer, graphics);
        }
    }

    private void initializePackets() {
        packetTemplate = new HashMap<Character, PongPacket>();
        try {
            for (Map.Entry<Character, Class<? extends PongPacket>> entry : Registry.packets.entrySet()) {
                PongPacket packet = entry.getValue().newInstance();
                packet.setup();
                packetTemplate.put(entry.getKey(), packet);

            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getID() {
        return 0;
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        debbie.i("Initializing");

        pieces = new byte[0];
        client = new PongClient(this);
        displayListener = client.getDisplayListener();

        try {

            while (!displayListener.isConnected()) {
                Thread.sleep(1000);
                debbie.i("Waiting for other player to connect.");
            }//Wait for both players to connect

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        initializePackets();
        font = new TrueTypeFont(new java.awt.Font("Verdana", java.awt.Font.BOLD, 80), false);
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {


    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
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