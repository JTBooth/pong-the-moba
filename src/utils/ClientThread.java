package utils;

import client.PongDisplay;

/**
 * Created by sihrc on 7/5/14.
 */
public class ClientThread extends Thread {
    @Override
    public void run() {
        PongDisplay.main(new String[0]);
    }
}
