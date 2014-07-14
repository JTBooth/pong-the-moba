package packets;

import org.newdawn.slick.Graphics;
import shapes.CerealRegistry;
import utils.Bytes;
import utils.Debugger;

import java.util.Arrays;

/**
 * Created by rbooth on 7/13/14.
 */
public class Cerealizer {
    public byte[] serialize(Cereal cereal){
        try {
            return cereal.serialize();
        } catch (IllegalArgumentException e) {
            return new byte[0];
        }
    }

    public static void render(byte[] bytes, Graphics graphics){
        int pointer = 0;
        char shape;
        Debugger.debugger.i("Entering while loop");
        while (pointer < bytes.length){
            shape = Bytes.twoBytes2Char(Arrays.copyOfRange(bytes, pointer, pointer += 2));
            Debugger.debugger.i(CerealRegistry.get(shape).getClass().getSimpleName());
            pointer = CerealRegistry.get(shape).deserialize(bytes, pointer, graphics);
        }
    }
}
