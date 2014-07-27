package spell;

import shapes.Paddle;

public class SetRestitution extends DelayedSpell {
    Paddle paddle;
    float restitution;

    public SetRestitution(Paddle paddle, float restitution, int timeout) {
        super(timeout);
        this.paddle = paddle;
        this.restitution = restitution;
    }

    @Override
    public boolean timeToAct() {

        return ticksRemaining == 0;
    }

    @Override
    public void takeAction() {
        paddle.getFixture().setRestitution(restitution);

    }

}
