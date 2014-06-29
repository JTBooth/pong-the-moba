package pong;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

public class Ball {
    private CircleShape shape;
    private Body body;
    private Pong pong;

    public Ball(float x, float y, float r, World world, Pong pong) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x, y);
        bodyDef.type = BodyType.DYNAMIC;
        Body body = world.createBody(bodyDef);

        CircleShape circleShape = new CircleShape();
        circleShape.m_radius = r;
        shape = circleShape;

        FixtureDef fd = new FixtureDef();
        fd.density = 1.0f;
        fd.friction = 1f;
        fd.restitution = 1.0f;
        fd.shape = shape;
        body.createFixture(fd);
        this.body = body;

        this.pong = pong;
        pong.addSolidBall(this);
    }

    public float getX() {
        return (float) (body.getPosition().x * Pong.PTM_RATIO);
    }

    public float getY() {
        return (float) (body.getPosition().y * Pong.PTM_RATIO);
    }

    public float getRadius() {
        return (float) (shape.m_radius * Pong.PTM_RATIO);
    }

    public Body getBody() {
        return body;
    }

    public void destroy() {
        body.getWorld().destroyBody(body);
        pong.removeSolidBall(this);
    }
    
    public void setPosition(float x, float y) {
    	body.setTransform(new Vec2(x,y), 0);
    }
}
