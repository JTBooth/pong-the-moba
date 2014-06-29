package pong;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import server.Player;

public class Spellkeeper {
	Spell[] p1spells;
	Spell[] p2spells;
	int[] mana;
	Map<Integer, Integer> commandSpellMap;
	Pong pong;
	
	public Spellkeeper(Pong pong) {
		this.pong=pong;
		p1spells = new Spell[] {new LaserSpell(pong, pong.getPlayer(0))};
		p2spells = new Spell[] {new LaserSpell(pong, pong.getPlayer(1))};
		commandSpellMap = new HashMap<Integer, Integer>();
		commandSpellMap.put(Keyboard.KEY_SPACE, 0);
		mana = new int[] {0,0};
	}
	
	public boolean tryToCast(Player player, int key) {
		if (player == pong.getPlayer(0)) {
			Spell spell = p1spells[commandSpellMap.get(key)];
			if (mana[0] > spell.getCost() && spell.getCooldownCounter() == 0) {
				spell.cast();
				mana[0] -= spell.getCost();
				spell.setCooldownCounter(spell.getCooldown());
				return true;
			}
		} else if (player == pong.getPlayer(1)) {
			Spell spell = p1spells[commandSpellMap.get(key)];
			if (mana[1] > spell.getCost() && spell.getCooldownCounter() == 0) {
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
	
	public void decreaseCooldowns() {
		for (Spell spell : p1spells) {
			if (spell.cooldown > 0) {
				spell.cooldownCounter -= 1;
			}
		}
		for (Spell spell : p2spells) {
			if (spell.cooldown > 0) {
				spell.cooldownCounter -= 1;
			}
		}
	}
}
