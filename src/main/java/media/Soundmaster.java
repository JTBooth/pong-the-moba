package media;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;
import utils.Debugger;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import java.applet.AudioClip;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rbooth on 7/10/14.
 */
public class Soundmaster {
    private Sound bounce;

    public Soundmaster() {

        try {
            bounce = new Sound("src/main/resources/bounce_1.aif");
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }


    public void playBounce(float speed) {
        Debugger.debugger.enable();
        Debugger.debugger.d("contact at speed " + speed);
        float volume = 1f - (1f/(speed+1f));
        Debugger.debugger.d("sound at volume " + volume);
        bounce.play(1f, volume);
        Debugger.debugger.disable();
    }


}
