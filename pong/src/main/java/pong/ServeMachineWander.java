package pong;

import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Vec2;
import spell.DelayedEffect;
import utils.Settings;

import java.util.Random;

/**
 * Created by rbooth on 7/26/14.
 */
public class ServeMachineWander extends DelayedEffect {
    ServeMachine serveMachine;
    Random random;
    float xVelocity;
    float yVelocity;
    float rotVelocity;
    float jetpackDirection;
    float maxDistMeters;
    float verticalStretch;

    float originX;
    float originY;

    public ServeMachineWander(ServeMachine serveMachine, float maxDistMeters, float verticalStretch) {
        super(2);
        this.serveMachine=serveMachine;
        random=new Random();
        this.maxDistMeters=maxDistMeters;
        this.verticalStretch=verticalStretch;
        xVelocity=0;
        yVelocity=0;
        rotVelocity = 0;
        originX=serveMachine.getxPos();
        originY=serveMachine.getyPos();
    }

    @Override
    public void tick() {
        float xPos = serveMachine.getxPos();
        float yPos = serveMachine.getyPos();
        float rotation = serveMachine.getRotation();

        // update forces on the serve machine
        Vec2 jetpackForce = new Vec2((float) Math.cos(jetpackDirection), (float) Math.sin(jetpackDirection))
                .mul(Settings.jetpackForce);
        Vec2 vecToOrigin = vecToOrigin(xPos, yPos);
        Vec2 restoringForce = vecToOrigin.mul(vecToOrigin.length()/maxDistMeters).mul(0.2f);
        restoringForce.y = restoringForce.y/verticalStretch;

        // Spin jetpack
        jetpackDirection += random.nextFloat()*0.05 - 0.045;
        if (jetpackDirection > MathUtils.TWOPI) {
            jetpackDirection -= MathUtils.TWOPI;
        }

        Vec2 force = jetpackForce.add(restoringForce);

        // update velocity based on serveMachine's mass
        xVelocity += force.x;
        yVelocity += force.y;

        // move serve machine according to updated velocity
        serveMachine.setxPos(xPos + xVelocity);
        serveMachine.setyPos(yPos + yVelocity);

        System.out.println("position: " + xPos + ", " + yPos);
        System.out.println("force: " + force);
        System.out.println("jpd: " + jetpackDirection);
    }

    private Vec2 vecToOrigin(float xPos, float yPos) {
        return new Vec2(originX - xPos, originY - yPos);
    }

    @Override
    public boolean timeToAct() {
        return false;
    }

    @Override
    public void takeAction() {

    }
}
