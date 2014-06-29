package pong;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

/**
 * Created by sihrc on 6/29/14.
 */
public class Laser extends Ball {
    private Vec2 direction;

    /** Constructor **/
    public Laser(float x, float y, float r, Vec2 direction, World world, Pong pong) {
        super(x, y, r, world, pong, true, '3');
        this.direction = direction;
        this.getBody().setLinearVelocity(direction.mul(Settings.laserVelocity));
        pong.addLaser(this);
    }

    public Vec2 getDirection(){
        return direction;
    }
}
