package shapes;


import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;
import java.util.List;

import pong.Player;
import pong.Pong;
import serialization.Packet;
import serialization.Pattern;
import spell.ServeMachineWander;
import utils.Debugger;
import utils.Settings;

/**
 * Created by rbooth on 7/26/14.
 */
public class ServeMachine extends PongShape {
    float xPos;
    float yPos;
    float rotation;
    Debugger debbie = new Debugger(ServeMachine.class.getSimpleName());

    public ServeMachine(float xPos, float yPos, Pong pong) {
        this.xPos=xPos;
        this.yPos=yPos;
        rotation=0;
        pong.getDelayedEffects().add(new ServeMachineWander(this, Settings.serveMachineMaxDist, Settings.serveMachineStretch));
    }

    public void serve(int player, Ball ball) {
        float xVelocity = (float) Math.cos(rotation)*Settings.serveSpeed;
        float yVelocity = (float) Math.sin(rotation)*Settings.serveSpeed;
        Vec2 ballVelocity;
        switch (player) {
            case Player.LEFT:
                ballVelocity = new Vec2(-xVelocity, -yVelocity);
                break;
            case Player.RIGHT:
                ballVelocity = new Vec2(xVelocity, yVelocity);
                break;
            default:
                debbie.e("You served the ball to not a player: player # " + player
                        + ". Players are 0 and 1.");
                throw new RuntimeException();
        }
        ball.setPosition(xPos, yPos);
        ball.getBody().setAngularVelocity(0);
        ball.getBody().setLinearVelocity(ballVelocity);
        System.out.println("reset ball to (" + ball.getX() + ", " + ball.getY() + ")");
        debbie.i("reset ball to (" + ball.getX() + ", " + ball.getY() + ")");
    }



    @Override
    public List<Packet> setSerialData() {
        ArrayList<Packet> packets = new ArrayList<Packet>(3);
        packets.add(Packet.data(Pattern.FLOAT2B, getAngle(), MathUtils.TWOPI));          // Rotation
        packets.add(Packet.data(Pattern.FLOAT2B, xPos, Settings.windowMeters[0])); // X Position
        packets.add(Packet.data(Pattern.FLOAT2B, yPos, Settings.windowMeters[1])); // Y Position
        return packets;
    }


    public float getxPos() {
        return xPos;
    }
    public float getyPos() {
        return yPos;
    }

    public void setxPos(float xPos) {
        this.xPos=xPos;
    }
    public void setyPos(float yPos) {
        this.yPos=yPos;
    }

    @Override
    public float getAngle() {
        return rotation < 0 ? rotation + MathUtils.TWOPI : rotation;
    }

    public void setRotation(float rotation) {

        if (rotation > MathUtils.PI) {
            rotation -= MathUtils.TWOPI;
        }
        else if (rotation < -MathUtils.PI) {
            rotation += MathUtils.TWOPI;
        }
        this.rotation=rotation;
    }
}
