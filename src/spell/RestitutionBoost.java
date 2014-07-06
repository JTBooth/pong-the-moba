package spell;

import pong.Pong;
import server.Player;

public class RestitutionBoost extends Spell {
    Player player;

	public RestitutionBoost(String name, Player player) {
		super(name, 3, 90, player);
        this.player=player;
	}

	@Override
	public void cast() {
		player.getPaddle().getFixture().setRestitution(2.5f);
		Pong.pong.getDelayedEffects().add(new SetRestitution(player.getPaddle(), 1, 20));
	}

}
