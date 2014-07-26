package serialize;

/**
 * Created by sihrc on 7/23/14.
 */
public class Packet {
    public Pattern pattern;
    public Object data;
    public float scale;

    public Packet(Pattern pattern, Object data, float scale) {
        this.pattern = pattern;
        this.scale = scale;

        if (data == null) {
            this.data = new Object();
        } else {
            this.data = data;
        }
    }

    public Packet(Pattern pattern, float scale) {
        this(pattern, null, scale);
    }
    public Packet(Pattern pattern){
        this(pattern, null, 0);
    }

    @Override
    public String toString() {
        return "[" + scale + " scale " + pattern + " pattern " + data + " data]";
    }
}