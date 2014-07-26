package client.resources;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rbooth on 7/24/14.
 */
public class SpriteSheetMap extends HashMap<Byte, SpriteSheet> {
    public SpriteSheetMap() {
        SpriteSheet redPaddle = null;
        SpriteSheet bluePaddle = null;
        try {
            redPaddle = new SpriteSheet("pong/src/main/resources/redPaddleSprite.jpg", 20, 200);
            bluePaddle = new SpriteSheet("pong/src/main/resources/bluePaddleSprite.jpg", 20, 200);
        } catch (SlickException e) {
            e.printStackTrace();
        }


        this.put(RED_PADDLE, redPaddle);
        this.put(BLUE_PADDLE, bluePaddle);
    }

    public static final byte RED_PADDLE = (byte) 0;
    public static final byte BLUE_PADDLE = (byte) 1;
}
