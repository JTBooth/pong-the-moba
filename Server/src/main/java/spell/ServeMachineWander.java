package spell;

import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Vec2;

import java.util.Random;

import shapes.ServeMachine;
import utils.Settings;

/**
 * Created by rbooth on 7/26/14.
 */
public class ServeMachineWander extends DelayedSpell {
    ServeMachine serveMachine;
    Random random;
    float xVelocity;
    float yVelocity;
    float rotVelocity;
    float jetpackDirection;
    float maxDistMeters;
    float verticalStretch;
    boolean up;

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
        up=true;
    }

    @Override
    public void tick() {
        float xPos = serveMachine.getxPos();
        float yPos = serveMachine.getyPos();
        float rotation = serveMachine.getAngle();

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

        // handle rotation wobble
        if (up && rotation < Settings.serveMachineMaxRotation)
            rotation += 0.01*Settings.serveMachineMaxRotation;
        else if (up && rotation >= Settings.serveMachineMaxRotation)
            up=false;
        else if (!up && rotation > -Settings.serveMachineMaxRotation)
            rotation -= 0.01*Settings.serveMachineMaxRotation;
        else if (!up && rotation <= -Settings.serveMachineMaxRotation)
            up=true;


        // move serve machine according to updated velocity
        serveMachine.setxPos(xPos + xVelocity);
        serveMachine.setyPos(yPos + yVelocity);
        serveMachine.setRotation(rotation);
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
