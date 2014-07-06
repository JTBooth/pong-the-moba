package spell;

import server.Player;

public class RestitutionBoost extends Spell {

	public RestitutionBoost(String name, int cost, int cooldown, Player player) {
		super(name, cost, cooldown, player);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void cast() {
		player.getPaddle().getFixture().setRestitution(2.5f);
		
	}

}
