package spell;

import pong.Player;

public class RestitutionBoost extends Spell {
    Player player;

    @Override
    public void applyEffects() {
        player.getPaddle().getFixture().setRestitution(2.5f);
        pong.getDelayedEffects().add(new SetRestitution(player.getPaddle(), 1, 20));
    }

    @Override
    public int setCost() {
        return 3;
    }

    @Override
    public int setCoolDown() {
        return 30;
    }
}
