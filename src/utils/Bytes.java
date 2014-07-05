package utils;

/**
 * Created by sihrc on 7/4/14.
 */
public class Bytes {
    public static byte float2Byte(float sizeFloat, float scale) {
        assert sizeFloat >= 0;
        assert sizeFloat <= scale;
        return (byte) ((int) (((256.0/scale)*sizeFloat)-1));
    }

    public static float byte2Float(byte sizeByte, float scale) {
        return (float) (scale/256.0)*((float) (sizeByte+1));
    }

    public static byte[] float2Byte2(float sizeFloat, float scale) {
        assert sizeFloat >= 0;
        assert sizeFloat <= scale;
        return char2Bytes2(((char) ((int) (((65536.0/scale)*sizeFloat)-1))));
    }

    public static float twoByte2Float(byte[] sizeByte, float scale){
        char c = (char) ((((char) sizeByte[0]) << 8) | sizeByte[1]);
        return (float) (scale/65536.0)*((float) (c+1));
    }

    public static byte[] char2Bytes2(char c){
        byte[] ret = new byte[2];
        ret[0] = (byte)((c & 0Xff00) >> 8);
        ret[1] = (byte)((c & 0x00ff));
        return ret;
    }

    public static void main(String[] args){
        float a = 102.22491f;

        byte[] res = float2Byte2(a, 300);
        System.out.println(twoByte2Float(res, 300));
//        byte[] res = float2Byte(a, 300);
//        System.out.println(twoByte2Float(res, 300));
    }
}
