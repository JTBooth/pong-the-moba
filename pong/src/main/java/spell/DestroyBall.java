package spell;

import pong.DelayedEffect;
import shapes.Ball;

public class DestroyBall extends DelayedEffect {
    Ball ball;

    public DestroyBall(Ball ball, int timeout) {
        super(timeout);
        this.ball = ball;
    }

    @Override
    public boolean timeToAct() {
        return ticksRemaining == 0;
    }

    @Override
    public void takeAction() {
        ball.destroy();
    }

}
