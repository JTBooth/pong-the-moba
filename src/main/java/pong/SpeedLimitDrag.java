package pong;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import shapes.PongShape;
import utils.Debugger;

/**
 * Created by rbooth on 7/9/14.
 */
public class SpeedLimitDrag extends GlobalEffect {
    private float speedLimit;
    private float dragRatio;

    public SpeedLimitDrag(float speedLimit, float dragRatio) {
        this.speedLimit=speedLimit;
        this.dragRatio=dragRatio;
    }

    @Override
    public void applyForce(PongShape pongShape) {
        Debugger.debugger.enable();
        Debugger.debugger.d("sld is pondering");
        Body body = pongShape.getBody();
        Vec2 velocity = body.getLinearVelocity();
        float abs = velocity.length();

        float diff = abs - speedLimit;
        Debugger.debugger.d("ball abs velocity is " + abs + ". this is above speed limit: " + (diff > 0f));
        if (diff > 0f) {
            Debugger.debugger.d("sld is applying forces");
            float ratio = abs/speedLimit;
            Vec2 restorative = new Vec2(-velocity.x * ratio * dragRatio, -velocity.y * ratio * dragRatio);
            body.applyForceToCenter(restorative);

        }
        Debugger.debugger.disable();
    }
}
