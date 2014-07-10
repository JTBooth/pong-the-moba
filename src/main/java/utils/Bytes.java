package utils;

import org.jbox2d.common.MathUtils;

import java.util.Arrays;

/**
 * Created by sihrc on 7/4/14.
 */
public class Bytes {
    public static byte float2Byte(float sizeFloat, float scale) throws IllegalArgumentException{
        if (sizeFloat < 0 || sizeFloat > scale){
            throw new IllegalArgumentException();
        }
        return (byte) ((int) (((256.0/scale)*sizeFloat - 128)-1));
    }

    public static byte[] float2Byte2(float sizeFloat, float scale) throws IllegalArgumentException {
        if (sizeFloat < 0 || sizeFloat > scale){
            throw new IllegalArgumentException();
        }
        return char2Bytes2(((char) ((int) (((65536.0 / scale) * sizeFloat) - 1))));
    }

    public static byte[] char2Bytes2(char c){
        byte[] ret = new byte[2];
        ret[0] = (byte)((c & 0Xff00) >> 8);
        ret[1] = (byte)((c & 0x00ff));
        return ret;
    }

    public static float byte2Float(byte sizeByte, float scale) {
        return (float) (scale/256.0)*((float) (sizeByte+129));
    }

    public static float twoByte2Float(byte[] sizeByte, float scale){
        int a = ((uByte((sizeByte[0])) << 8) | uByte(sizeByte[1]));
        return (float) (scale/65536.0 * (a + 1));
    }

    public static char twoBytes2Char(byte[] sizeByte){
        return (char) (((sizeByte[0]) << 8) | sizeByte[1]);
    }

    public static int uByte(byte b) {
        return b & 0x7f + (128*((b >> 7)&1));
    }

    public static void main(String[] args){
        test_float();
    }

    private static void test_float(){
        float a = 0.5f;
        System.out.println(a);

        byte res[] = float2Byte2(a, MathUtils.TWOPI);
        System.out.println(Arrays.toString(res));

        float b = twoByte2Float(res, MathUtils.TWOPI);
        System.out.println(b);
    }
}
