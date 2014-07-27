package serialization;

/**
 * Created by sihrc on 7/23/14.
 */
public class Packet {
    public Pattern pattern;
    public Object data;
    public float scale;

    public static Packet pattern (Pattern pattern, float scale) {
        return new Packet(pattern, null, scale);
    }

    public static Packet pattern (Pattern pattern) {
        return pattern(pattern, 0);
    }

    public static Packet data (Pattern pattern, Object data, float scale) {
        return new Packet(pattern, data, scale);
    }

    public static Packet data (Pattern pattern, Object data) {
        return data(pattern, data, 0);
    }

    private Packet(Pattern pattern, Object data, float scale) {
        this.pattern = pattern;
        this.scale = scale;

        if (data == null) {
            this.data = new Object();
        } else {
            this.data = data;
        }
    }

    public Packet(Pattern pattern) {
        this(pattern, null, 0);
    }

    @Override
    public String toString() {
        return "[" + scale + " scale " + pattern + " pattern " + data + " data]";
    }
}