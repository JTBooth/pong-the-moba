package media;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import utils.Debugger;

/**
 * Created by rbooth on 7/10/14.
 */
public class SoundMaster {
    Debugger debbie = new Debugger(SoundMaster.class.getSimpleName(), Debugger.DEBUG);
    private Sound bounce;

    public SoundMaster() {

        try {
            bounce = new Sound("src/main/resources/bounce_1.aif");
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }


    public void playBounce(float speed) {
        debbie.d("contact at speed " + speed);
        float volume = 1f - (1f/(speed+1f));
        debbie.d("sound at volume " + volume);
        bounce.play(1f, volume);
    }


}
