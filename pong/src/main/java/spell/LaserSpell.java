package spell;

import shapes.Ball;

public class LaserSpell extends Spell {

    @Override
    public void applyEffects() {
        Ball laser = pong.makeLaser(player);
        pong.addShape(laser);
        DestroySpell destroySpell = new DestroySpell(laser, 30);
        pong.getDelayedEffects().add(destroySpell);
    }

    @Override
    public int setCost() {
        return 5;
    }

    @Override
    public int setCoolDown() {
        return 60;
    }

}
