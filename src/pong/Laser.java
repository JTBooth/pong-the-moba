package pong;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

/**
 * Created by sihrc on 6/29/14.
 */
public class Laser extends Ball {
    private Vec2 direction;
    private Vec2 velocity;

    /** Constructor **/
    public Laser(float x, float y, float r, Vec2 direction, World world, Pong pong) {
        super(x, y, r, world, pong, true);
        this.direction = direction;
        this.velocity = direction.mul(Settings.laserVelocity);
    }

    public Vec2 getDirection(){
        return direction;
    }

    public Vec2 getVelocity(){
        return velocity;
    }
}
