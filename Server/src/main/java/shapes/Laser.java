package shapes;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import pong.Pong;
import utils.Settings;

/**
 * Created by sihrc on 6/29/14.
 */
public class Laser extends Ball {
    private Vec2 direction;

    /**
     * Constructor *
     */
    public Laser(float x, float y, float r, Vec2 direction, World world, Pong pong) {
        super(x, y, r, world, true, '3', pong);
        this.direction = direction;
        getBody().setLinearVelocity(direction.mul(Settings.laserVelocity));
        getBody().getFixtureList().setDensity(10);
        getBody().getFixtureList().setDensity(Settings.laserDensity);
    }

    public Vec2 getDirection() {
        return direction;
    }
}
