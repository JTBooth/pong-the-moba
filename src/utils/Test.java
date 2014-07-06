package utils;

import client.PongDisplay;

import java.net.URL;

/**
 * Created by sihrc on 7/5/14.
 */
public class Test {
    public static void main(String args[]) throws InterruptedException {
//        (new ServerThread()).start();
//        Thread.sleep(10000);
//        new Thread(){
//            @Override
//            public void run() {
//                new PongDisplay();
//            }
//        }.start();
//
//        new Thread() {
//            @Override
//            public void run() {
//                new PongDisplay();
//            }
//        }.start();
////        (new ClientThread()).start();
////        Thread.sleep(1500);
////        (new ClientThread()).start();
//        while (true){
//            Thread.sleep(1000);
//        }
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//        URL url = classLoader.getResource("../../../gameplaySettingsSpec");
        URL url = classLoader.getResource("/res/gameplaySettingsSpec");
        System.out.println(url.toString());
    }
}