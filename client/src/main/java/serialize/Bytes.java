package serialize;

/**
 * Created by sihrc on 7/4/14.
 */
public class Bytes {
    public static float byte2Float(byte sizeByte, float scale) {
        return (float) (scale / 256.0) * ((float) (sizeByte + 129));
    }

    public static char twoBytes2Char(byte[] sizeByte) {
        return (char) (((sizeByte[0]) << 8) | sizeByte[1]);
    }

    public static float twoByte2Float(byte[] sizeByte, float scale) {
        int a = ((uByte((sizeByte[0])) << 8) | uByte(sizeByte[1]));
        return (float) (scale / 65536.0 * (a + 1));
    }

    public static int uByte(byte b) {
        return b & 0x7f + (128 * ((b >> 7) & 1));
    }
}
