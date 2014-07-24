package manager;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import utils.Debugger;

/**
 * Created by rbooth on 7/10/14.
 */
public class SoundManager {
    Debugger debbie = new Debugger(SoundManager.class.getSimpleName());
    private Sound bounce;

    public SoundManager() {

        try {
            bounce = new Sound("pong/" +
                    "src/main/resources/bounce_1.aif");
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }


    public void playBounce(float speed) {
        debbie.i("contact at speed " + speed);
        float volume = 1f - (1f / (speed + 1f));
        debbie.i("sound at volume " + volume);
        bounce.play(1f, volume);
    }


}
