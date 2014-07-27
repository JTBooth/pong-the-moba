package manager;

import pong.Player;
import pong.Pong;
import spell.Spell;
import utils.Debugger;

public class SpellManager {
    Debugger debbie = new Debugger(SpellManager.class.getSimpleName());
    Pong pong;

    public SpellManager(Pong pong) {
        this.pong = pong;
    }

    public void castSpell(Player player, int key) {
        player.getSpell(key).cast();
    }

    public void update() {
        for (Player player : pong.getPlayers().values()) {
            for (Spell spell : player.getSpells().values()) {
                spell.coolDown();
            }
        }
    }
}
