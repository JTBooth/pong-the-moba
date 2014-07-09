package spell;

import org.lwjgl.input.Keyboard;

import java.util.HashMap;
import java.util.Map;

import pong.Pong;
import utils.Settings;
import server.Player;
import utils.Debugger;

public class Spellkeeper {
    Debugger debbie = new Debugger(Spellkeeper.class.getSimpleName(), Debugger.DEBUG | Debugger.INFO);
    Pong pong;
    Spell[] p1spells;
    Spell[] p2spells;
    byte[] mana;
    Map<Integer, Integer> commandSpellMap;
    int i = 0;

    public Spellkeeper(Pong pong) {
        this.pong = pong;

        p1spells = new Spell[]{
        		new LaserSpell(pong, pong.getPlayerL()),
        		new RestitutionBoost("Restitution Boost", pong.getPlayerL(), pong),
        		};
        
        p2spells = new Spell[]{
        		new LaserSpell(pong, pong.getPlayerR()),
        		new RestitutionBoost("Restitution Boost", pong.getPlayerR(), pong)
        		};
        
        commandSpellMap = new HashMap<Integer, Integer>();
        commandSpellMap.put(Keyboard.KEY_SPACE, 0);
        commandSpellMap.put(Keyboard.KEY_Q, 1);
        mana = new byte[]{0, 0};
    }

    public boolean tryToCast(Player player, int key) {
        if (player.isPlayer(pong.getPlayerL())) {
            Spell spell = p1spells[commandSpellMap.get(key)];
            if (mana[0] > spell.getCost() && spell.getCooldownCounter() == 0) {
                debbie.i("Player Left casted a spell");
                spell.cast();
                mana[0] -= spell.getCost();
                spell.setCooldownCounter(spell.getCooldown());
                return true;
            }
        } else if (player.isPlayer(pong.getPlayerR())) {
            Spell spell = p2spells[commandSpellMap.get(key)];
            if (mana[1] > spell.getCost() && spell.getCooldownCounter() == 0) {
                debbie.i("Player Right casted a spell");
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
        if (i % Settings.ticksPerManaGain == 0) {
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
        pong.setMana(mana[0], mana[1]);
    }
}
