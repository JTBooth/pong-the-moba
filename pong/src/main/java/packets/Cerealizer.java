package packets;

import org.newdawn.slick.Graphics;

import java.util.Arrays;

import shapes.CerealRegistry;
import shapes.IllegalShapeException;
import utils.Bytes;
import utils.Debugger;

/**
 * Created by rbooth on 7/13/14.
 */
public class Cerealizer {
    static Debugger debbie = new Debugger(Cerealizer.class.getSimpleName());

    public static void render(byte[] bytes, Graphics graphics) {
        int pointer = 0;
        char shape;
        debbie.d("Entering while loop");
        while (pointer < bytes.length) {
            shape = Bytes.twoBytes2Char(Arrays.copyOfRange(bytes, pointer, pointer += 2));
            debbie.i("rendering " + CerealRegistry.get(shape).getClass().getSimpleName());
            pointer = CerealRegistry.get(shape).deserialize(bytes, pointer, graphics);
        }
    }

    public byte[] serialize(Cereal cereal) {
        try {
            return cereal.serialize();
        } catch (IllegalShapeException e) {
            return new byte[0];
        }
    }
}
