package packets;

import org.newdawn.slick.Graphics;

import shapes.IllegalShapeException;

/**
 * Created by rbooth on 7/13/14.
 */
public interface Cereal {
    public abstract byte[] serialize() throws IllegalShapeException;

    public abstract int deserialize(byte[] cereal, int pointer, Graphics graphics);

    public abstract boolean visible();

    public abstract char getId();
}
