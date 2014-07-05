package utils;

/**
 * Created by sihrc on 7/4/14.
 */
public class Bytes {
    public static byte uniformLinearFloat2Byte(float sizeFloat, int scale) {
        assert sizeFloat >= 0;
        assert sizeFloat <= scale;
        return (byte) ((int) (((256.0/scale)*sizeFloat)-1));
    }

    public static float uniformLinearByte2Float(byte sizeByte, int scale) {
        return (float) (scale/256.0)*((float) (sizeByte+1));
    }

    public static char uniformLinearFloat2Char(float sizeFloat, int scale) {
        assert sizeFloat >= 0;
        assert sizeFloat <= scale;
        return (char) ((int) (((65535.0/scale)*sizeFloat)-1));
    }

    public static float uniformLinearChar2Float(char sizeByte, int scale) {
        return (float) (scale/65535.0)*((float) (sizeByte+1));
    }
}
