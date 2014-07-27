package sounds;

/**
 * Created by sihrc on 7/26/14.
 */
public class BounceSound extends PongSound {
    float speed;

    public BounceSound() {
    }

    public BounceSound(float speed) {
        this.speed = speed;
    }

    @Override
    public float getVolume() {
        return 1f - (1f / (speed + 1f));
    }
}
