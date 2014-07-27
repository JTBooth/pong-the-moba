package shapes;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import java.util.ArrayList;
import java.util.List;

import serialize.Packet;
import serialize.Pattern;
import serialize.PongPacket;
import utils.Debugger;
import utils.Settings;

import static serialize.Bytes.uByte;

/**
 * Created by sihrc on 6/28/14.
 */
public class InfoBoard extends PongPacket {
    Debugger debbie = new Debugger(InfoBoard.class.getSimpleName());

    /**
     * Mana *
     */
    private ManaBar leftMana = new ManaBar(Settings.maxMana, 0);
    private ManaBar rightMana = new ManaBar(Settings.maxMana, Settings.windowSize[0] - Settings.manaBarWidth);

    /**
     * Constructor *
     */
    public InfoBoard() {
    }

    @Override
    public List<Packet> getSerialPattern() {
        return new ArrayList<Packet>() {{
            add(new Packet(Pattern.BYTE)); //LEFT
            add(new Packet(Pattern.BYTE)); //RIGHT
            add(new Packet(Pattern.BYTE)); //Left Mana
            add(new Packet(Pattern.BYTE)); //Right Mana
        }};
    }


    @Override
    public void extractData(List<Packet> data, Graphics graphics) {
        int counter = 0;
        graphics.setColor(Settings.colorMap.get('0'));
        graphics.drawString(String.valueOf(data.get(counter++).data), Settings.scorePositions[0], Settings.scorePositions[1]);

        //Draw right score
        graphics.setColor(Settings.colorMap.get('2'));
        graphics.drawString(String.valueOf(data.get(counter++).data), Settings.scorePositions[2], Settings.scorePositions[3]);

        //Draw left mana
        debbie.e("READING " + data.get(counter) + " MANA");

        leftMana.setCurrentMana((Byte) data.get(counter++).data);
        leftMana.render(graphics);

        //Draw right mana
        rightMana.setCurrentMana((Byte) data.get(counter++).data);
        rightMana.render(graphics);
    }

    private class ManaBar {
        private byte maxMana;
        private byte currentMana;
        private float currentFraction;
        private int x;

        ManaBar(byte maxMana, int x) {
            this.maxMana = maxMana;
            this.currentMana = 0;
            this.x = x;
        }

        byte getCurrentMana() {
            return currentMana;
        }

        void setCurrentMana(byte currentMana) {
            this.currentMana = currentMana;
            this.currentFraction = ((float) uByte(currentMana)) / (uByte(maxMana));
        }

        void render(Graphics graphics) {
            graphics.setColor(Settings.colorMap.get('4'));
            int height = (int) (currentFraction * Settings.windowSize[1]);
            int y = Settings.windowSize[1] / 2 - height / 2;

            debbie.d("mana bar constructed with x: " + x + " y: " + y + " width: " + Settings.manaBarWidth + " height: " + height + " cf: " + currentFraction);
            Rectangle rect = new Rectangle(x, y, Settings.manaBarWidth, height);
            debbie.e("FILLING " + currentMana + " MANA");
            graphics.fill(rect);
        }
    }
}
