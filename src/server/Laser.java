package server;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import pong.Settings;
import shapes.Ball;

/**
 * Created by sihrc on 6/29/14.
 */
public class Laser extends Ball {
    private Vec2 direction;

    /**
     * Constructor *
     */
    public Laser(float x, float y, float r, Vec2 direction, World world) {
        super(x, y, r, world, true, '3');
        this.direction = direction;
        getBody().setLinearVelocity(direction.mul(Settings.laserVelocity));
        getBody().getFixtureList().setDensity(10);
        getBody().getFixtureList().setDensity(Settings.laserDensity);
    }

    public Vec2 getDirection() {
        return direction;
    }
}
