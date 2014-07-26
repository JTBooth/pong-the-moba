package pong;

import org.jbox2d.common.Vec2;
import org.newdawn.slick.Graphics;
import serialize.Packet;
import serialize.PongPacket;
import shapes.Ball;
import utils.Debugger;
import utils.Settings;

import java.util.List;

/**
 * Created by rbooth on 7/26/14.
 */
public class ServeMachine extends PongPacket {
    float xPos;
    float yPos;
    Debugger debbie;

    public ServeMachine(float xPos, float yPos) {
        this.xPos=xPos;
        this.yPos=yPos;
        debbie = new Debugger(ServeMachine.class.getSimpleName());
    }

    public void serve(int player, Ball ball) {
        Vec2 ballVelocity;
        switch (player) {
            case Player.LEFT:
                ballVelocity = new Vec2(-Settings.serveSpeed, 0);
                break;
            case Player.RIGHT:
                ballVelocity = new Vec2(Settings.serveSpeed, 0);
                break;
            default:
                debbie.e("You served the ball to not a player: player # " + player
                        + ". Players are 0 and 1.");
                throw new RuntimeException();
        }
        ball.setPosition(xPos, yPos);
        ball.getBody().setAngularVelocity(0);
        ball.getBody().setLinearVelocity(ballVelocity);
        debbie.i("reset ball to (" + ball.getX() + ", " + ball.getY() + ")");
    }

    @Override
    public List<Object> setSerialData() {
        return null;
    }

    @Override
    public List<Packet> getSerialPattern() {
        return null;
    }

    @Override
    public void extractData(List<Packet> data, Graphics graphics) {

    }
}
