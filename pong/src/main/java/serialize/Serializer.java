package serialize;

import org.newdawn.slick.Graphics;

import java.util.Arrays;

import shapes.Registry;
import utils.IllegalShapeException;
import utils.Debugger;

/**
 * Created by rbooth on 7/13/14.
 */
public class Serializer {
    static Debugger debbie = new Debugger(Serializer.class.getSimpleName());

    public static void render(byte[] bytes, Graphics graphics) {
        int pointer = 0;
        char id;
        debbie.d("Entering while loop");
        while (pointer < bytes.length) {
            id = Bytes.twoBytes2Char(Arrays.copyOfRange(bytes, pointer, pointer += 2));
            debbie.d("rendering " + Registry.getPacket(id).getClass().getSimpleName());
            pointer = Registry.getPacket(id).deserialize(bytes, pointer, graphics);
        }
    }

    public byte[] serialize(PongPacket pongPacket) {
        try {
            return pongPacket.serialize();
        } catch (IllegalShapeException e) {
            return new byte[0];
        }
    }
}
