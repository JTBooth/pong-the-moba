package utils;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.SpriteSheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import client.resources.SpriteSheetMap;
import serialize.PongPacket;
import shapes.Ball;
import shapes.InfoBoard;
import shapes.Laser;
import shapes.Paddle;
import shapes.Wall;
import sounds.BounceSound;
import spell.LaserSpell;
import spell.RestitutionBoost;
import spell.Spell;

/**
 * Created by sihrc on 7/5/14.
 */
public class Registry {
    static Debugger debbie = new Debugger(Registry.class.getSimpleName());

    /***********************************************************************
     * * * * * * * * ** * * * * * * Packets * * * * * * * * * * * * * * * **
     ***********************************************************************/
    //Sounds
    final public static String SOUND_PATH = "pong/src/main/resources/";

    //Registered Packets
    public static final List<Class<? extends PongPacket>> classes = new ArrayList<Class<? extends PongPacket>>() {{
        add(Ball.class);
        add(Laser.class);
        add(Paddle.class);
        add(Wall.class);
        add(InfoBoard.class);
        add(BounceSound.class);
    }};
    //Packet IDs
    public static final Map<Character, Class<? extends PongPacket>> packets = new HashMap<Character, Class<? extends PongPacket>>() {{
        /** Char Limit **/
        assert classes.size() < 255;

        for (int i = 0; i < classes.size(); i++) {
            put((char) i, classes.get(i));
        }
    }};

    //Get Packet ID
    public static char getPacketId(Class<? extends PongPacket> packet) {
        return (char) classes.indexOf(packet);
    }

    //Get Packet
    public static PongPacket getPacket(char key) {
        try {
            return packets.get(key).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /***********************************************************************
     * * * * * * * * ** * * * * * * Spells * * * * * * * * * * * * * * * * *
     ***********************************************************************/
    public static final Map<Integer, Class<? extends Spell>> spells = new HashMap<Integer, Class<? extends Spell>>(){{
        put(Keyboard.KEY_SPACE, LaserSpell.class);
        put(Keyboard.KEY_Q, RestitutionBoost.class);
    }};

    /***********************************************************************
     * * * * * * * * ** * * * Sprite Sheet Map * * * * * * * * * * * * * * *
     ***********************************************************************/

    public static final SpriteSheetMap spriteSheetMap = new SpriteSheetMap();

    public static SpriteSheet getSpriteSheet(byte spriteSheetId) {
        return spriteSheetMap.get(spriteSheetId);
    }
}
