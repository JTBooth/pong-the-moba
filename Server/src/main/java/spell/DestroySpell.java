package spell;

import shapes.PongShape;

public class DestroySpell extends DelayedSpell {
    PongShape shape;

    public DestroySpell(PongShape pongShape, int timeout) {
        super(timeout);
        this.shape = pongShape;
    }

    @Override
    public boolean timeToAct() {
        return ticksRemaining == 0;
    }

    @Override
    public void takeAction() {
        shape.destroy();
    }

}
