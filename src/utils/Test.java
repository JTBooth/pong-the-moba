package utils;

/**
 * Created by sihrc on 7/5/14.
 */
public class Test {
    public static void main(String args[]){
        (new ServerThread()).start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        (new ClientThread()).start();
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        (new ClientThread()).start();
    }
}