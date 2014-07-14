package packets;

import org.newdawn.slick.Graphics;

/**
 * Created by rbooth on 7/13/14.
 */
public interface Cereal {
    public abstract byte[] serialize() throws IllegalArgumentException;
    public abstract int deserialize(byte[] cereal, int pointer, Graphics graphics);
    public abstract boolean visible();
    public abstract char getId();


}
