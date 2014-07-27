package serialize;

/**
 * Created by sihrc on 7/23/14.
 */
public class Packet {
    public Pattern pattern;
    public Object data;
    public float scale;

    public Packet(Pattern pattern, Object data) {
        this(pattern, data, 0);
    }

    public Packet(Pattern pattern, Object data, float scale) {
        this.pattern = pattern;
        this.scale = scale;

        if (data == null) {
            this.data = new Object();
        } else {
            this.data = data;
        }
    }

    @Override
    public String toString() {
        return "[" + scale + " scale " + pattern + " pattern " + data + " data]";
    }
}