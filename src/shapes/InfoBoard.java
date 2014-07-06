package shapes;

import org.jbox2d.collision.shapes.Shape;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

import pong.Settings;
import utils.Bytes;

/**
 * Created by sihrc on 6/28/14.
 */
public class InfoBoard extends PongShape{
    /**
     * Graphics *
     */
    TrueTypeFont font;
    /**
     * SCORES *
     */
    private byte left = 0;
    private byte right = 0;

    /**
     * Constructor *
     */
    public InfoBoard(){}
    public InfoBoard(TrueTypeFont font) {
        this.font = font;
    }

    @Override
    public char getId() {
        return ShapeRegistry.INFO_BOARD;
    }

    /**
     * Get Font for registering
     */
    public TrueTypeFont getFont() {
        return font;
    }

    @Override
    public byte[] serialize() {
        byte[] serialized = new byte[7];
        int pointer = 0;

        //Shape ID
        System.arraycopy(Bytes.char2Bytes2(getId()), 0, serialized, pointer, 2);
        pointer += 2;


        //Left Score
        serialized[pointer] = left;
        pointer++;

        //Right Score
        serialized[pointer] = right;

        return serialized;
    }

    @Override
    public int deserialize(byte[] cereal, int pointer, Graphics graphics) {
        font.drawString(Settings.scorePositions[0], Settings.scorePositions[1], String.valueOf((int)cereal[pointer++]), Settings.colorMap.get('0'));
        font.drawString(Settings.scorePositions[2], Settings.scorePositions[3], String.valueOf((int) cereal[pointer++]), Settings.colorMap.get('1'));
        return pointer;
    }

    @Override
    public boolean visible() {
        return false;
    }

    @Override
    public Shape getBoxShape() {
        return null;
    }

    @Override
    public org.newdawn.slick.geom.Shape getSlickShape() {
        return null;
    }
}
