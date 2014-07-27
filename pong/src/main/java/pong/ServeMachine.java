package pong;

import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Vec2;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import serialize.Packet;
import serialize.Pattern;
import serialize.PongPacket;
import shapes.Ball;
import utils.Debugger;
import utils.Settings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rbooth on 7/26/14.
 */
public class ServeMachine extends PongPacket {
    float xPos;
    float yPos;
    float rotation;
    public final float maxRotation = MathUtils.QUARTER_PI;
    Debugger debbie;

    public ServeMachine() {}

    public ServeMachine(float xPos, float yPos, Pong pong) {
        this.xPos=xPos;
        this.yPos=yPos;
        rotation=0;
        debbie = new Debugger(ServeMachine.class.getSimpleName());
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
    public List<Object> setSerialData() {

        ArrayList<Object> serialData = new ArrayList<Object>(3);
        serialData.add((rotation < 0 ? rotation + MathUtils.TWOPI : rotation));
        serialData.add(xPos);
        serialData.add(yPos);

        return serialData;
    }

    @Override
    public List<Packet> getSerialPattern() {

        ArrayList<Packet> packets = new ArrayList<Packet>(3);
        packets.add(new Packet(Pattern.FLOAT2B, MathUtils.TWOPI));          // Rotation
        packets.add(new Packet(Pattern.FLOAT2B, Settings.windowMeters[0])); // X Position
        packets.add(new Packet(Pattern.FLOAT2B, Settings.windowMeters[1])); // Y Position
        return packets;
    }

    @Override
    public void extractData(List<Packet> data, Graphics graphics) {
        float rotation = (Float) data.get(0).data;
        int xPos = Settings.m2p((Float) data.get(1).data);
        int yPos = Settings.m2p((Float) data.get(2).data);

        float xDiff = (float) Math.cos(rotation)*Settings.serveMachineLineLength;
        float yDiff = (float) Math.sin(rotation)*Settings.serveMachineLineLength;
        System.out.println("serveMachine at " + xPos + ", " + yPos);
        graphics.drawGradientLine(xPos-xDiff, yPos-yDiff, new Color(255,0,0), xPos+xDiff, yPos+yDiff, new Color(0,0,255));

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

    public float getRotation() {
        return rotation;
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
