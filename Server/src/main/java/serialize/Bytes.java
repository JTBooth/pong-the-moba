package serialize;

import utils.IllegalShapeException;

/**
 * Created by sihrc on 7/4/14.
 */
public class Bytes {
    public static byte float2Byte(float sizeFloat, float scale) throws IllegalShapeException {
        if (sizeFloat < 0 || sizeFloat > scale) {
            throw new IllegalShapeException();
        }
        return (byte) ((int) (((256.0 / scale) * sizeFloat - 128) - 1));
    }


    public static byte[] float2Byte2(float sizeFloat, float scale) throws IllegalShapeException {
        if (sizeFloat < 0 || sizeFloat > scale * 1.1f) {
            throw new IllegalShapeException();
        }
        return char2Bytes2(((char) ((int) (((65536.0 / scale) * sizeFloat) - 1))));
    }

    public static byte[] char2Bytes2(char c) {
        byte[] ret = new byte[2];
        ret[0] = (byte) ((c & 0Xff00) >> 8);
        ret[1] = (byte) ((c & 0x00ff));
        return ret;
    }

    public static int uByte(byte b) {
        return b & 0x7f + (128 * ((b >> 7) & 1));
    }
}
