package pong;

import java.util.HashMap;
import java.util.Map;

import client.CommandUpdate;
import shapes.Paddle;
import spell.Spell;
import utils.Registry;
import utils.Settings;

public class Player {
    final public static int TOTAL = 2;
    final public static int LEFT = 0;
    final public static int RIGHT = 1;

    private Pong pong;
    private Paddle paddle;

    private int who;
    private long id;

    private CommandUpdate commands;
    private Map<Integer, Spell> spells;
    public byte mana;

    public Player(long id) {
        this.id = id;
        this.mana = 0;
        commands = new CommandUpdate();
    }

    /** Step **/
    public void step(long tick) {
        if (tick % Settings.ticksPerManaGain == 0 && mana < Settings.maxMana) {
            mana += 1;
        }
    }

    /** Initialize Spells **/
    private void initializeSpells() {
        spells = new HashMap<Integer, Spell>(Registry.spells.size());
        try {
            Spell spell;
            for (Map.Entry<Integer, Class<? extends Spell>> entry : Registry.spells.entrySet()) {
                spell = entry.getValue().newInstance();
                spell.setup(pong, this);
                spells.put(entry.getKey(), spell);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * GETTERS *
     */
    public Paddle getPaddle() {
        return paddle;
    }

    public Map<Integer, Spell> getSpells() {
        return spells;
    }

    /**
     * SETTERS *
     */
    public void setPaddle(Paddle paddle) {
        this.paddle = paddle;
    }

    public Spell getSpell(int key) {
        return spells.get(key);
    }

    public int[] getKeys() {
        return commands.getKeys();
    }

    public void setKeys(CommandUpdate update) {
        commands = update;
    }

    public int who() {
        return who;
    }

    public void setWho(int who) {
        this.who = who;
    }

    public void setPong(Pong pong) {
        this.pong = pong;
        initializeSpells();
    }

    /**
     * IDENTITY *
     */
    public boolean isPlayer(Player otherPlayer) {
        return otherPlayer.getId() == id;
    }

    public long getId() {
        return id;
    }
}
