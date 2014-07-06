package utils;

import pong.Pong;

/**
 * Created by sihrc on 7/5/14.
 */
public class ServerThread extends Thread {
    @Override
    public void run() {
        Pong.main(new String[0]);
    }
}
