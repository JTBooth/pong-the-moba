package pong;

import org.lwjgl.input.Keyboard;

import java.util.HashMap;
import java.util.Map;

import server.Player;
import utils.Debugger;

public class Spellkeeper {
    Debugger debbie = new Debugger(Spellkeeper.class.getSimpleName(), Debugger.DEBUG);
	Spell[] p1spells;
	Spell[] p2spells;
	int[] mana;
	Map<Integer, Integer> commandSpellMap;
	int i = 0;
	
	public Spellkeeper() {
		p1spells = new Spell[] {new LaserSpell(Pong.pong, Pong.pong.getPlayer(0))};
		p2spells = new Spell[] {new LaserSpell(Pong.pong, Pong.pong.getPlayer(1))};
		commandSpellMap = new HashMap<Integer, Integer>();
		commandSpellMap.put(Keyboard.KEY_SPACE, 0);
		mana = new int[] {0,0};
	}
	
	public boolean tryToCast(Player player, int key) {
        debbie.d(Pong.pong.getStringPlayer(player) + " trying to cast key " + key);
        int pos = Pong.pong.getStringPlayer(player).equals("LEFT")?0:1;
        if (player.getId() == Pong.pong.getPlayer(0).getId()) {
			Spell spell = p1spells[commandSpellMap.get(key)];
			if (mana[0] > spell.getCost() && spell.getCooldownCounter() == 0) {
                System.out.println("CASTED");
				spell.cast();
				mana[0] -= spell.getCost();
				spell.setCooldownCounter(spell.getCooldown());
				return true;
			}
		} else if (player.getId() == Pong.pong.getPlayer(1).getId()) {
			Spell spell = p1spells[commandSpellMap.get(key)];
            if (mana[1] > spell.getCost() && spell.getCooldownCounter() == 0) {
                System.out.println("CASTED");
                spell.cast();
				mana[1] -= spell.getCost();
				spell.setCooldownCounter(spell.getCooldown());
				return true;
			}
		} else {
			throw new RuntimeException();
		}
		return false;
	}
	
	public void update() {
		++i;
		if (i % 180 == 0) {
			mana[0] += 1;
			mana[1] += 1;
			i = 0;
		}
		for (Spell spell : p1spells) {
			if (spell.cooldownCounter > 0) {
				spell.cooldownCounter -= 1;
			}
		}
		for (Spell spell : p2spells) {
			if (spell.cooldownCounter > 0) {
				spell.cooldownCounter -= 1;
			}
		}
	}
}
