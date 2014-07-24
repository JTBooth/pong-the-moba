package pong;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import shapes.PongShape;

/**
 * Created by rbooth on 7/9/14.
 */
public class DragEffect extends GlobalEffect {
    private float speedLimit;
    private float dragRatio;

    public DragEffect(float speedLimit, float dragRatio) {
        this.speedLimit = speedLimit;
        this.dragRatio = dragRatio;
    }

    @Override
    public void applyForce(PongShape pongShape) {
        Body body = pongShape.getBody();
        Vec2 velocity = body.getLinearVelocity();
        float abs = velocity.length();

        float diff = abs - speedLimit;
        if (diff > 0f) {
            float ratio = abs / speedLimit;
            Vec2 restorative = new Vec2(-velocity.x * ratio * dragRatio, -velocity.y * ratio * dragRatio);
            body.applyForceToCenter(restorative);
        }
    }
}
